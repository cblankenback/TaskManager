package com.cst3115.enterprise.taskmanager.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing user details fetched from the backend.
 *
 * @property userId The unique identifier for the user.
 * @property username The username of the user.
 * @property role The role assigned to the user.
 * @property otherFields Any additional fields returned by the backend.
 */
data class UserDetails(
    @SerializedName("employeeId") val employeeId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("departmentId") val departmentId: Int,
    @SerializedName("availabilityId") val availabilityId: Int,
    @SerializedName("roleId") val roleId: Int,
    // Add other fields as necessary
)
