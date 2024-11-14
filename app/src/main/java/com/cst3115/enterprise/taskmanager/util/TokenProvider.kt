package com.cst3115.enterprise.taskmanager.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Singleton object for providing authentication tokens.
 */
object TokenProvider {

    private const val PREFS_FILENAME = "secure_prefs"
    private const val TOKEN_KEY = "auth_token"

    /**
     * Initializes the EncryptedSharedPreferences.
     *
     * @param context The application context.
     * @return EncryptedSharedPreferences instance.
     */
    private fun getEncryptedPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILENAME,
            getMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    /**
     * Generates or retrieves the MasterKey for encryption.
     *
     * @param context The application context.
     * @return MasterKey instance.
     */
    private fun getMasterKey(context: Context) =
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    /**
     * Retrieves the stored authentication token.
     *
     * @param context The application context.
     * @return The authentication token if available, null otherwise.
     */
    fun getToken(context: Context): String? {
        val securePrefs = getEncryptedPrefs(context)
        return securePrefs.getString(TOKEN_KEY, null)
    }

    /**
     * Saves the authentication token securely.
     *
     * @param context The application context.
     * @param token The JWT token to save.
     */
    fun saveToken(context: Context, token: String) {
        val securePrefs = getEncryptedPrefs(context)
        securePrefs.edit().putString(TOKEN_KEY, token).apply()
    }

    /**
     * Clears the stored authentication token.
     *
     * @param context The application context.
     */
    fun clearToken(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val securePrefs = EncryptedSharedPreferences.create(
            context,
            PREFS_FILENAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        securePrefs.edit().remove(TOKEN_KEY).apply()
    }
}
