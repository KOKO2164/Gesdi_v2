package com.koko.gesdi_v2.entidad

data class CategoryTransaction (
    var category_id: Int,
    var category_name: String
) {
    override fun toString(): String {
        return category_name
    }
}