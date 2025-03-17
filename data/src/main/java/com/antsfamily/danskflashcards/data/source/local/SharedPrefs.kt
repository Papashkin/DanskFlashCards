package com.antsfamily.danskflashcards.data.source.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class SharedPrefs @Inject constructor(context: Context) {

    companion object {
        private const val KEY_WEB_CLIENT_ID = "WEB_CLIENT_ID"
        private const val NAME_SHARED_PREFS = "secret_shared_prefs"
    }

    private val masterKey = MasterKey.Builder(context, context.packageName)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setUserAuthenticationRequired(false)
        .setRequestStrongBoxBacked(false)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        NAME_SHARED_PREFS,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ).apply {
        this.edit()
            .putString(
                KEY_WEB_CLIENT_ID,
                "692133800678-v9uijmnu84efrqtpgf7ql2kua9cl2afa.apps.googleusercontent.com"
            )
            .apply()
    }

    suspend fun getWebClientId(): String? {
        return prefs.getString(KEY_WEB_CLIENT_ID, null)
    }
}