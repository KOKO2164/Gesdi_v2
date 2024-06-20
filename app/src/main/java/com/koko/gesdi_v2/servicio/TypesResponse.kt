package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.Tipo

data class TypesResponse (
    @SerializedName("typesList") var listaTipos: ArrayList<Tipo>
)