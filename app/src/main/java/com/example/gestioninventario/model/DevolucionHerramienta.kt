package com.example.gestioninventario.model

import java.time.LocalDateTime
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "DevolucionHerramienta")
data class DevolucionHerramienta(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idSolicitudHerramienta: Int,
    val idHerramienta: Int,
    val fechaEntrega: LocalDateTime = LocalDateTime.now(),
    val fechaDevolucion: LocalDateTime? = null,   // null = aún no devuelta
    val idUsuarioEntrega: Int,
    val idUsuarioDevolucion: Int? = null
)
