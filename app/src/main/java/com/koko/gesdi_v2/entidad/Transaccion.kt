package com.koko.gesdi_v2.entidad

data class Transaccion (
    var transaction_id: Int,
    var transaction_amount: Double,
    var transaction_type: String,
    var transaction_date: String,
    var transaction_description: String,
    var user_id: Int,
    var category_id: Int
)