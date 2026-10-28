package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.Notificacion
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificacionDao {

    @Insert
    suspend fun agregarNotificacion(notificacion: Notificacion)

    @Update
    suspend  fun actualizarNotificacion(notificacion: Notificacion)

    @Delete
    suspend fun eliminarNotificacion(notificacion: Notificacion)

    @Query("SELECT * FROM Notificacion ORDER BY id")
    fun listarNotificaciones(): Flow<List<Notificacion>>

}