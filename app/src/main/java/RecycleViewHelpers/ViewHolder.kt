package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rodrigo.cordova.crudrodrigoc2b.R

class
ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txtMascotaCard)
    val btnEditar: ImageView = view.findViewById(R.id.imgEditar)
    val btnEliminar: ImageView = view.findViewById(R.id.imgEliminar)
}
