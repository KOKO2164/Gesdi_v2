package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.Presupuesto

data class BudgetsResponse (
    @SerializedName("budgetsList") var listaPresupuestos:ArrayList<Presupuesto>,
    @SerializedName("message") var mensaje:String
)