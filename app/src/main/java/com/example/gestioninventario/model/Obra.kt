package com.example.gestioninventario.model

import com.example.gestioninventario.model.estado.EstadoObra
import com.example.gestioninventario.model.estado.PrioridadObra
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Obra")
data class Obra(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val ubicacion: String,
    val prioridad: PrioridadObra = PrioridadObra.Media,
    val estado: EstadoObra = EstadoObra.EnProceso
)
