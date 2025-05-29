package com.reringuy.support.models.data

import android.util.Patterns

data class EmailPassword(
    var email: String,
    var password: String
) {
    fun isValid(): Boolean {
        return (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                && (password.isNotEmpty() && password.length >= 6)
    }
}
