package com.example.appfotos.utils

import android.content.Context
import com.example.appfotos.models.UsuarioModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

sealed class AuthRes<out T>{
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}

class AuthManager (private val context: Context) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    suspend fun createUserWithEmailAndPassword(email: String, password:String, nombre:String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email,password).await()
            //guardamos en la base de datos
            //creamos el objeto de usuaario
            val idUsuario = authResult.user?.uid ?: ""
            val usuario = UsuarioModel(idUsuario,nombre)
            val database: DatabaseReference = FirebaseDatabase.getInstance().getReference()
            database.child("Usuario").child(idUsuario).setValue(usuario.toMap())
            AuthRes.Success(authResult.user)
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error al crear el usuario")
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password:String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email,password).await()
            AuthRes.Success(authResult.user)
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception){
            AuthRes.Error(e.message ?: "Error al restablecer la contraseña")
        }
    }

    fun signOut(){
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }

}