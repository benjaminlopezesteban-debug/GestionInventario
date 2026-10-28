package com.example.gestioninventario.model

import com.example.gestioninventario.model.estado.EstadoSolicitudHerramienta
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "SolicitudHerramienta")
data class SolicitudHerramienta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idMaestroObra: Int,
    val idObra: Int = 1,
    val idHerramienta: Int? = null,
    val nombreHerramienta: String = "",
    val tipoHerramienta: String,
    val cantidad: Int = 1,
    val fechaSolicitud: LocalDateTime = LocalDateTime.now(),
    val fechaRequerida: LocalDate,
    val fechaDevolucionEstimada: LocalDate,
    val estado: EstadoSolicitudHerramienta = EstadoSolicitudHerramienta.Pendiente,
    val motivoRechazo: String? = null
)
