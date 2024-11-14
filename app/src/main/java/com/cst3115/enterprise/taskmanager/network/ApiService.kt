package com.cst3115.enterprise.taskmanager.network

import com.cst3115.enterprise.taskmanager.model.Availability
import com.cst3115.enterprise.taskmanager.model.Department
import com.cst3115.enterprise.taskmanager.model.EmployeeRequestDTO
import com.cst3115.enterprise.taskmanager.model.EmployeeResponseDTO
import com.cst3115.enterprise.taskmanager.model.LoginRequestDTO
import com.cst3115.enterprise.taskmanager.model.LoginResponseDTO
import com.cst3115.enterprise.taskmanager.model.Role
import com.cst3115.enterprise.taskmanager.model.UserDetails
import com.cst3115.enterprise.taskmanager.model.UserDetailsUpdateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
}
