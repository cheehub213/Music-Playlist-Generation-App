package com.aurabeat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ApiConfigReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SET_API_BASE_URL = "com.aurabeat.action.SET_API_BASE_URL"
        const val ACTION_CLEAR_API_BASE_URL = "com.aurabeat.action.CLEAR_API_BASE_URL"
        const val EXTRA_URL = "url"
        private const val PREFS_NAME = "aurabeat_prefs"
        private const val PREF_KEY_API_BASE_URL = "api_base_url"
        private const val TAG = "ApiConfigReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        when (intent.action) {
            ACTION_SET_API_BASE_URL -> {
                val url = intent.getStringExtra(EXTRA_URL)?.trim().orEmpty()
                if (url.isBlank()) {
                    Log.i(TAG, "Missing URL extra")
                    Toast.makeText(context, "No API URL provided", Toast.LENGTH_SHORT).show()
                    return
                }

                prefs.edit().putString(PREF_KEY_API_BASE_URL, url).apply()
                Log.i(TAG, "API base URL set to $url")
                Toast.makeText(context, "API URL updated", Toast.LENGTH_SHORT).show()
            }

            ACTION_CLEAR_API_BASE_URL -> {
                prefs.edit().remove(PREF_KEY_API_BASE_URL).apply()
                Log.i(TAG, "API base URL cleared")
                Toast.makeText(context, "API URL reset", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.i(TAG, "Ignored action: ${intent.action}")
            }
        }
    }
}