package com.example.gestioninventario.model

import java.time.LocalDateTime
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Notificacion")
data class Notificacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idUsuario: Int,
    val mensaje: String,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val leida: Boolean = false
)
