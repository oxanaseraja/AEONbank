package android.com.testaeonapp.retrofit

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @Headers(
        "app-key: 12345",
        "v: 1")
    @POST("login")
    suspend fun getAuth(@Body authRequest: AuthRequest): AuthResponse
}