package com.gwolf.tvlauncher.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gwolf.tvlauncher.data.AppModel
import com.gwolf.tvlauncher.ui.adapter.AppGridAdapter
import com.gwolf.tvlauncher.ui.dialog.AppActionBottomSheet
import com.gwolf.tvlauncher.util.PackageReceiver
import com.gwolf.tvlauncher.util.TvKeyHandler

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var packageReceiver: PackageReceiver? = null

    private lateinit var favoritesAdapter: AppGridAdapter
    private lateinit var allAppsAdapter: AppGridAdapter

    private var focusedApp: AppModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val layoutId = resources.getIdentifier("activity_main", "layout", packageName)
        if (layoutId != 0) {
            setContentView(layoutId)
        } else {
            val root = android.widget.FrameLayout(this)
            setContentView(root)
        }

        setupAdapters()
        setupViews()
        observeData()
    }

    override fun onStart() {
        super.onStart()
        registerPackageReceiver()
        viewModel.loadApps()
    }

    override fun onStop() {
        super.onStop()
        unregisterPackageReceiver()
    }

    private fun setupAdapters() {
        favoritesAdapter = AppGridAdapter(
            onAppClick = { app -> launchApp(app) },
            onAppLongClick = { app, _ -> showAppActions(app) }
        )

        allAppsAdapter = AppGridAdapter(
            onAppClick = { app -> launchApp(app) },
            onAppLongClick = { app, _ -> showAppActions(app) }
        )
    }

    private fun setupViews() {
        val rvFavoritesId = resources.getIdentifier("rv_favorites", "id", packageName)
        val rvAppsId = resources.getIdentifier("rv_apps", "id", packageName)
        val btnSettingsId = resources.getIdentifier("btn_settings", "id", packageName)

        if (rvFavoritesId != 0) {
            findViewById<RecyclerView>(rvFavoritesId)?.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = favoritesAdapter
            }
        }

        if (rvAppsId != 0) {
            val rvApps = findViewById<RecyclerView>(rvAppsId)
            rvApps?.adapter = allAppsAdapter
            setupDynamicGrid(rvApps)
        }

        if (btnSettingsId != 0) {
            findViewById<View>(btnSettingsId)?.setOnClickListener {
                startActivity(Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    private fun setupDynamicGrid(recyclerView: RecyclerView?) {
        if (recyclerView == null) return

        val userSpan = viewModel.getGridColumns()
        if (userSpan > 0) {
            recyclerView.layoutManager = GridLayoutManager(this, userSpan)
            return
        }

        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = recyclerView.width
                if (width <= 0) {
                    recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 5)
                    return
                }

                val cardWidthRes = resources.getIdentifier("app_card_width", "dimen", packageName)
                val spacingSmRes = resources.getIdentifier("spacing_sm", "dimen", packageName)

                val cardWidth = if (cardWidthRes != 0) {
                    resources.getDimensionPixelSize(cardWidthRes)
                } else {
                    (196 * resources.displayMetrics.density).toInt()
                }

                val gap = if (spacingSmRes != 0) {
                    resources.getDimensionPixelSize(spacingSmRes) * 2
                } else {
                    (16 * resources.displayMetrics.density).toInt()
                }

                val calculatedSpan = (width + gap) / (cardWidth + gap)
                val finalSpan = calculatedSpan.coerceIn(4, 6)

                recyclerView.layoutManager = GridLayoutManager(this@MainActivity, finalSpan)
            }
        }
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    private fun observeData() {
        viewModel.favoriteApps.observe(this) { list ->
            favoritesAdapter.submitList(list)
            val rvFavId = resources.getIdentifier("rv_favorites", "id", packageName)
            if (rvFavId != 0) {
                findViewById<View>(rvFavId)?.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
            }
        }

        viewModel.allApps.observe(this) { list ->
            allAppsAdapter.submitList(list)
        }
    }

    private fun launchApp(app: AppModel) {
        val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
        }
    }

    private fun showAppActions(app: AppModel) {
        val dialog = AppActionBottomSheet.newInstance(
            app = app,
            onPinToggle = { target ->
                if (target.isFavorite) {
                    viewModel.unpinApp(target.packageName)
                } else {
                    viewModel.pinApp(target.packageName)
                }
            },
            onHideApp = { target ->
                viewModel.hideApp(target.packageName)
            },
            onActionCompleted = {
                // List refreshed automatically via LiveData in ViewModel
            }
        )
        dialog.show(supportFragmentManager, "AppActionBottomSheet")
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (TvKeyHandler.isBackKey(event.keyCode)) {
                val dialog = supportFragmentManager.findFragmentByTag("AppActionBottomSheet") as? AppActionBottomSheet
                if (dialog != null && dialog.isVisible) {
                    dialog.dismiss()
                    return true
                }
                val rvFavoritesId = resources.getIdentifier("rv_favorites", "id", packageName)
                if (rvFavoritesId != 0) {
                    val rvFav = findViewById<RecyclerView>(rvFavoritesId)
                    if (rvFav != null && rvFav.visibility == View.VISIBLE && rvFav.childCount > 0) {
                        rvFav.getChildAt(0)?.requestFocus()
                        return true
                    }
                }
                val rvAppsId = resources.getIdentifier("rv_apps", "id", packageName)
                if (rvAppsId != 0) {
                    val rvApps = findViewById<RecyclerView>(rvAppsId)
                    if (rvApps != null && rvApps.childCount > 0) {
                        rvApps.getChildAt(0)?.requestFocus()
                        return true
                    }
                }
                return true
            }

            if (TvKeyHandler.isMenuKey(event.keyCode)) {
                val currentFocus = currentFocus
                if (currentFocus != null) {
                    val rvAppsId = resources.getIdentifier("rv_apps", "id", packageName)
                    val rvFavId = resources.getIdentifier("rv_favorites", "id", packageName)
                    val rvApps = if (rvAppsId != 0) findViewById<RecyclerView>(rvAppsId) else null
                    val rvFav = if (rvFavId != 0) findViewById<RecyclerView>(rvFavId) else null

                    val app = findAppForFocusedView(currentFocus, rvFav, favoritesAdapter)
                        ?: findAppForFocusedView(currentFocus, rvApps, allAppsAdapter)

                    if (app != null) {
                        showAppActions(app)
                        return true
                    }
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }

    private fun findAppForFocusedView(
        view: View,
        recyclerView: RecyclerView?,
        adapter: AppGridAdapter
    ): AppModel? {
        if (recyclerView == null) return null
        val itemView = recyclerView.findContainingItemView(view) ?: return null
        val position = recyclerView.getChildAdapterPosition(itemView)
        if (position != RecyclerView.NO_POSITION && position < adapter.currentList.size) {
            return adapter.currentList[position]
        }
        return null
    }

    private fun registerPackageReceiver() {
        if (packageReceiver == null) {
            packageReceiver = PackageReceiver {
                viewModel.loadApps()
            }
            registerReceiver(packageReceiver, PackageReceiver.createIntentFilter())
        }
    }

    private fun unregisterPackageReceiver() {
        packageReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (_: Exception) {}
            packageReceiver = null
        }
    }
}
