package com.koko.gesdi_v2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.entidad.Meta
import java.text.SimpleDateFormat
import java.util.Locale

class AdaptadorMetas:RecyclerView.Adapter<AdaptadorMetas.MiViewHolder>() {
    private var listaMetas:List<Meta> = ArrayList()
    private lateinit var context: Context

    fun setListaMetas(metas:ArrayList<Meta>) {
        this.listaMetas = metas
        notifyDataSetChanged()
    }

    fun agregarDatos(items: ArrayList<Meta>){
        this.listaMetas = items
    }

    fun contexto(context: Context){
        this.context = context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaNombreMeta = view.findViewById<TextView>(R.id.filaNombreMeta)
        private var filaMontoActualMeta = view.findViewById<TextView>(R.id.filaMontoActualMeta)
        private var filaMontoObjetivoMeta = view.findViewById<TextView>(R.id.filaMontoObjetivoMeta)
        private var filaFechaLimiteMeta = view.findViewById<TextView>(R.id.filaFechaLimiteMeta)

        fun formatearFecha(fechaOriginal: String): String {
            val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val formatoNuevo = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val fecha = formatoOriginal.parse(fechaOriginal)
            return formatoNuevo.format(fecha)
        }

        fun formatearMonto(monto: Double): String {
            return String.format(Locale.US, "S/ %.2f", monto)
        }

        fun setValores(meta: Meta) {
            filaNombreMeta.text = meta.goal_name
            filaMontoActualMeta.text = formatearMonto(meta.goal_actual_amount)
            filaMontoObjetivoMeta.text = formatearMonto(meta.goal_target_amount)
            filaFechaLimiteMeta.text = formatearFecha(meta.goal_deadline)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fila_meta, parent, false)
    )

    override fun onBindViewHolder(holder: AdaptadorMetas.MiViewHolder, position: Int) {
        val meta = listaMetas[position]
        holder.setValores(meta)
    }

    override fun getItemCount(): Int {
        return listaMetas.size
    }
}