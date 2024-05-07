package Modelo

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager

class Conexion {

    fun cadenaConexion():Connection?{

        //entrar a cmd y poner ipconfig para obtener ip que se pone despues de la @, 1521 es el puerto, si no funciona es el 1522
        try {
            val ip = "jdbc:oracle:thin:@10.10.0.79:1521:xe"
            val usuario = "system"
            //la contraseña que se puso en oracle
            val contrasena = "desarrollo"

            val connection = DriverManager.getConnection(ip, usuario, contrasena)
            return connection
        }
        catch (e:Exception){
            println("Este es el error: $e:")
            return null
        }
    }

}