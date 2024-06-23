package com.koko.gesdi_v2.entidad

data class Presupuesto (
    var budget_id: Int,
    var budget_name: String,
    var budget_amount: Double,
    var budget_month: Int?,
    var budget_year: Int,
    var budget_is_total: Int,
    var category_id: Int?,
    var category_name: String?,
    var user_id: Int
)