package com.koko.gesdi_v2.entidad

data class Reporte (
    var transaction_id: Int,
    var transaction_amount: Double,
    var transaction_date: String,
    var transaction_description: String,
    var category_name: String,
    var type_name: String
)