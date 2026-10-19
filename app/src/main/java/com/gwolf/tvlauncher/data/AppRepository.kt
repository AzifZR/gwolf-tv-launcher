package com.gwolf.tvlauncher.data

import android.content.Context
import android.content.Intent

class AppRepository(private val context: Context) {

    fun loadInstalledApps(): List<AppModel> {
        val pm = context.packageManager
        val tvIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        val mobileIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

        val tvApps = pm.queryIntentActivities(tvIntent, 0)
        val mobileApps = pm.queryIntentActivities(mobileIntent, 0)

        val map = mutableMapOf<String, AppModel>()
        // Prioritaskan leanback activity, fallback ke mobile activity
        for (ri in tvApps) {
            val pkg = ri.activityInfo.packageName
            if (pkg == context.packageName) continue
            map[pkg] = AppModel(
                packageName = pkg,
                activityName = ri.activityInfo.name,
                label = ri.loadLabel(pm).toString(),
                isLeanback = true
            )
        }
        for (ri in mobileApps) {
            val pkg = ri.activityInfo.packageName
            if (pkg == context.packageName || map.containsKey(pkg)) continue
            map[pkg] = AppModel(
                packageName = pkg,
                activityName = ri.activityInfo.name,
                label = ri.loadLabel(pm).toString(),
                isLeanback = false
            )
        }
        return map.values.sortedBy { it.label.lowercase() }
    }
}
