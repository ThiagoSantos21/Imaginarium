package com.example.imaginarium

import android.app.Activity
import android.content.Intent
import com.google.firebase.storage.ktx.storage
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import java.io.File


class IrregularityActivity : AppCompatActivity() {

    private var FILE_NAME = "photo.jpg"
    private lateinit var ftFrontal: AppCompatImageButton
    private lateinit var ftTraseira: AppCompatImageButton
    private lateinit var ftLDireita: AppCompatImageButton
    private lateinit var ftLEsquerda: AppCompatImageButton
    private lateinit var btnEnviar: AppCompatButton
    private lateinit var photo: File

    private var paths: MutableList<String> = mutableListOf("","","","")
    val storage = Firebase.storage("gs://photos-imaginarium")

    private var fileName = "photo_"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_irregularity)

        ftFrontal = findViewById(R.id.ftFrontal)
        ftTraseira = findViewById(R.id.ftTraseira)
        ftLDireita = findViewById(R.id.ftLDireita)
        ftLEsquerda = findViewById(R.id.ftLEsquerda)
        btnEnviar = findViewById(R.id.btnEnviar)

        ftFrontal.setOnClickListener(){
            getPhoto(1)
        }
        ftTraseira.setOnClickListener(){
            getPhoto(2)
        }
        ftLDireita.setOnClickListener(){
            getPhoto(3)
        }
        ftLEsquerda.setOnClickListener(){
            getPhoto(4)
        }
        btnEnviar.setOnClickListener(){
            if(paths[0].isEmpty()||paths[1].isEmpty()||paths[2].isEmpty()||paths[3].isEmpty())
                Snackbar.make(btnEnviar, "Capture todas as fotos", Snackbar.LENGTH_LONG).show()
            else{

                val storageRef = storage.reference
                for (i in paths) {
                    val delete = File(paths.toString())
                    var file = Uri.fromFile(File("${i}"))
                    val photoRef = storageRef.child("br.com.appzonaazul/${file.lastPathSegment}")
                    photoRef.putFile(file)
                    delete.delete()
                }
                Snackbar.make(btnEnviar, "Irregularidade registrada", Snackbar.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getPhoto(REQUEST_CODE: Int){
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photo = getPhotoFile(FILE_NAME)

        val fileProvider = FileProvider.getUriForFile(this,"com.example.imaginarium.fileprovider",photo)

        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
        if(pictureIntent.resolveActivity(this.packageManager)!=null) {
            startActivityForResult(pictureIntent,REQUEST_CODE)
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storage)
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(photo.absolutePath)
            val img: Drawable = BitmapDrawable(resources,bitmap)
            if(requestCode == 1) {
                ftFrontal.background = img
                ftFrontal.setImageIcon(null)
                paths[0] = photo.absolutePath
                Log.i("ENDERECO",paths[0])
            }
            if(requestCode == 2) {
                ftTraseira.background = img
                ftTraseira.setImageIcon(null)
                paths[1] = photo.absolutePath
                Log.i("ENDERECO",paths[1])
            }
            if(requestCode == 3) {
                ftLDireita.background = img
                ftLDireita.setImageIcon(null)
                paths[2] = photo.absolutePath
                Log.i("ENDERECO",paths[2])
            }
            if(requestCode == 4) {
                ftLEsquerda.background = img
                ftLEsquerda.setImageIcon(null)
                paths[3] = photo.absolutePath
                Log.i("ENDERECO",paths[3])
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


}

