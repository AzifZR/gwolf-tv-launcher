package com.gwolf.tvlauncher.data

data class AppModel(
    val packageName: String,
    val activityName: String,
    val label: String,
    val isLeanback: Boolean,
    val isFavorite: Boolean = false,
    val isHidden: Boolean = false,
    val customOrder: Int = 0
)
