package com.example.appfotos.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
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

    suspend fun deleteImage(fileName: String){
        val fileRef = getStorageReference().child(fileName)
        val deleteTask = fileRef.delete()
        deleteTask.await()
    }

    suspend fun uploadFile(fileName: String, filePath: Uri){
        val fileRef = getStorageReference().child(fileName)
        val uploadTask = fileRef.putFile(filePath)
        uploadTask.await()
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