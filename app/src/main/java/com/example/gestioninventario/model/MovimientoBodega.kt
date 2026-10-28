package com.example.gestioninventario.model

import java.math.BigDecimal
import java.time.LocalDateTime
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "MovimientoBodega")
data class MovimientoBodega(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val idHerramienta: Int? = null,   // exactamente uno de los dos no nulo
    val idMaterial: Int? = null,
    val cantidad: BigDecimal,
    val fecha: LocalDateTime = LocalDateTime.now(),
    val idUsuario: Int,
    val idObra: Int? = null
)
