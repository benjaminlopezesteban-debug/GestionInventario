package com.example.gestioninventario.model

import com.example.gestioninventario.model.estado.EstadoSolicitudMaterial
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "SolicitudMaterial")
data class SolicitudMaterial(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idJefeObra: Int,
    val idObra: Int,
    val idMaterial: Int,
    val cantidad: BigDecimal,
    val fechaSolicitud: LocalDateTime = LocalDateTime.now(),
    val fechaRequerida: LocalDate,
    val fechaEntrega: LocalDateTime? = null,
    val estado: EstadoSolicitudMaterial = EstadoSolicitudMaterial.Pendiente,
    val motivoRechazo: String? = null
)
