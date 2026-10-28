package com.example.gestioninventario.model
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,        // se almacena cifrada (BCrypt)
    val rol: String
)
