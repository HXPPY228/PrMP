package com.example.lab.ui.theme

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurityManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun savePassKey(passKey: String) {
        sharedPrefs.edit().putString("internal_passkey", passKey).apply()
    }

    fun getPassKey(): String? {
        return sharedPrefs.getString("internal_passkey", null)
    }

    fun isPassKeySet(): Boolean = getPassKey() != null

    fun validatePassKey(input: String): Boolean {
        return getPassKey() == input
    }
}