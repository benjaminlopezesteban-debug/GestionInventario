package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.DevolucionHerramientaDao
import com.example.gestioninventario.Dao.HerramientaDao
import com.example.gestioninventario.Dao.MovimientoBodegaDao
import com.example.gestioninventario.Dao.NotificacionDao
import com.example.gestioninventario.Dao.ObraDao
import com.example.gestioninventario.Dao.SolicitudHerramientaDao
import com.example.gestioninventario.Dao.UsuarioDao
import com.example.gestioninventario.Database.appDatabase
import com.example.gestioninventario.model.HistorialEstado
import com.example.gestioninventario.model.SolicitudHerramienta
import com.example.gestioninventario.model.estado.EstadoSolicitudHerramienta
import com.example.gestioninventario.model.estado.PrioridadObra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import java.time.LocalDate
import java.time.LocalDateTime

class SolicitudHerramientaRepository(
    private val solicitudHerramientaDao: SolicitudHerramientaDao,
    private val herramientaDao: HerramientaDao,
    private val devolucionDao: DevolucionHerramientaDao,
    private val movimientoDao: MovimientoBodegaDao,
    private val usuarioDao: UsuarioDao,
    private val db: appDatabase,
    private val obraDao : ObraDao,
    private val notificacionRepository: NotificacionRepository,
    private val historialEstadoRepository: HistorialEstadoRepository
    ) {

    private val rolMaestroObra = "MAESTRO DE OBRA"
    private val rolAdministradorInventario = "ADMINISTRADOR INVENTARIO"
    val solicitudHerramienta: Flow<List<SolicitudHerramienta>> = solicitudHerramientaDao.listarSolicitudes()

    suspend fun almacenarSolicitud(solicitudHerramienta: SolicitudHerramienta){
        solicitudHerramientaDao.agregarSolicitudHerramienta(solicitudHerramienta)
    }

    suspend fun eliminarSolicitud(solicitudHerramienta: SolicitudHerramienta){
        solicitudHerramientaDao.eliminarSolicitudHerramienta(solicitudHerramienta)
    }

    suspend fun actualizarSolicitud(solicitudHerramienta: SolicitudHerramienta){
        solicitudHerramientaDao.actualizarSolicitudHerramienta(solicitudHerramienta)
    }

    //Logica de Negocio A

    // A.1 - Crear Solicitud de Herramientas + notificación + Historial
    suspend fun crearSolicitud(
        idMaestroObra: Int,
        idObra: Int,
        tipoHerramienta: String,
        cantidad: Int,
        fechaSolicitud: LocalDateTime = LocalDateTime.now(),
        fechaRequerida: LocalDate,
        fechaDevolucionEstimada: LocalDate
    ) {
        // 1. El usuario debe existir y ser Maestro de Obra
        val usuario = usuarioDao.obtenerPorId(idMaestroObra)
            ?: throw IllegalArgumentException("El usuario no ha sido encontrado")

        require(usuario.rol.uppercase() == rolMaestroObra) {
            "El rol '${usuario.rol}' no está permitido. Solo un Maestro de Obra puede crear solicitudes de herramientas."
        }

        // 2. La obra debe existir
        obraDao.obtenerPorId(idObra)
            ?: throw IllegalArgumentException("La obra no existe")

        // 3. Validar los datos ingresados
        require(tipoHerramienta.isNotBlank()) { "Debe indicar el tipo de herramienta" }
        require(cantidad > 0) { "La cantidad debe ser mayor a 0" }
        require(fechaDevolucionEstimada > fechaRequerida) {
            "La fecha de devolución debe ser posterior a la fecha requerida"
        }

        // 4. Construir la solicitud e insertarla
        val solicitud = SolicitudHerramienta(
            idMaestroObra = idMaestroObra,
            idObra = idObra,
            tipoHerramienta = tipoHerramienta,
            cantidad = cantidad,
            fechaSolicitud = fechaSolicitud,
            fechaRequerida = fechaRequerida,
            fechaDevolucionEstimada = fechaDevolucionEstimada,
            estado = EstadoSolicitudHerramienta.Pendiente,
            motivoRechazo = null
        )

        val idSolicitud = solicitudHerramientaDao.agregarSolicitudHerramienta(solicitud).toInt()


        //5. Enviar la notificación al id del admin del inventario cuando esta realizada la solicitud
        val usuarioAdmin = usuarioDao.notificarAdministradoresInventario(rolAdministradorInventario)
            ?: throw IllegalStateException("No hay administrador de inventario configurado")

        val id = usuarioAdmin.id //No se resolvia la referencia porque el usuario podria ser null con ?

        val fecha = LocalDateTime.now()
        notificacionRepository.notificarUsuario(id, "Solicitud de Herramienta",
            fecha)

        // 6. Construir el historial estado Pendiente
        val registroHistorial = HistorialEstado( 0, "SolicitudHerramienta", idSolicitud,
            null, "Pendiente", fecha, idMaestroObra, "Registro Solicitud")

        historialEstadoRepository.almacenarHistorial(registroHistorial)
    }


    //A.2 - Revisar Solicitud de Herramientas. Administrador Inventario
    fun solicitudesPendientes(): Flow<List<SolicitudHerramienta>> =
        solicitudHerramientaDao.listarPorEstado("PENDIENTE")
    //val pendientes by viewModel.pendientes.collectAsState() PAra agregar a pantalla


    //A.3 - Decidir si enviar o no al usuario
    private fun decidir(cantidadSolicitada: Int, stock: Int, prioridad: PrioridadObra): EstadoSolicitudHerramienta {
        val stockMinimo = 5
        return when {
            cantidadSolicitada > stock -> {
                EstadoSolicitudHerramienta.Rechazada(
                    "Stock insuficiente: solicitado $cantidadSolicitada, disponible $stock"
                )
            }
            stock < stockMinimo && prioridad != PrioridadObra.Alta -> {
                EstadoSolicitudHerramienta.SinStock
            }
            else -> {
                EstadoSolicitudHerramienta.Aprobada
            }
        }
    }

    //A.4 - Responder a la solicitud + notificación + historial
    suspend fun responderSolicitud(
        solicitudHerramienta: SolicitudHerramienta,
        idAdministradorInv: Int,
        fechaDevolucionEstimada: LocalDate

    ) {
        require(solicitudHerramienta.estado == EstadoSolicitudHerramienta.Pendiente) {
            "La solicitud ya fue respondida"
        }

        val obra = obraDao.obtenerPorId(solicitudHerramienta.idObra)
            ?: throw IllegalArgumentException("La obra no existe")

        val stock = herramientaDao.obtenerHerramientasDisponibles(solicitudHerramienta.tipoHerramienta)

        val decision = decidir(solicitudHerramienta.cantidad, stock, obra.prioridad)
        val aprobada = decision is EstadoSolicitudHerramienta.Aprobada

        val solicitudActualizada = solicitudHerramienta.copy(
            estado = decision,
            motivoRechazo = when (decision) {
                is EstadoSolicitudHerramienta.SinStock -> "Se ha rechazado la solicitud por falta de stock"
                else -> null
            },
            fechaDevolucionEstimada = if (aprobada) fechaDevolucionEstimada
            else solicitudHerramienta.fechaDevolucionEstimada
        )

        solicitudHerramientaDao.actualizarSolicitudHerramienta(solicitudActualizada)

        //Notificación
        val idMaestroObra = solicitudHerramienta.idMaestroObra
        val fecha = LocalDateTime.now()
        notificacionRepository.notificarUsuario(idMaestroObra, "Solicitud de Herramienta '${decision}'", fecha)


        //Historial
        val registroHistorial = HistorialEstado( 0, "SolicitudHerramienta", solicitudHerramienta.id,
            "Pendiente", "'${decision}'", fecha, idAdministradorInv, "Respuesta Solicitud")
        historialEstadoRepository.almacenarHistorial(registroHistorial)

    }






}