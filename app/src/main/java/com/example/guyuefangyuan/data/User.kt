package com.example.guyuefangyuan.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String,
    val email: String = "",
    val fullName: String = "",
    val studentId: String = "",
    val major: String = "",
    val bio: String = ""
)