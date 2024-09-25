package com.example.krishnamaya1.authentication.presentation

import android.content.Context
import android.content.Context.MODE_PRIVATE

object AuthSharedPreferences {
    fun storeData(
        context: Context,
        userId:String,
        userName:String,
        bio:String,
        photoUri:String
    ) {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.putString("userName", userName)
        editor.putString("bio", bio)
        editor.putString("photoUri", photoUri)
        editor.apply()
    }

    fun getUserName(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userName", null)
    }

    fun getBio(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("bio", null)
    }

    fun getId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }

    fun getPhotoUri(context:Context): String? {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("photoUri", null)
    }

    // Function to clear all data from shared preferences
    fun clearData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}