package RecyclerViewHelper

import Modelo.Conexion
import Modelo.dataClassMascotas
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rodrigo.cordova.crudrodrigoc2b.R


class Adaptador(private var Datos: List<dataClassMascotas>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista:List< dataClassMascotas>){
        Datos = nuevaLista
        notifyDataSetChanged()//Esto notifica al recyclerview que hay datos nuevos
    }

    ///////////////////////////TODO: ELIMINAR DATOS///////////////////////////////

    fun eliminarDatos(nombreMascotas: String, position: Int){

        //actualizar lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)


        GlobalScope.launch (Dispatchers.IO){
            //1- crear un objeto de la clase conexion
            val objConexion = Conexion().cadenaConexion()

            //2- crear una variable que contenga un PrepareStatement
            val deleteMascota = objConexion?.prepareStatement("delete from tbMascotas where nombreMascota = ?")!!
            deleteMascota.setString(1, nombreMascotas)
            deleteMascota.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

        }
        Datos = listaDatos.toList()
        //notifico al adaptador sobre los cambios
        notifyItemRemoved(position)
        notifyDataSetChanged()



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)

        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mascota = Datos[position]
        holder.textView.text = mascota.nombreMascotas

        //todo click en el icono de eliminar
        holder.btnEliminar.setOnClickListener{

            //creamos Alert
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar la mascota?")

            //botones
            builder.setPositiveButton("si"){dialog, switch -> eliminarDatos(mascota.nombreMascotas, position)

            }
            builder.setNegativeButton("no"){dialog, which -> dialog.dismiss()

            }

            val dialog = builder.create()
            dialog.show()





        }


    }




}
