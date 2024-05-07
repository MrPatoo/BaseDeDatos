package rodrigo.cordova.crudrodrigoc2b

import Modelo.Conexion
import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dataClassMascotas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mandar a llamar a todos los elementos
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPeso = findViewById<EditText>(R.id.txtPeso)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rcvMascotas = findViewById<RecyclerView>(R.id.rcvMascotas)

        //asignar datos
        //asignarle un layout a rcv

        rcvMascotas.layoutManager = LinearLayoutManager(this)

        //////////////TODO: mostrar datos//////////////////
        //funcion para mostrar los datos
        fun obtenerDatos(): List<dataClassMascotas>{
            //creo obj de clase conexion
            val objConexion = Conexion().cadenaConexion()

            //creo statement (trae valores)
            val statement = objConexion?.createStatement()
            val resultSet =statement?.executeQuery("select * from tbMascotas") !!

            val mascotas = mutableListOf<dataClassMascotas>()

            //Recorro todos los registros de la base de datos
            while (resultSet.next()){
                val nombre = resultSet.getString("nombreMascota")
                val mascota = dataClassMascotas(nombre)
                mascotas.add(mascota)


            }
            return mascotas
        }

        //Asignar el adaptador al rcv
        CoroutineScope(Dispatchers.IO).launch{
            val mascotasDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(mascotasDB)
                rcvMascotas.adapter = adapter
            }
        }

        //2.Programar el boton de añadir
        btnAgregar.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {

                //1. Creo un objeto de la clase conexion
                val objConexion = Conexion().cadenaConexion()

                //añado una variable que contenga PrepareStatement
                val addMascota = objConexion?.prepareStatement("Insert into tbMascotas values(?, ?, ?)")!!
                addMascota.setString(1,txtNombre.text.toString())
                addMascota.setInt(2, txtPeso.text.toString().toInt())
                addMascota.setInt(3,txtEdad.text.toString().toInt())

                addMascota.executeUpdate()


            }
        }

    }
}