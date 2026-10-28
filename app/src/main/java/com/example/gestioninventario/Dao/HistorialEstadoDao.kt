package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.HistorialEstado
import kotlinx.coroutines.flow.Flow


@Dao
interface HistorialEstadoDao {

    @Insert
    suspend fun agregarHistorialEstado(historialEstado: HistorialEstado)

    @Update
    suspend fun actualizarHistorialEstado(historialEstado: HistorialEstado)

    @Delete
    suspend fun eliminarHistorialEstado(historialEstado: HistorialEstado)

    @Query("SELECT * FROM HistorialEstado ORDER BY id")
    fun listarHistorial(): Flow<List<HistorialEstado>>
}