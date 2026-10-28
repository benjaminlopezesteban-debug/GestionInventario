package com.example.gestioninventario.model

import com.example.gestioninventario.model.estado.EstadoSolicitudCompra
import java.time.LocalDateTime
import java.math.BigDecimal
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "SolicitudCompra")
data class SolicitudCompra(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipoRecurso: String,
    val nombreRecurso: String,
    val cantidad: BigDecimal,
    val idSolicitante: Int,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val estado: EstadoSolicitudCompra = EstadoSolicitudCompra.CompraPendiente,
    val idAprueba: Int? = null,
    val mensajeRespuesta: String? = null,
    val fechaRespuesta: LocalDateTime? = null
)
