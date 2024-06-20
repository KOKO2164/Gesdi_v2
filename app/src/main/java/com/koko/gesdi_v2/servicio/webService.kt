package com.koko.gesdi_v2.servicio

import com.google.gson.GsonBuilder
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Meta
import com.koko.gesdi_v2.entidad.Transaccion
import com.koko.gesdi_v2.entidad.Usuario
import com.koko.gesdi_v2.request.LoginRequest
import com.koko.gesdi_v2.request.SignupRequest
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

    //@GET("/users/{user_id}")
    //suspend fun getUsuario(@Path("user_id") user_id: Int): Response<Usuario>

    @POST("/categories/add")
    suspend fun addCategoria(@Body categoria: Categoria): Response<String>

    @GET("/categories/{user_id}")
    suspend fun getCategorias(@Path("user_id") user_id: Int): Response<CategoriesResponse>

    @GET("/categories-search/{user_id}")
    suspend fun buscarCategorias(@Path("user_id") user_id: Int, @Query("category_name") category_name: String): Response<CategoriesResponse>

    @GET("/types")
    suspend fun getTipos(): Response<TypesResponse>

    @POST("/transactions/add")
    suspend fun addTransaccion(@Body transaccion: Transaccion): Response<String>

    @GET("/transactions/{user_id}")
    suspend fun getTransacciones(@Path("user_id") user_id: Int): Response<TransactionsResponse>

    @GET("/transactions-search/{user_id}")
    suspend fun buscarTransacciones(@Path("user_id") user_id: Int, @Query("transaction_description") transaction_description: String): Response<TransactionsResponse>

    //@GET("/category_transactions/{user_id}")
    //suspend fun getCategoriasTransaccion(@Path("user_id") user_id: Int): Response<CategoryTransactionResponse>

    @POST("/goals/add")
    suspend fun addMeta(@Body meta: Meta): Response<String>

    @GET("/goals/{user_id}")
    suspend fun getMetas(@Path("user_id") user_id: Int): Response<GoalsResponse>

    @GET("/goals-search/{user_id}")
    suspend fun buscarMetas(@Path("user_id") user_id: Int, @Query("goal_name") goal_name: String): Response<GoalsResponse>

    //@GET("/goals/{goal_id}")
    //suspend fun getmeta(@Path("goal_id") goal_id: Int): Response<Meta>
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