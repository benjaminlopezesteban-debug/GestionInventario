package com.example.gestioninventario.model

import java.time.LocalDateTime
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "HistorialEstado")
data class HistorialEstado(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entidadTipo: String,
    val entidadId: Int,
    val estadoAnterior: String? = null,
    val estadoNuevo: String,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val idUsuario: Int,
    val observacion: String? = null
)
