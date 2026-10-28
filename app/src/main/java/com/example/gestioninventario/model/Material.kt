package com.example.gestioninventario.model

import com.example.gestioninventario.model.estado.EstadoMaterial
import java.math.BigDecimal
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Material")
data class Material(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val unidadMedida: String,      // metros, kg, m3, unidades...
    val cantidadDisponible: BigDecimal = BigDecimal.ZERO,
    val codigoQr: String,
    val estado: EstadoMaterial = EstadoMaterial.BuenEstado,
    val idRack: Int? = null
)
