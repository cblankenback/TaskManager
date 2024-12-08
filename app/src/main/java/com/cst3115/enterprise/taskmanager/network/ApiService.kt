package com.cst3115.enterprise.taskmanager.network

import com.cst3115.enterprise.taskmanager.model.Availability
import com.cst3115.enterprise.taskmanager.model.Department
import com.cst3115.enterprise.taskmanager.model.EmployeeRequestDTO
import com.cst3115.enterprise.taskmanager.model.EmployeeResponseDTO
import com.cst3115.enterprise.taskmanager.model.LoginRequestDTO
import com.cst3115.enterprise.taskmanager.model.LoginResponseDTO
import com.cst3115.enterprise.taskmanager.model.Priority
import com.cst3115.enterprise.taskmanager.model.Role
import com.cst3115.enterprise.taskmanager.model.Status
import com.cst3115.enterprise.taskmanager.model.Task
import com.cst3115.enterprise.taskmanager.model.TaskUpdate
import com.cst3115.enterprise.taskmanager.model.UserDetails
import com.cst3115.enterprise.taskmanager.model.UserDetailsUpdateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/availabilities")
    suspend fun getAvailabilities(): Response<List<Availability>>

    @GET("/api/departments")
    suspend fun getDepartments(): Response<List<Department>>

    @GET("/api/roles")
    suspend fun getRoles(): Response<List<Role>>

    @GET("/api/me")
    suspend fun getCurrentUser(): Response<UserDetails>

    @POST("/api/auth/register")
    suspend fun registerUser(@Body employeeRequest: EmployeeRequestDTO): Response<EmployeeResponseDTO>

    @POST("/api/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @PUT("/api/employees/{id}")
    suspend fun updateUserDetails(
        @Path("id") id: Int,
        @Body userDetails: UserDetailsUpdateDTO
    ): Response<EmployeeResponseDTO>
    @GET("/api/employees/{id}")
    suspend fun getEmployee(@Path("id") id: Int): Response<EmployeeResponseDTO>
    @GET("/api/tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("/api/tasks")
    suspend fun createTask(@Body task: CreateTaskRequest): Response<Task>
    @GET("/api/tasks/{id}")
    suspend fun getTask(@Path("id") id: Int): Response<Task>

    @GET("/api/taskupdates")
    suspend fun getTaskUpdates(@Query("taskId") taskId: Int): Response<List<TaskUpdate>>

    @POST("/api/taskupdates")
    suspend fun createTaskUpdate(@Body request: CreateTaskUpdateRequest): Response<TaskUpdate>

    @GET("/api/statuses")
    suspend fun getStatuses(): Response<List<Status>>

    @GET("/api/priorities/{id}")
    suspend fun getPriority(@Path("id") id: Int): Response<Priority>

    @GET("/api/priorities")
    suspend fun getPriorities(): Response<List<Priority>>


}
