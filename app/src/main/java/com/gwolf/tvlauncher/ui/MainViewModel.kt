package com.gwolf.tvlauncher.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gwolf.tvlauncher.data.AppModel
import com.gwolf.tvlauncher.data.AppRepository
import com.gwolf.tvlauncher.data.LauncherPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val preferences = LauncherPreferences(application)

    private val _favoriteApps = MutableLiveData<List<AppModel>>()
    val favoriteApps: LiveData<List<AppModel>> = _favoriteApps

    private val _allApps = MutableLiveData<List<AppModel>>()
    val allApps: LiveData<List<AppModel>> = _allApps

    fun loadApps() {
        val installed = repository.loadInstalledApps()
        val pinned = preferences.getPinnedApps()
        val hidden = preferences.getHiddenApps()

        val favList = mutableListOf<AppModel>()
        val mainList = mutableListOf<AppModel>()

        for (app in installed) {
            val isHidden = hidden.contains(app.packageName)
            val isPinned = pinned.contains(app.packageName)

            if (isHidden) continue

            val configured = app.copy(
                isFavorite = isPinned,
                isHidden = false
            )

            if (isPinned) {
                favList.add(configured)
            }
            mainList.add(configured)
        }

        _favoriteApps.value = favList
        _allApps.value = mainList
    }

    fun pinApp(packageName: String) {
        preferences.pinApp(packageName)
        loadApps()
    }

    fun unpinApp(packageName: String) {
        preferences.unpinApp(packageName)
        loadApps()
    }

    fun hideApp(packageName: String) {
        preferences.hideApp(packageName)
        loadApps()
    }

    fun getGridColumns(): Int {
        return preferences.getGridColumns()
    }
}
