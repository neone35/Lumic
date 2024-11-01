package com.arturmaslov.lumic.utils

interface ActivityHelper {
    fun setListeners()
    fun setObservers()
    fun requestPermissions(permissionArray: Array<String>)
}