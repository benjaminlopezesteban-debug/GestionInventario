package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Material
import com.example.gestioninventario.model.SolicitudCompra
import com.example.gestioninventario.model.SolicitudMaterial
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudMaterialDao {
    @Insert
    suspend fun agregarSolicitudMaterial(solicitudMaterial: SolicitudMaterial): Long

    @Update
    suspend fun actualizarSolicitudMaterial(solicitudMaterial: SolicitudMaterial)

    @Delete
    suspend fun eliminarSolicitudMaterial(solicitudMaterial: SolicitudMaterial)

    @Query("SELECT * FROM SolicitudMaterial ORDER BY id")
    fun listarSolicitudesMateriales(): Flow<List<SolicitudMaterial>>

    @Query("SELECT * FROM SolicitudMaterial WHERE estado = :estado LIMIT 1")
    fun listarPorEstado(estado: String): Flow<List<SolicitudMaterial>>
}
