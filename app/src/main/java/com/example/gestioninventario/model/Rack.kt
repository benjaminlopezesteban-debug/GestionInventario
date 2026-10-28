package com.example.gestioninventario.model
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Rack")
data class Rack(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val descripcion: String? = null,
    val estadoOcupado: Boolean = false
)
