package com.koko.gesdi_v2.request

data class SignupRequest (
    val user_name: String,
    val user_email: String,
    val user_password: String
)