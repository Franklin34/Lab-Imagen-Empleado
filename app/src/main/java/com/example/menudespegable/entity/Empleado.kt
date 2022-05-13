package com.example.menudespegable.entity

import android.widget.TextView
import java.io.Serializable

class Empleado(
    var idEmpleado: Int,
    var identificacion: String?, var nombre: String?, var departamento: String?, var puesto: String?, var avatar: String) : Serializable {

}