package com.polzzak_android.data.repository

import android.content.Context
import java.util.UUID
import javax.inject.Inject

class GUIDRepository @Inject constructor(context: Context) {
    private val sharedPref = context.getSharedPreferences(
        SHARED_PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )

    fun requestGUID() = sharedPref.getString(GUID_KEY, null) ?: run {
        val editor = sharedPref.edit()
        val newGUID = UUID.randomUUID().toString()
        editor.putString(GUID_KEY, newGUID)
        editor.apply()
        newGUID
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "guid_shared_pref"
        private const val GUID_KEY = "guid_key"
    }
}