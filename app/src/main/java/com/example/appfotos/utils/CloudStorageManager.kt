package com.example.appfotos.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class CloudStorageManager (context: Context){
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val authManager = AuthManager(context)
    private val userId = authManager.getCurrentUser()?.uid

    fun getStorageReference(): StorageReference {
        return storageRef.child("photos").child(userId ?: "")
        //return storageRef.child("photos")
    }

    fun downloadImage(context: Context, imageUrl: String, fileName: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle(fileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        downloadManager.enqueue(request)
    }

    suspend fun deleteImage(urlImage: String, idUsuario: String, idImage: String){
        val fileRef = storage.getReferenceFromUrl(urlImage)
        //val fileRef = getStorageReference().child(fileName)
        val deleteTask = fileRef.delete()
        deleteTask.await()

        //eliminamos de la base de datos tambien
        val dataReference = FirebaseDatabase.getInstance().getReference()
        val deleteTaskDB = dataReference.child("Recuerdo").child(idUsuario).child(idImage).removeValue()
        deleteTaskDB.await()
    }

    suspend fun uploadFile(fileName: String, filePath: Uri, id:String,idUsuario:String){
        val fileRef = getStorageReference().child(fileName)
        val uploadTask = fileRef.putFile(filePath)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val database :DatabaseReference = FirebaseDatabase.getInstance().getReference()
                database.child("Recuerdo").child(idUsuario).child(id).child("url").setValue(downloadUri.toString())
            } else {
                // Handle failures
                // ...
            }
        }
    }

    suspend fun getUserImages(): List<Pair<String,String>> {
        val imageInfo = mutableListOf<Pair<String, String>>()
        val listResult: ListResult = getStorageReference().listAll().await()
        for (item in listResult.items){
            val url = item.downloadUrl.await().toString()
            val name = item.name
            imageInfo.add(Pair(url,name))
        }
        return imageInfo
    }


}