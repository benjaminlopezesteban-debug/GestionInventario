package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.MovimientoBodega
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoBodegaDao {
    @Insert
    suspend fun agregarMovimiento(movimientoBodega: MovimientoBodega)

    @Update
    suspend  fun actualizarMovimiento(movimientoBodega: MovimientoBodega)

    @Delete
    suspend fun eliminarMovimiento(movimientoBodega: MovimientoBodega)

    @Query("SELECT * FROM MovimientoBodega WHERE id = :id")
    suspend fun obtenerPorId(id: Int): MovimientoBodega?

    @Query("SELECT * FROM MovimientoBodega ORDER BY id")
    fun listarMovimientos(): Flow<List<MovimientoBodega>>
}