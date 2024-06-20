package com.koko.gesdi_v2.entidad

data class Tipo (
    var type_id: Int,
    var type_name: String
) {
    override fun toString(): String {
        return type_name
    }
}