package com.gwolf.tvlauncher.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.gwolf.tvlauncher.data.AppModel

class AppItemDiffCallback : DiffUtil.ItemCallback<AppModel>() {
    override fun areItemsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: AppModel, newItem: AppModel): Boolean {
        return oldItem == newItem
    }
}
