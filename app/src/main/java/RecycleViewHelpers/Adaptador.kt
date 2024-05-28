package RecyclerViewHelper

import Modelo.Conexion
import Modelo.dataClassMascotas
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.cordova.crudrodrigoc2b.R
import rodrigo.cordova.crudrodrigoc2b.activity_Detalle_Mascota
import java.util.UUID


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

        //TODO: editar datos////////////

        fun actualizarLista(nuevaLista: List<dataClassMascotas>){
            Datos = nuevaLista
            notifyDataSetChanged() //notifica al adaptador sobre los cambios
        }

        fun actualicePantalla(uuid: String, nuevoNombre: String){
            val index = Datos.indexOfFirst{it.uuid == uuid}
            Datos[index].nombreMascotas = nuevoNombre
            notifyDataSetChanged()
        }

        fun actualizarDatos(nuevoNombre: String, uuid: String){
            GlobalScope.launch(Dispatchers.IO){

                //1- crear un objeto de la clase conexion
                val objConexion = Conexion().cadenaConexion()

                //2- creo una variable con un prepareStatement
                val updateMascota = objConexion?.prepareStatement("update tbMascotas set nombreMascota = ? where uuid = ?")!!
                updateMascota.setString(1, nuevoNombre)
                updateMascota.setString(2, uuid)

                updateMascota.executeUpdate()

                withContext(Dispatchers.Main){
                    actualicePantalla(uuid, nuevoNombre)
                }
            }
        }

        holder.btnEditar.setOnClickListener{


                //creamos Alert
                val context = holder.itemView.context

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Actualizar")
                builder.setMessage("¿Desea Actualizar la mascota?")

                    val cuadroTexto = EditText(context)
                    cuadroTexto.setHint(mascota.nombreMascotas)
                    builder.setView(cuadroTexto)

                //botones
                builder.setPositiveButton("actualizar"){dialog, switch -> actualizarDatos(cuadroTexto.text.toString(), mascota.uuid)

                }
                builder.setNegativeButton("cancelar"){dialog, which -> dialog.dismiss()

                }

                val dialog = builder.create()
                dialog.show()
        }

    //todo: Click a la card completa

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context

            val pantallaDetalle = Intent(context, activity_Detalle_Mascota::class.java)
            pantallaDetalle.putExtra("MascotaUUID", mascota.uuid)
            pantallaDetalle.putExtra("nombre", mascota.nombreMascotas)
            pantallaDetalle.putExtra("peso", mascota.peso)
            pantallaDetalle.putExtra("edad", mascota.edad)

            context.startActivity(pantallaDetalle)
        }





    }






}
