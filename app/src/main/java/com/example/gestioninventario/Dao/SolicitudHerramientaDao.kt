package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.SolicitudHerramienta
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudHerramientaDao {

    @Insert
    suspend fun agregarSolicitudHerramienta(solicitudHerramienta: SolicitudHerramienta): Long

    @Update
    suspend fun actualizarSolicitudHerramienta(solicitudHerramienta: SolicitudHerramienta)

    @Delete
    suspend fun eliminarSolicitudHerramienta(solicitudHerramienta: SolicitudHerramienta)

    @Query("SELECT * FROM SolicitudHerramienta WHERE estado = :estado ORDER BY id DESC")
    fun listarPorEstado(estado: String): Flow<List<SolicitudHerramienta>>

    @Query("SELECT * FROM SolicitudHerramienta ORDER BY id DESC")
    fun listarSolicitudes(): Flow<List<SolicitudHerramienta>>

    @Query("SELECT * FROM SolicitudHerramienta WHERE idMaestroObra = :idUsuario ORDER BY id DESC")
    fun listarPorUsuario(idUsuario: Int): Flow<List<SolicitudHerramienta>>

    @Query("SELECT * FROM SolicitudHerramienta WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): SolicitudHerramienta?
}
