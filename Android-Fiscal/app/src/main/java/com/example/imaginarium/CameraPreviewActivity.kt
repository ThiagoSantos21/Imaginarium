package com.example.imaginarium

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.imaginarium.databinding.ActivityCameraPreviewBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = intent.getIntExtra("Request",0)
        cameraProvider = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        startCamera()

        binding.btFoto.setOnClickListener(){
            val uri= takePhoto()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                blinkPreview()
            }
            val intent = Intent(this,IrregularityActivity::class.java)
            intent.putExtra("uri",uri)
            intent.putExtra("Request",request)
            startActivity(intent)

        }
    }

    private fun startCamera(){

        cameraProvider.addListener({

            imageCapture = ImageCapture.Builder().build()

            val camProvider = cameraProvider.get()
            val preview = Preview.Builder().build().also{
                it.setSurfaceProvider(binding.camPreview.surfaceProvider)
            }
            try {
                camProvider.unbindAll()
                camProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            }catch (e: Exception){
                Log.e("CameraPreview","Falha ao abrir")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() : Uri{

        val fileName =  "JPEG_${System.currentTimeMillis()}"
        val file = File(externalMediaDirs[0], fileName)

        imageCapture?.let{


            val outputFile = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFile,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i("CameraPreview","A imagem foi salva no diretório ${file.toUri()}")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(binding.root.context,"Erro ao salvar foto",Toast.LENGTH_LONG).show()
                        Log.e("CameraPreview","Exceção ao gravar arquivo da foto: $exception")
                    }
                })

            }

        return file.toUri()

        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun blinkPreview(){
            binding.root.postDelayed({
                binding.root.foreground = ColorDrawable(Color.WHITE)
                binding.root.postDelayed({
                    binding.root.foreground = null
                },50)
            },100)
        }

    }
