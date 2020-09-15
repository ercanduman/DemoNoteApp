package com.enbcreative.demonoteapp.data.prefs

import android.content.Context
import androidx.preference.PreferenceManager
import com.enbcreative.demonoteapp.USER_ID_INVALID

class Preferences(context: Context) {
    private val appContext = context.applicationContext
    private val preference = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun getUserID() = preference.getInt(KEY_USER_ID, USER_ID_INVALID)
    fun getLastTime() = preference.getString(KEY_LAST_TIME, null)
    fun saveUserID(userId: Int) = preference.edit().putInt(KEY_USER_ID, userId).apply()
    fun saveLastTime(time: String?) = preference.edit().putString(KEY_LAST_TIME, time).apply()

    fun signOut() {
        saveUserID(USER_ID_INVALID)
        saveLastTime(null)
        preference.all.clear()
    }

    companion object {
        private const val KEY_USER_ID = "com.enbcreative.demonoteapp.KEY_USER_ID"
        private const val KEY_LAST_TIME = "com.enbcreative.demonoteapp.KEY_LAST_SAVE_TIME"
    }
}