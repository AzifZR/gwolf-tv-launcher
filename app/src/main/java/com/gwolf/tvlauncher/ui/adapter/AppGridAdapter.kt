package com.gwolf.tvlauncher.ui.adapter

import android.app.ActivityManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gwolf.tvlauncher.data.AppModel

class AppGridAdapter(
    private val onAppClick: (AppModel) -> Unit,
    private val onAppLongClick: (AppModel, View) -> Unit
) : ListAdapter<AppModel, AppGridAdapter.AppViewHolder>(AppItemDiffCallback()) {

    private var iconCache: LruCache<String, Drawable>? = null

    private fun getIconCache(context: Context): LruCache<String, Drawable> {
        return iconCache ?: synchronized(this) {
            iconCache ?: run {
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
                val memClass = am?.memoryClass ?: 64
                val cacheSize = (memClass * 1024 * 1024) / 4
                object : LruCache<String, Drawable>(cacheSize) {
                    override fun sizeOf(key: String, value: Drawable): Int {
                        val width = value.intrinsicWidth.coerceAtLeast(1)
                        val height = value.intrinsicHeight.coerceAtLeast(1)
                        return width * height * 4
                    }
                }.also { iconCache = it }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val context = parent.context
        val layoutId = context.resources.getIdentifier("item_app_card", "layout", context.packageName)
        val view = if (layoutId != 0) {
            LayoutInflater.from(context).inflate(layoutId, parent, false)
        } else {
            // Fallback lightweight card container if layout not yet provided
            val card = com.gwolf.tvlauncher.ui.view.DpadFocusableCard(context)
            card.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            card
        }
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context
        private val cache = getIconCache(context)

        private val imgIconId = context.resources.getIdentifier("img_app_icon", "id", context.packageName)
        private val tvLabelId = context.resources.getIdentifier("tv_app_label", "id", context.packageName)
        private val cardRootId = context.resources.getIdentifier("card_root", "id", context.packageName)

        private val imgAppIcon: ImageView? = if (imgIconId != 0) itemView.findViewById(imgIconId) else null
        private val tvAppLabel: TextView? = if (tvLabelId != 0) itemView.findViewById(tvLabelId) else null
        private val cardRoot: View = if (cardRootId != 0) itemView.findViewById(cardRootId) ?: itemView else itemView

        fun bind(item: AppModel) {
            tvAppLabel?.text = item.label

            if (imgAppIcon != null) {
                val cached = cache.get(item.packageName)
                if (cached != null) {
                    imgAppIcon.setImageDrawable(cached)
                } else {
                    try {
                        val pm = context.packageManager
                        val icon = pm.getApplicationIcon(item.packageName)
                        cache.put(item.packageName, icon)
                        imgAppIcon.setImageDrawable(icon)
                    } catch (_: Exception) {
                        imgAppIcon.setImageDrawable(null)
                    }
                }
            }

            cardRoot.setOnClickListener {
                onAppClick(item)
            }

            cardRoot.setOnLongClickListener {
                onAppLongClick(item, cardRoot)
                true
            }
        }
    }
}
