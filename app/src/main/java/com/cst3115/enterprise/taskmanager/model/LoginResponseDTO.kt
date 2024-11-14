package com.cst3115.enterprise.taskmanager.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for login responses.
 *
 * @property token Authentication token for the user.
 * @property userId The authenticated user's ID.
 * @property message Optional message from the server.
 */
data class LoginResponseDTO(
    @SerializedName("token") val token: String
)
