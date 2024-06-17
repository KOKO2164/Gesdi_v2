package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.Transaccion

data class TransactionsResponse (
    @SerializedName("transactionsList") var listaTransacciones:ArrayList<Transaccion>
)