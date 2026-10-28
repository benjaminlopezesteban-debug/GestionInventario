package com.example.gestioninventario.Dao

import androidx.room3.*
import com.example.gestioninventario.model.Herramienta
import kotlinx.coroutines.flow.Flow

@Dao
interface HerramientaDao {

    @Insert
    suspend fun agregarHerramienta(herramienta: Herramienta)

    @Update
    suspend fun actualizarHerramienta(herramienta: Herramienta)

    @Delete
    suspend fun eliminarHerramienta(herramienta: Herramienta)

    @Query("SELECT * FROM Herramienta ORDER BY id")
    fun listarHerramienta(): Flow<List<Herramienta>>

    @Query("SELECT * FROM Herramienta WHERE UPPER(estado) = 'DISPONIBLE' ORDER BY id")
    fun listarHerramientasDisponibles(): Flow<List<Herramienta>>

    @Query("SELECT COUNT(id) FROM Herramienta WHERE tipo = :tipo AND UPPER(estado) = 'DISPONIBLE'")
    suspend fun obtenerHerramientasDisponibles(tipo: String): Int

    @Query("SELECT * FROM Herramienta WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Herramienta?

    @Query("SELECT MAX(id) FROM Herramienta")
    suspend fun obtenerMaxId(): Int?
}
