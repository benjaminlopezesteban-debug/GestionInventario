package com.example.gestioninventario.model

import com.example.gestioninventario.model.estado.EstadoHerramienta
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Herramienta")
data class Herramienta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val codigoQr: String = "",
    val estado: EstadoHerramienta = EstadoHerramienta.Disponible,
    val idRack: Int? = null,   // ubicación si está en bodega
    val idObra: Int? = null,   // ubicación si está prestada (solo uno de los dos)
    val imagenUri: String? = null // Uri opcional de la foto seleccionada desde la galería
)
