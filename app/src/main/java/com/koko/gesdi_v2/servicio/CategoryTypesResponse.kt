package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.CategoryType

data class CategoryTypesResponse (
    @SerializedName("category_typesList") var listaTiposCategoria: ArrayList<CategoryType>
)