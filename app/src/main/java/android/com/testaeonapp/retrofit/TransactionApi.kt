package android.com.testaeonapp.retrofit

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TransactionApi {
    @Headers(
        "app-key: 12345",
        "v: 1")
    @GET("payments")
    suspend fun getTransactions(@Header("token") token: String): TransactionResponse



}