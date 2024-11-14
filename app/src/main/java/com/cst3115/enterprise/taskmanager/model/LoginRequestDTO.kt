package com.cst3115.enterprise.taskmanager.model

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO(
    @SerializedName("username") val userId: String,
    @SerializedName("password") val password: String
)
