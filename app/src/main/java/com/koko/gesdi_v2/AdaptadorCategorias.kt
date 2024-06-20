package com.koko.gesdi_v2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.koko.gesdi_v2.entidad.Categoria

class AdaptadorCategorias:RecyclerView.Adapter<AdaptadorCategorias.MiViewHolder>() {
    private var listaCategorias:ArrayList<Categoria> = ArrayList()
    private lateinit var context: Context

    fun setListaCategorias(categorias: ArrayList<Categoria>){
        this.listaCategorias = categorias
        notifyDataSetChanged()
    }

    fun agregarDatos(items: ArrayList<Categoria>){
        this.listaCategorias = items
    }

    fun contexto(context: Context){
        this.context = context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaNombreCategoria = view.findViewById<TextView>(R.id.filaNombreCategoria)
        private var filaTipoCategoria = view.findViewById<TextView>(R.id.filaTipoCategoria)

        fun setValores(categoria: Categoria) {
            filaNombreCategoria.text = categoria.category_name
            filaTipoCategoria.text = categoria.type_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fila_categoria, parent, false)
    )

    override fun onBindViewHolder(holder: AdaptadorCategorias.MiViewHolder, position: Int) {
        val categoriaItem = listaCategorias[position]
        holder.setValores(categoriaItem)
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }
}