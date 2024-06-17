package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.Categoria

data class CategoriesResponse (
    @SerializedName("categoriesList") var listaCategorias:ArrayList<Categoria>
)