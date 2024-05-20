package RecyclerViewHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dataClassMascotas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.cordova.crudrodrigoc2b.R


class Adaptador(private var Datos: List<dataClassMascotas>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista:List< dataClassMascotas>){
        Datos = nuevaLista
        notifyDataSetChanged()//Esto notifica al recyclerview que hay datos nuevos
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)

        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreMascota
    }

    


}
