package com.codingstuff.loginandsignup.Activity

class Validator {
    fun validateInput(email: String, pass: String): Boolean {
        return !(email.isEmpty()) || (pass.isEmpty())
    }

    companion object {
        fun validateInput(email: String, password: String): Boolean {
            return !(email.isEmpty()) || (password.isEmpty())
        }
        //companion object digunakan kotlin sebagai pengganti static pada java dengan aturan singleton
    }
}