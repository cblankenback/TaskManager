
package com.cst3115.enterprise.taskmanager.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the fields that can be updated for a user.
 *
 * @property firstName The updated first name of the user.
 * @property lastName The updated last name of the user.
 * @property departmentId The updated department ID.
 * @property availabilityId The updated availability ID.
 * @property roleId The updated role ID.
 */
data class UserDetailsUpdateDTO(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("departmentId") val departmentId: Int,
    @SerializedName("availabilityId") val availabilityId: Int,
    @SerializedName("roleId") val roleId: Int
)
