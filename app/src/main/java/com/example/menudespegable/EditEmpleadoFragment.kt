package com.example.menudespegable

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.example.menudespegable.entity.Empleado
import com.example.menudespegable.repository.EmpleadoRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.security.MessageDigest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PICK_IMAGE = 100;

/**
 * A simple [Fragment] subclass.
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var img_avatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)

        val botonActualizar = view.findViewById<Button>(R.id.actualizar)
        val botonEliminar = view.findViewById<Button>(R.id.eliminar)

        val text = view.findViewById<TextView>(R.id.empleado)
        text.setText(empleado?.nombre)

        val text2 = view.findViewById<TextView>(R.id.departamento)
        text2.setText(empleado?.departamento)

        val text3 = view.findViewById<TextView>(R.id.puesto)
        text3.setText(empleado?.puesto)

        val text4 = view.findViewById<TextView>(R.id.id)
        text4.setText(empleado?.identificacion)

        img_avatar = view.findViewById(R.id.empleado_imagen)

        if(empleado?.avatar != "") {
            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }

        botonActualizar.setOnClickListener{
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Desea modificar el registro?")
                .setCancelable(false)
                .setPositiveButton("Si") {
                    dialog, id ->
                        /*val empleado2: Empleado = Empleado(
                            text4.text.toString(),
                            text.text.toString(),
                            text2.text.toString(),
                            text3.text.toString(),
                            1)*/

                    empleado?.identificacion = text4.text.toString()
                        empleado?.nombre = text.text.toString()
                        empleado?.puesto = text3.text.toString()
                        empleado?.departamento = text2.text.toString()
                        empleado?.avatar = encodeImage(img_avatar.drawable.toBitmap())!!

                        empleado?.let { it1 -> EmpleadoRepository.instance.edit(it1) }

                    val fragmento: Fragment = CamaraFragment.newInstance("")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
                .setNegativeButton("No") {
                    dialog, id ->
                    val fragmento: Fragment = CamaraFragment.newInstance("")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
            builder.create()
            builder.show()
        }

        botonEliminar.setOnClickListener{
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Desea eliminar el registro?")
                .setCancelable(false)
                .setPositiveButton("Si") {
                        dialog, id ->
                    empleado?.idEmpleado?.let { it1 -> EmpleadoRepository.instance.delete(it1) }

                    val fragmento: Fragment = CamaraFragment.newInstance("")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
                .setNegativeButton("No") {
                        dialog, id ->
                    val fragmento: Fragment = CamaraFragment.newInstance("")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
            builder.create()
            builder.show()
        }

        img_avatar.setOnClickListener {
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            var imageUri = data?.data

            Picasso.get()
                .load(imageUri)
                .resize(120, 120)
                .centerCrop()
                .into(img_avatar)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun decodeImage (b64 : String): Bitmap{
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empleado Parameter 1.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}