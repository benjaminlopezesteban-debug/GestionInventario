package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.DevolucionHerramienta
import com.example.gestioninventario.model.Herramienta
import kotlinx.coroutines.flow.Flow

@Dao
interface DevolucionHerramientaDao {

    @Insert
    suspend fun agregarDevolucionHerramienta(devolucionHerramienta: DevolucionHerramienta)

    @Update
    suspend fun actualizarDevolucionHerramienta(devolucionHerramienta: DevolucionHerramienta)

    @Delete
    suspend fun eliminarDevolucionHerramienta(devolucionHerramienta: DevolucionHerramienta)

    @Query("SELECT * FROM DevolucionHerramienta ORDER BY id")
    fun listarDevolucionHerramientas(): Flow<List<DevolucionHerramienta>>

    @Query("""
        SELECT h.* FROM Herramienta h
        INNER JOIN DevolucionHerramienta d ON d.idHerramienta = h.id
        INNER JOIN SolicitudHerramienta s ON s.id = d.idSolicitudHerramienta
        WHERE s.idMaestroObra = :idUsuario AND d.fechaDevolucion IS NULL
    """)
    fun listarHerramientasACargo(idUsuario: Int): Flow<List<Herramienta>>

    @Query("SELECT * FROM DevolucionHerramienta WHERE idSolicitudHerramienta = :idSolicitud LIMIT 1")
    suspend fun obtenerPorSolicitud(idSolicitud: Int): DevolucionHerramienta?
}
