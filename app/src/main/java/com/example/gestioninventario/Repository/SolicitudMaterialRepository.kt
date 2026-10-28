package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.MaterialDao
import com.example.gestioninventario.Dao.ObraDao
import com.example.gestioninventario.Dao.SolicitudMaterialDao
import com.example.gestioninventario.Dao.UsuarioDao
import com.example.gestioninventario.model.HistorialEstado
import com.example.gestioninventario.model.Material
import com.example.gestioninventario.model.SolicitudMaterial
import com.example.gestioninventario.model.estado.EstadoMaterial
import com.example.gestioninventario.model.estado.EstadoSolicitudMaterial
import com.example.gestioninventario.model.estado.PrioridadObra
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class SolicitudMaterialRepository(
    private val solicitudMaterialDao: SolicitudMaterialDao,
    private val materialDao: MaterialDao,
    private val usuarioDao: UsuarioDao,
    private val obraDao: ObraDao,
    private val notificacionRepository: NotificacionRepository,
    private val historialEstadoRepository: HistorialEstadoRepository
) {

    // Valores del campo "rol" guardado en Usuario (deben coincidir con los de la BD)
    private val rolJefeObra = "JEFE DE OBRA"
    private val rolAdministradorInventario = "ADMINISTRADOR INVENTARIO"

    val solicitudMaterial: Flow<List<SolicitudMaterial>> = solicitudMaterialDao.listarSolicitudesMateriales()

    suspend fun almacenarSolicitud(solicitudMaterial: SolicitudMaterial) {
        solicitudMaterialDao.agregarSolicitudMaterial(solicitudMaterial)
    }

    suspend fun eliminarSolicitud(solicitudMaterial: SolicitudMaterial) {
        solicitudMaterialDao.eliminarSolicitudMaterial(solicitudMaterial)
    }

    suspend fun actualizarsolicitud(solicitudMaterial: SolicitudMaterial) {
        solicitudMaterialDao.actualizarSolicitudMaterial(solicitudMaterial)
    }

    // Lógica de negocio

    // B.1 - Crear solicitud de materiales + notificación + historial
    suspend fun crearSolicitud(
        idJefeObra: Int,
        idObra: Int,
        idMaterial: Int,
        cantidad: BigDecimal,
        fechaSolicitud: LocalDateTime = LocalDateTime.now(),
        fechaRequerida: LocalDate
    ) {
        // 1. El usuario debe existir y ser Jefe de Obra
        val usuario = usuarioDao.obtenerPorId(idJefeObra)
            ?: throw IllegalArgumentException("El usuario no ha sido encontrado")

        require(usuario.rol.uppercase() == rolJefeObra) {
            "El rol '${usuario.rol}' no está permitido. Solo un Jefe de Obra puede crear solicitudes de materiales."
        }

        // 2. La obra y el material deben existir
        obraDao.obtenerPorId(idObra)
            ?: throw IllegalArgumentException("La obra no existe")
        materialDao.obtenerPorId(idMaterial)
            ?: throw IllegalArgumentException("El material no existe")

        // 3. Validar los datos ingresados
        require(cantidad > BigDecimal.ZERO) { "La cantidad debe ser mayor a 0" }

        // 4. Construir la solicitud e insertarla (el insert devuelve el id generado)
        val solicitud = SolicitudMaterial(
            idJefeObra = idJefeObra,
            idObra = idObra,
            idMaterial = idMaterial,
            cantidad = cantidad,
            fechaSolicitud = fechaSolicitud,
            fechaRequerida = fechaRequerida,
            estado = EstadoSolicitudMaterial.Pendiente,
            motivoRechazo = null
        )
        val idSolicitud = solicitudMaterialDao.agregarSolicitudMaterial(solicitud).toInt()

        // 5. Notificar al administrador de inventario
        val administrador = usuarioDao.obtenerPorRol(rolAdministradorInventario)
            ?: throw IllegalStateException("No hay administrador de inventario configurado")

        val fecha = LocalDateTime.now()
        notificacionRepository.notificarUsuario(administrador.id, "Solicitud de Material", fecha)

        // 6. Registrar el historial del estado PENDIENTE (lo hace el jefe de obra)
        val registroHistorial = HistorialEstado(
            0, "SolicitudMaterial", idSolicitud,
            null, "Pendiente", fecha, idJefeObra, "Registro Solicitud"
        )
        historialEstadoRepository.almacenarHistorial(registroHistorial)
    }

    // B.2 - Revisar solicitudes pendientes. Administrador de inventario
    fun solicitudesPendientes(): Flow<List<SolicitudMaterial>> =
        solicitudMaterialDao.listarPorEstado("Pendiente")

    // B.3 - Decidir el estado de la solicitud
    private fun decidir(
        cantidadSolicitada: BigDecimal,
        material: Material,
        prioridad: PrioridadObra
    ): EstadoSolicitudMaterial {
        val stockMinimo = BigDecimal(5)
        val stock = material.cantidadDisponible
        return when {
            material.estado != EstadoMaterial.BuenEstado -> {
                EstadoSolicitudMaterial.Rechazada("El material no está en buen estado")
            }
            cantidadSolicitada > stock -> {
                EstadoSolicitudMaterial.SinStock
            }
            stock < stockMinimo && prioridad != PrioridadObra.Alta -> {
                EstadoSolicitudMaterial.SinStock
            }
            else -> {
                EstadoSolicitudMaterial.Aprobada
            }
        }
    }

    // B.4 - Responder a la solicitud + notificación + historial
    suspend fun responderSolicitud(
        solicitudMaterial: SolicitudMaterial,
        idAdministradorInv: Int
    ) {
        require(solicitudMaterial.estado == EstadoSolicitudMaterial.Pendiente) {
            "La solicitud ya fue respondida"
        }

        val obra = obraDao.obtenerPorId(solicitudMaterial.idObra)
            ?: throw IllegalArgumentException("La obra no existe")
        val material = materialDao.obtenerPorId(solicitudMaterial.idMaterial)
            ?: throw IllegalArgumentException("El material no existe")

        val decision = decidir(solicitudMaterial.cantidad, material, obra.prioridad)

        val motivo = when (decision) {
            is EstadoSolicitudMaterial.Rechazada -> decision.motivo
            is EstadoSolicitudMaterial.SinStock ->
                "Stock insuficiente: solicitado ${solicitudMaterial.cantidad}, disponible ${material.cantidadDisponible}"
            else -> null
        }

        // Guardar la solicitud con el nuevo estado
        val solicitudActualizada = solicitudMaterial.copy(
            estado = decision,
            motivoRechazo = motivo
        )
        solicitudMaterialDao.actualizarSolicitudMaterial(solicitudActualizada)

        val nombreEstado = decision::class.simpleName ?: ""
        val fecha = LocalDateTime.now()

        // Notificar al jefe de obra
        notificacionRepository.notificarUsuario(
            solicitudMaterial.idJefeObra, "Solicitud de Material '$nombreEstado'", fecha
        )

        // Registrar el historial (lo hace el administrador de inventario)
        val registroHistorial = HistorialEstado(
            0, "SolicitudMaterial", solicitudMaterial.id,
            "Pendiente", nombreEstado, fecha, idAdministradorInv, "Respuesta Solicitud"
        )
        historialEstadoRepository.almacenarHistorial(registroHistorial)
    }
}