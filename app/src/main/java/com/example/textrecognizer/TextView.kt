package com.example.textrecognizer


import android.app.AlertDialog
import android.content.Context
import android.content.Intent

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.os.Bundle

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.textrecognizer.R.id.save_action
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector
//import kotlinx.android.synthetic.main.activity_save_dialog.*
//import kotlinx.android.synthetic.main.activity_save_dialog.view.*
import kotlinx.android.synthetic.main.activity_text.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception



class TextView : AppCompatActivity() {
    /* lateinit var textview: EditText
    lateinit var fileName:EditText*/
 lateinit var extractedtext: String
 //private  var  FILE_NAME:String = "example.txt";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
     // textView.editableText.clear()
       textExtraction()

    }



    fun textExtraction() {

        extractedtext = intent.getStringExtra("ExtractedText").toString()

        if (extractedtext.isEmpty()) {
            Toast.makeText(this@TextView, "No Text", Toast.LENGTH_LONG).show()
            var intentAgain=Intent(this,MainActivity::class.java)
            startActivity(intentAgain)

        }
        textView.setText(extractedtext)


    }
        fun SaveText(view:View) {


           val file: String = fileName.text.toString()
            val data: String = textView.text.toString()


            val fileOutputStream: FileOutputStream
            try {
                fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
                fileOutputStream.write(data.toByteArray())
                Toast.makeText(this, "Saved to " + getFilesDir() + "/" + file,
                    Toast.LENGTH_LONG).show();
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(applicationContext, "data save", Toast.LENGTH_LONG).show()
            fileName.text.clear()
            textView.text.clear()

        }
    fun TakeAnother(view: View){
        textView.text.clear()
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)

    }
        }





