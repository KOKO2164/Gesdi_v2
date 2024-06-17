package com.koko.gesdi_v2.entidad

data class Meta (
    var goal_id: Int,
    var goal_name: String,
    var goal_target_amount: Double,
    var goal_actual_amount: Double,
    var goal_deadline: String,
    var user_id: Int
)
