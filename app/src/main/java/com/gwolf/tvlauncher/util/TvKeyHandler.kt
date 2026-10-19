package com.gwolf.tvlauncher.util

import android.view.KeyEvent

object TvKeyHandler {

    fun isBackKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK
    }

    fun isMenuKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_MENU
    }

    fun isCenterKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER
    }
}
