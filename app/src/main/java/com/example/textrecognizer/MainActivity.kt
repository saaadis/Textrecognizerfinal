package com.example.textrecognizer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlinx.android.synthetic.main.activity_main.imageview as imageview1


class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 1
    private val REQUEST_CODE = 100
    // private val REQUEST_GALLERY_CAMERA = 54654
    //internal var imagePath: String? = ""
    private lateinit var imageBitmap: Bitmap
    lateinit var editText: EditText
    lateinit var text_Button: ImageButton
   // var imageUri = ""
    private val PERMISSION_REQUEST_CODE: Int = 101

    private var mCurrentPhotoPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.textView)
        text_Button = findViewById(R.id.textButton)

        editText.visibility = View.INVISIBLE
        CButton.setOnClickListener(View.OnClickListener {
            if (checkPersmission()) takePicture() else requestPermission()

        })
    }

    //check permissions
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ),
            PERMISSION_REQUEST_CODE)
    }

    fun takePicture() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,packageName+".fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
    //to set captured/selected image on imageView
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {

                    val extras = data?.getExtras()
                    imageBitmap = extras?.get("data") as Bitmap
                    imageview1.setImageBitmap(imageBitmap)


                }
            }
            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imageview1.setImageBitmap(imageBitmap)
            }

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                imageview1.setImageURI(data?.data) // handle chosen image

                imageBitmap = (imageview1.drawable as BitmapDrawable).bitmap


            }
        }
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            //To get the File for further usage
            val auxFile = File(mCurrentPhotoPath)

            imageBitmap=BitmapFactory.decodeFile(mCurrentPhotoPath)
            //val imageBitmap :Bitmap= BitmapFactory.decodeFile(mCurrentPhotoPath)
            imageview1.setImageBitmap(imageBitmap)

        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageview1.setImageURI(data?.data) // handle chosen image

            imageBitmap = (imageview1.drawable as BitmapDrawable).bitmap


        }

    }

    fun openGalleryForImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)}








    fun DetectTextFromImage(view: View) {
        editText.visibility = View.VISIBLE
        var text: String = ""
        var image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap)
        var detector: FirebaseVisionTextDetector = FirebaseVision.getInstance()
            .visionTextDetector
        var result: Task<FirebaseVisionText> = detector.detectInImage(image)
            .addOnSuccessListener(object : OnSuccessListener<FirebaseVisionText> {
                override fun onSuccess(p0: FirebaseVisionText?) {
                    //var boundingBox: Rect =block.boundingBox!!
                    //var cornerPoints:Array<Point> =block.cornerPoints!!
                    for (block: FirebaseVisionText.Block in p0!!.blocks) text += block.text
                    editText.setText(text)
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(this@MainActivity, "No Text", Toast.LENGTH_LONG).show()
                }
            })
    }
}







