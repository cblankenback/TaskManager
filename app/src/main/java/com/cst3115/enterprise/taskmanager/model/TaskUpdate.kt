package com.cst3115.enterprise.taskmanager.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TaskUpdate(
    @SerializedName("taskUpdateId") val taskUpdateId: Int,
    @SerializedName("updateDate") val updateDate: String, // or LocalDateTime if you're parsing
    @SerializedName("taskId") val taskId: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("statusId") val statusId: Int,
    @SerializedName("employeeId") val employeeId: Int
)
