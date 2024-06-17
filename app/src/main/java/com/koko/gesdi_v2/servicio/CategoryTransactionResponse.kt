package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.CategoryTransaction

data class CategoryTransactionResponse (
    @SerializedName("category_transactionList") var listaCategoriasTransaccion:ArrayList<CategoryTransaction>
)