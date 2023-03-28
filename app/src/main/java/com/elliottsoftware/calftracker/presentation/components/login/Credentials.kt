package com.elliottsoftware.calftracker.presentation.components.login

@JvmInline
value class Email(val value: String)

@JvmInline
value class Password(val value: String)

data class Credentials(
    val email: Email = Email(""),
    val password: Password = Password(""),
)