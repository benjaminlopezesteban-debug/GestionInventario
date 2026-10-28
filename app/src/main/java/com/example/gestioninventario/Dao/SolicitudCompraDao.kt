package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Material
import com.example.gestioninventario.model.SolicitudCompra
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudCompraDao {

    @Insert
    suspend fun agregarSolicitudCompra(solicitudCompra: SolicitudCompra): Long

    @Update
    suspend fun actualizarSolicitudCompra(solicitudCompra: SolicitudCompra)

    @Delete
    suspend fun eliminarSolicitudCompra(solicitudCompra: SolicitudCompra)

    @Query("SELECT * FROM SolicitudCompra ORDER BY id")
    fun listarSolicitudes(): Flow<List<SolicitudCompra>>

    @Query("SELECT * FROM SolicitudCompra WHERE estado = :estado LIMIT 1")
    fun listarPorEstado(estado: String): Flow<List<SolicitudCompra>>
}