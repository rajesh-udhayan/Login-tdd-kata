package com.example.logintdd.main

import androidx.lifecycle.ViewModel

sealed class CredentialValidation {
    object EmptyUserNameAndPassword : CredentialValidation()
    object EmptyUserName : CredentialValidation()
    object EmptyPassword : CredentialValidation()
    object InvalidUserName : CredentialValidation()
    object InvalidPassword : CredentialValidation()
    object InvalidUserNameAndPassword : CredentialValidation()
    object ValidCredentials : CredentialValidation()
}

class MainViewModel : ViewModel() {

    fun validateCredentials(username: String, password: String): CredentialValidation {
        val user = "john"
        val pwd = "1234"

        if (username.isEmpty() && password.isEmpty())
            return CredentialValidation.EmptyUserNameAndPassword
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty())
                return CredentialValidation.EmptyUserName
            if (password.isEmpty())
                return CredentialValidation.EmptyPassword
        }
        if (!username.isEmpty() && !password.isEmpty()) {
            if (!username.equals(user) && !password.equals(pwd))
                return CredentialValidation.InvalidUserNameAndPassword
            if (!username.equals(user))
                return CredentialValidation.InvalidUserName
            if (!password.equals(pwd))
                return CredentialValidation.InvalidPassword
            return CredentialValidation.ValidCredentials
        }
        return CredentialValidation.ValidCredentials
    }
}