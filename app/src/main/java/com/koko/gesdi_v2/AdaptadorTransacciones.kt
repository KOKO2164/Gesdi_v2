package com.koko.gesdi_v2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.entidad.Categoria
import com.koko.gesdi_v2.entidad.Presupuesto
import com.koko.gesdi_v2.entidad.Transaccion
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class AdaptadorTransacciones:RecyclerView.Adapter<AdaptadorTransacciones.MiViewHolder>() {
    private var listaTransacciones:ArrayList<Transaccion> = ArrayList()
    private lateinit var context: Context

    fun setListaTransacciones(transacciones:ArrayList<Transaccion>) {
        this.listaTransacciones = transacciones
        notifyDataSetChanged()
    }

    fun agregarDatos(items: ArrayList<Transaccion>){
        this.listaTransacciones = items
    }

    fun contexto(context: Context){
        this.context = context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaDescripcionTransaccion = view.findViewById<TextView>(R.id.filaDescripcionTransaccion)
        private var filaMontoTransaccion = view.findViewById<TextView>(R.id.filaMontoTransaccion)
        private var filaFechaTransaccion = view.findViewById<TextView>(R.id.filaFechaTransaccion)

        fun formatearFecha(fechaOriginal: String): String {
            val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val formatoNuevo = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val fecha = formatoOriginal.parse(fechaOriginal)
            return formatoNuevo.format(fecha)
        }

        fun formatearMonto(monto: Double): String {
            return String.format(Locale.US, "S/ %.2f", monto)
        }

        fun setValores(transaccion: Transaccion) {
            filaDescripcionTransaccion.text = transaccion.transaction_description
            filaFechaTransaccion.text = formatearFecha(transaccion.transaction_date)
            filaMontoTransaccion.text = formatearMonto(transaccion.transaction_amount)

            if (transaccion.type_id == 1) {
                filaMontoTransaccion.setTextColor(Color.GREEN)
            } else {
                filaMontoTransaccion.setTextColor(Color.RED)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fila_transaccion, parent, false)
    )

    override fun onBindViewHolder(holder: AdaptadorTransacciones.MiViewHolder, position: Int) {
        val transaccion = listaTransacciones[position]
        holder.setValores(transaccion)
    }

    override fun getItemCount(): Int {
        return listaTransacciones.size
    }
}