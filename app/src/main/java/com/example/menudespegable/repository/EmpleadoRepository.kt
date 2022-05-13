package com.example.menudespegable.repository

import com.example.menudespegable.entity.Empleado
import com.example.menudespegable.service.EmpleadoService
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmpleadoRepository {
    val empleados: HashMap<String, Empleado> = HashMap()
    val empleadoService: EmpleadoService

    companion object{
        @JvmStatic
        val instance by lazy {
            EmpleadoRepository().apply {  }
        }
    }

    constructor(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://etiquicia.click/restAPI/api.php/records/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        empleadoService = retrofit.create(EmpleadoService::class.java)
    }

    fun save(empleado: Empleado) {
        empleadoService.saveEmpleado(empleado).execute()
    }

    fun edit(empleado: Empleado) {
        var hola: Response<String>? = empleadoService.editEmpleado(empleado.idEmpleado, empleado).execute()
        println(hola)
    }

    fun delete(id: Int) {
        empleadoService.deleteEmpleado(id).execute()
    }

    fun datos():List<Empleado> {
        return empleadoService.getEmpleados().execute().body()!!.records
    }
}