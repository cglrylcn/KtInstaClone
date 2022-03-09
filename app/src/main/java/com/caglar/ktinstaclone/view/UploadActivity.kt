package com.caglar.ktinstaclone.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.caglar.ktinstaclone.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture : Uri?= null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLauncher()
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    fun upload(view: View) {
        val reference = storage.reference
        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"
        val imageReference = reference.child("images/").child(imageName)

        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture!!)
                .addOnSuccessListener {
                    //Save downloadUrl -> fireStore
                    val uploadPictureReference = storage.reference.child("images/").child(imageName)
                    uploadPictureReference.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()
                        if (auth.currentUser != null) {
                            val postMap = hashMapOf<String, Any>()
                            postMap["downloadUrl"] = downloadUrl
                            postMap["userEmail"] = auth.currentUser!!.email!!
                            postMap["comment"] = binding.commentText.text.toString()
                            postMap["date"] = Timestamp.now()

                            firestore.collection("Posts")
                                .add(postMap)
                                .addOnSuccessListener {
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
        else {
            Toast.makeText(this,"Please select an image",Toast.LENGTH_LONG).show()
        }

    }

    fun selectImage(view: View) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view,"Permission needed",Snackbar.LENGTH_INDEFINITE).setAction("Give permission") {
                    //Request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }
            else {
                //Request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else {
            //Intent to gallery
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity result
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    intentFromResult?.let {
                        selectedPicture = it.data
                        selectedPicture?.let { sPic ->
                            binding.imageView.setImageURI(sPic)
                        }
                    }
                }
            })

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //Permission Granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else {
                //Permission Denied
            }
        }
    }
}