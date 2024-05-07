package rodrigo.cordova.crudrodrigoc2b

import Modelo.Conexion
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        //2.Programar el boton de agregar
        btnAgregar.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {

                //1. Creo un objeto de la clase conexion
                val objConexion = Conexion().cadenaConexion()

                //añado una variable que contenga PrepareStatement
                val addMascota = objConexion?.prepareStatement("Insert into tbMascotas (?, ?, ?)")!!
                addMascota.setString(1,txtNombre.text.toString())
                addMascota.setInt(1, txtPeso.text.toString().toInt())
                addMascota.setInt(2,txtEdad.text.toString().toInt())

                addMascota.executeUpdate()

                Toast.makeText(this@MainActivity, "Mascota registrada", Toast.LENGTH_LONG).show()
            }
        }

    }
}