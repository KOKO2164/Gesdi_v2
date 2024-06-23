package com.koko.gesdi_v2.servicio

import com.google.gson.GsonBuilder
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Meta
import com.koko.gesdi_v2.entidad.Presupuesto
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

    @POST("/goals/add")
    suspend fun addMeta(@Body meta: Meta): Response<String>

    @GET("/goals/{user_id}")
    suspend fun getMetas(@Path("user_id") user_id: Int): Response<GoalsResponse>

    @GET("/goals-search/{user_id}")
    suspend fun buscarMetas(@Path("user_id") user_id: Int, @Query("goal_name") goal_name: String): Response<GoalsResponse>

    @POST("/budgets/add")
    suspend fun addPresupuesto(@Body presupuesto: Presupuesto): Response<String>

    @GET("/budgets/{user_id}")
    suspend fun getPresupuestos(@Path("user_id") user_id: Int): Response<BudgetsResponse>

    @GET("/budgets-search/{user_id}")
    suspend fun buscarPresupuestos(@Path("user_id") user_id: Int, @Query("budget_name") budget_name: String): Response<BudgetsResponse>

    @GET("/reports/{user_id}")
    suspend fun getReporte(@Path("user_id") user_id: Int, @Query("month") month: Int, @Query("year") year: Int): Response<ReportResponse>
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