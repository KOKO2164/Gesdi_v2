package com.koko.gesdi_v2.servicio

import com.google.gson.annotations.SerializedName
import com.koko.gesdi_v2.entidad.Meta

data class GoalsResponse (
    @SerializedName("goalsList") var listaMetas:ArrayList<Meta>
)