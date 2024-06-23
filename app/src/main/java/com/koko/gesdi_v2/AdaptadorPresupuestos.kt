package com.koko.gesdi_v2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.entidad.Presupuesto
import java.util.Locale

class AdaptadorPresupuestos: RecyclerView.Adapter<AdaptadorPresupuestos.MiViewHolder>() {
    private var listaPresupuestos: ArrayList<Presupuesto> = ArrayList()
    private lateinit var context: Context

    fun setListaPresupuestos(presupuestos: ArrayList<Presupuesto>) {
        this.listaPresupuestos = presupuestos
        notifyDataSetChanged()
    }

    fun contexto(context: Context) {
        this.context = context
    }

    class MiViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        private var filaNombrePresupuesto = view.findViewById<TextView>(R.id.filaNombrePres)
        private var filaMontoPresupuesto = view.findViewById<TextView>(R.id.filaMontoPres)
        private var filaCategoriaPresupuesto = view.findViewById<TextView>(R.id.filaCategoriaPres)

        fun formatearMonto(monto: Double): String {
            return String.format(Locale.US, "S/ %.2f", monto)
        }
        fun setValores(presupuesto: Presupuesto) {
            filaNombrePresupuesto.text = presupuesto.budget_name + " - " + presupuesto.budget_month + "/" + presupuesto.budget_year
            filaMontoPresupuesto.text = formatearMonto(presupuesto.budget_amount)
            filaCategoriaPresupuesto.text = presupuesto.category_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fila_presupuesto, parent, false)
    )

    override fun getItemCount(): Int {
        return listaPresupuestos.size
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val presupuesto = listaPresupuestos[position]
        holder.setValores(presupuesto)
    }
}