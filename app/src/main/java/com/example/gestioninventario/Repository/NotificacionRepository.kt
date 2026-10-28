package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.NotificacionDao
import com.example.gestioninventario.Dao.UsuarioDao
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.Notificacion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class NotificacionRepository(
    private val notificacionDao: NotificacionDao,
    private val usuarioDao: UsuarioDao
    ) {

    val notificacion: Flow<List<Notificacion>> = notificacionDao.listarNotificaciones()

    suspend fun aAlmacenarNotificacion(notificacion: Notificacion){
        notificacionDao.agregarNotificacion(notificacion)
    }

    suspend fun eliminarNotificiacion(notificacion: Notificacion){
        notificacionDao.eliminarNotificacion(notificacion)
    }

    suspend fun actualizarNotificacion(notificacion: Notificacion){
        notificacionDao.actualizarNotificacion(notificacion)
    }
         // Para un usuario específico (maestro, jefe de obra, etc.)
        suspend fun notificarUsuario(idUsuario: Int, mensaje: String, fecha: LocalDateTime ) {
            notificacionDao.agregarNotificacion(
                Notificacion(
                    idUsuario = idUsuario,
                    mensaje = mensaje,
                    fecha = fecha,
                    leida = false
                )
            )

        }

}


