package com.example.menudespegable

import android.app.Activity
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
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PICK_IMAGE = 100;

/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img_avatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_add_empleado, container, false)

        val botonGuardar = view.findViewById<Button>(R.id.guardar)

        val text = view.findViewById<TextView>(R.id.nombre2)

        val text2 = view.findViewById<TextView>(R.id.departamento2)

        val text3 = view.findViewById<TextView>(R.id.puesto2)

        val text4 = view.findViewById<TextView>(R.id.id2)

        img_avatar = view.findViewById(R.id.empleado_imagen2)

        botonGuardar.setOnClickListener{
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Desea guardar el empleado?")
                .setCancelable(false)
                .setPositiveButton("Si") {
                        dialog, id ->

                    var empleado: Empleado = Empleado(
                        0,
                        "",
                        "",
                        "",
                          "",
                        "")


                    empleado?.identificacion = text4.text.toString()
                    empleado?.nombre = text.text.toString()
                    empleado?.puesto = text3.text.toString()
                    empleado?.departamento = text2.text.toString()
                    empleado?.avatar = encodeImage(img_avatar.drawable.toBitmap()).toString()

                    empleado?.let { it1 -> EmpleadoRepository.instance.save(it1) }

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
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}