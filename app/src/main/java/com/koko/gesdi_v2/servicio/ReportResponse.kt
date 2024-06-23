package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.Reporte

data class ReportResponse (
    @SerializedName("transactionsList") var listaTransacciones: ArrayList<Reporte>,
    @SerializedName("message") var mensaje: String
)