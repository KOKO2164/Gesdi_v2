package com.koko.gesdi_v2.servicio

import com.google.gson.GsonBuilder
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Meta
import com.koko.gesdi_v2.entidad.Transaccion
import com.koko.gesdi_v2.entidad.Usuario
import com.koko.gesdi_v2.request.LoginRequest
import com.koko.gesdi_v2.request.SignupRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

object AppConstants {
    const val URL_BASE = "http://192.168.18.6:3000"
}

interface WebService {
    @POST("/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<Usuario>

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Usuario>

    @GET("/users/{user_id}")
    suspend fun getUsuario(@Path("user_id") user_id: Int): Response<Usuario>

    @POST("/categories/add")
    suspend fun addCategoria(@Body categoria: Categoria): Response<String>

    @GET("/categories/{user_id}")
    suspend fun getCategorias(@Path("user_id") user_id: Int): Response<CategoriesResponse>

    @GET("/categories")
    suspend fun buscarCategorias(@Query("category_name") category_name: String): Response<CategoriesResponse>

    @GET("/category_types")
    suspend fun getTiposCategoria(): Response<CategoryTypesResponse>

    @GET("/transactions/{user_id}")
    suspend fun getTransacciones(@Path("user_id") user_id: Int): Response<TransactionsResponse>

    @POST("/transactions/add")
    suspend fun addTransaccion(@Body transaccion: Transaccion): Response<String>

    @GET("/transactions-search/{user_id}")
    suspend fun buscarTransacciones(@Path("user_id") user_id: Int, @Query("transaction_description") transaction_description: String): Response<TransactionsResponse>

    @GET("/category_transactions/{user_id}")
    suspend fun getCategoriasTransaccion(@Path("user_id") user_id: Int): Response<CategoryTransactionResponse>

    @GET("/goals/{user_id}")
    suspend fun getMetas(@Path("user_id") user_id: Int): Response<GoalsResponse>

    @GET("/goals/{goal_id}")
    suspend fun getmeta(@Path("goal_id") goal_id: Int): Response<Meta>
}

object RetrofitClient {
    val webService:WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}