package com.example.krishnamaya1.authentication.domain

import android.content.Context
import android.net.Uri
import android.widget.Toast
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

fun isDetailValid(
    name: String,
    bio: String,
    email: String,
    password: String,
    imageUri: Uri?,
    context: Context,
    nameError: String,
    bioError: String,
    emailError: String,
    passwordError: String
): Boolean {
    var isValid = true

    if (name.isBlank()) {
        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (bio.isBlank()) {
        Toast.makeText(context, "Bio cannot be empty", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (!email.isValidEmail()) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (password.length < 8) {
        Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (nameError.isNotEmpty()) {
        Toast.makeText(context, nameError, Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (bioError.isNotEmpty()) {
        Toast.makeText(context, bioError, Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (emailError.isNotEmpty()) {
        Toast.makeText(context, emailError, Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (passwordError.isNotEmpty()) {
        Toast.makeText(context, passwordError, Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (imageUri == null) {
        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    return isValid
}

fun isDetailValid(
    email: String,
    password: String,
    context: Context,
    emailError: String,
    passwordError: String
): Boolean {
    var isValid = true

    if (!email.isValidEmail()) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        isValid = false
    } else if (emailError.isNotEmpty()) {
        Toast.makeText(context, emailError, Toast.LENGTH_SHORT).show()
        isValid = false
    }

    if (password.length < 8) {
        Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
        isValid = false
    } else if (passwordError.isNotEmpty()) {
        Toast.makeText(context, passwordError, Toast.LENGTH_SHORT).show()
        isValid = false
    }

    return isValid
}

// Extension function to validate email format
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun handleFirebaseAuthException(exception: Exception?):String {
    return when (exception) {
        is FirebaseAuthInvalidCredentialsException -> {
            "The email or password is incorrect!"
        }
        is FirebaseAuthInvalidUserException -> {
            "No account found with this email!"
        }
        is FirebaseAuthUserCollisionException -> {
            "An account already exists with this email!"
        }
        is FirebaseNetworkException -> {
            "Please check your internet connection!"
        }
        else -> {
            "Unknown error occurred!"
        }
    }
}

fun isDetailValid(
    name: String,
    bio: String,
    imageUri: Uri?,
    context: Context,
    nameError: String,
    bioError: String,
): Boolean {
    var isValid = true

    if (name.isBlank()) {
        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (bio.isBlank()) {
        Toast.makeText(context, "Bio cannot be empty", Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (nameError.isNotEmpty()) {
        Toast.makeText(context, nameError, Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    if (bioError.isNotEmpty()) {
        Toast.makeText(context, bioError, Toast.LENGTH_SHORT).show()
        isValid = false
        return isValid
    }

    return isValid
}