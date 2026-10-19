package com.gwolf.tvlauncher.data

import android.content.Context
import android.content.SharedPreferences

class LauncherPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getPinnedApps(): Set<String> {
        return prefs.getStringSet(KEY_PINNED_APPS, emptySet())?.toSet() ?: emptySet()
    }

    fun setPinnedApps(packages: Set<String>) {
        prefs.edit().putStringSet(KEY_PINNED_APPS, packages).apply()
    }

    fun pinApp(packageName: String) {
        val current = getPinnedApps().toMutableSet()
        current.add(packageName)
        setPinnedApps(current)
    }

    fun unpinApp(packageName: String) {
        val current = getPinnedApps().toMutableSet()
        current.remove(packageName)
        setPinnedApps(current)
    }

    fun isPinned(packageName: String): Boolean {
        return getPinnedApps().contains(packageName)
    }

    fun getHiddenApps(): Set<String> {
        return prefs.getStringSet(KEY_HIDDEN_APPS, emptySet())?.toSet() ?: emptySet()
    }

    fun setHiddenApps(packages: Set<String>) {
        prefs.edit().putStringSet(KEY_HIDDEN_APPS, packages).apply()
    }

    fun hideApp(packageName: String) {
        val current = getHiddenApps().toMutableSet()
        current.add(packageName)
        setHiddenApps(current)
    }

    fun unhideApp(packageName: String) {
        val current = getHiddenApps().toMutableSet()
        current.remove(packageName)
        setHiddenApps(current)
    }

    fun isHidden(packageName: String): Boolean {
        return getHiddenApps().contains(packageName)
    }

    fun getGridColumns(): Int {
        return prefs.getInt(KEY_GRID_COLUMNS, DEFAULT_GRID_COLUMNS)
    }

    fun setGridColumns(columns: Int) {
        prefs.edit().putInt(KEY_GRID_COLUMNS, columns).apply()
    }

    companion object {
        private const val PREFS_NAME = "ohryu_launcher_prefs"
        const val KEY_PINNED_APPS = "KEY_PINNED_APPS"
        const val KEY_HIDDEN_APPS = "KEY_HIDDEN_APPS"
        const val KEY_GRID_COLUMNS = "KEY_GRID_COLUMNS"
        const val DEFAULT_GRID_COLUMNS = 5
    }
}
