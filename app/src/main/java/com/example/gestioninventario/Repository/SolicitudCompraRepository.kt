package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.SolicitudCompraDao
import com.example.gestioninventario.Dao.UsuarioDao
import com.example.gestioninventario.model.HistorialEstado
import com.example.gestioninventario.model.SolicitudCompra
import com.example.gestioninventario.model.estado.EstadoSolicitudCompra
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

class SolicitudCompraRepository(
    private val solicitudCompraDao: SolicitudCompraDao,
    private val usuarioDao: UsuarioDao,
    private val notificacionRepository: NotificacionRepository,
    private val historialEstadoRepository: HistorialEstadoRepository
) {

    // Valores del campo "rol" guardado en Usuario (deben coincidir con los de la BD)
    private val rolAdministradorInventario = "ADMINISTRADOR INVENTARIO"
    private val rolAdministradorGeneral = "ADMINISTRADOR GENERAL"

    val solicitudCompra: Flow<List<SolicitudCompra>> = solicitudCompraDao.listarSolicitudes()

    suspend fun almacenarSolicitud(solicitudCompra: SolicitudCompra) {
        solicitudCompraDao.agregarSolicitudCompra(solicitudCompra)
    }

    suspend fun eliminarSolicitud(solicitudCompra: SolicitudCompra) {
        solicitudCompraDao.eliminarSolicitudCompra(solicitudCompra)
    }

    suspend fun actualizarSolicitud(solicitudCompra: SolicitudCompra) {
        solicitudCompraDao.actualizarSolicitudCompra(solicitudCompra)
    }

    // Lógica de negocio

    // C.1 - Crear solicitud de compra + notificación + historial
    suspend fun crearSolicitud(
        idSolicitante: Int,
        tipoRecurso: String,
        nombreRecurso: String,
        cantidad: BigDecimal
    ) {
        // 1. El usuario debe existir y ser Administrador de Inventario
        val usuario = usuarioDao.obtenerPorId(idSolicitante)
            ?: throw IllegalArgumentException("El usuario no ha sido encontrado")

        require(usuario.rol.uppercase() == rolAdministradorInventario) {
            "El rol '${usuario.rol}' no está permitido. Solo un Administrador de Inventario puede crear solicitudes de compra."
        }

        // 2. Validar los datos ingresados
        require(tipoRecurso.isNotBlank()) { "Debe indicar el tipo de recurso" }
        require(nombreRecurso.isNotBlank()) { "Debe indicar el nombre del recurso" }
        require(cantidad > BigDecimal.ZERO) { "La cantidad debe ser mayor a 0" }

        // 3. Construir la solicitud e insertarla (el insert devuelve el id generado)
        val solicitud = SolicitudCompra(
            tipoRecurso = tipoRecurso,
            nombreRecurso = nombreRecurso,
            cantidad = cantidad,
            idSolicitante = idSolicitante,
            estado = EstadoSolicitudCompra.CompraPendiente
        )
        val idSolicitud = solicitudCompraDao.agregarSolicitudCompra(solicitud).toInt()

        // 4. Notificar al administrador general
        val administradorGeneral = usuarioDao.obtenerPorRol(rolAdministradorGeneral)
            ?: throw IllegalStateException("No hay administrador general configurado")

        val fecha = LocalDateTime.now()
        notificacionRepository.notificarUsuario(administradorGeneral.id, "Solicitud de Compra", fecha)

        // 5. Registrar el historial del estado COMPRA_PENDIENTE
        val registroHistorial = HistorialEstado(
            0, "SolicitudCompra", idSolicitud,
            null, "CompraPendiente", fecha, idSolicitante, "Registro Solicitud"
        )
        historialEstadoRepository.almacenarHistorial(registroHistorial)
    }

    // C.2 - Revisar solicitudes de compra pendientes. Administrador general
    fun solicitudesPendientes(): Flow<List<SolicitudCompra>> =
        solicitudCompraDao.listarPorEstado("CompraPendiente")

    // C.3 - Responder a la solicitud (aceptar o rechazar) + notificación + historial
    suspend fun responderSolicitud(
        solicitudCompra: SolicitudCompra,
        idAdministradorGeneral: Int,
        aprobada: Boolean,
        mensaje: String
    ) {
        require(solicitudCompra.estado == EstadoSolicitudCompra.CompraPendiente) {
            "La solicitud ya fue respondida"
        }

        val usuario = usuarioDao.obtenerPorId(idAdministradorGeneral)
            ?: throw IllegalArgumentException("El usuario no ha sido encontrado")

        require(usuario.rol.uppercase() == rolAdministradorGeneral) {
            "El rol '${usuario.rol}' no está permitido. Solo el Administrador General puede responder solicitudes de compra."
        }

        require(mensaje.isNotBlank()) {
            if (aprobada) "Debe indicar los recursos adquiridos" else "Debe indicar el motivo del rechazo"
        }

        val decision = if (aprobada) {
            EstadoSolicitudCompra.CompraRealizada(mensaje)
        } else {
            EstadoSolicitudCompra.CompraRechazada(mensaje)
        }

        val fecha = LocalDateTime.now()

        // Guardar la solicitud con el nuevo estado
        val solicitudActualizada = solicitudCompra.copy(
            estado = decision,
            idAprueba = idAdministradorGeneral,
            mensajeRespuesta = mensaje,
            fechaRespuesta = fecha
        )
        solicitudCompraDao.actualizarSolicitudCompra(solicitudActualizada)

        val nombreEstado = decision::class.simpleName ?: ""

        // Notificar al administrador de inventario (quien creó la solicitud)
        notificacionRepository.notificarUsuario(
            solicitudCompra.idSolicitante, "Solicitud de Compra '$nombreEstado'", fecha
        )

        // Registrar el historial (lo hace el administrador general)
        val registroHistorial = HistorialEstado(
            0, "SolicitudCompra", solicitudCompra.id,
            "CompraPendiente", nombreEstado, fecha, idAdministradorGeneral, "Respuesta Solicitud"
        )
        historialEstadoRepository.almacenarHistorial(registroHistorial)
    }
}