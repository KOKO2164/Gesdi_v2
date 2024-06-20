package com.koko.gesdi_v2.entidad

data class Categoria (
    var category_id: Int,
    var category_name: String,
    var user_id: Int,
    var type_id: Int,
    var type_name: String
) {
    override fun toString(): String {
        return category_name
    }
}