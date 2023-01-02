package com.adam.submissionstoryapp

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.adam.submissionstoryapp.model.UserModel

class UserPreference(context: Context) {
    companion object {
        private const val SESSION = "Session"
        private const val TOKEN = "token"
        private const val USERID = "userid"
        private const val NAME = "name"
    }
    private val spec = KeyGenParameterSpec.Builder(
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()
    private val masterKey = MasterKey.Builder(context)
        .setKeyGenParameterSpec(spec)
        .build()
    private var preferences: SharedPreferences = EncryptedSharedPreferences
        .create(
            context,
            SESSION,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setUser(value: UserModel){
        val editor = preferences.edit()
        editor.putString(USERID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.putString(NAME, value.name)
        editor.apply()
    }
    fun getUser(): UserModel {
        val model = UserModel()
        model.userId = preferences.getString(USERID, "")
        model.token = preferences.getString(TOKEN,"")
        model.name = preferences.getString(NAME, "")
        return model
    }

    fun clearSession(){
        val editor = preferences.edit()
        editor.clear().apply()
    }
}