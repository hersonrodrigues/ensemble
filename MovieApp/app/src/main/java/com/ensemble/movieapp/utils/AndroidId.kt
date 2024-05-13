package com.ensemble.movieapp.utils

import android.content.Context
import android.provider.Settings.Secure


class AndroidId {
    fun getDeviceId(context: Context): String {
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }
}