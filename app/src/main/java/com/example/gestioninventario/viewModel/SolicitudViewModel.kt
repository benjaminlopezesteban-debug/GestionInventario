package com.example.gestioninventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestioninventario.Dao.DevolucionHerramientaDao
import com.example.gestioninventario.Dao.HerramientaDao
import com.example.gestioninventario.Dao.SolicitudHerramientaDao
import com.example.gestioninventario.Database.appDatabase
import com.example.gestioninventario.model.DevolucionHerramienta
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.SolicitudHerramienta
import com.example.gestioninventario.model.estado.EstadoHerramienta
import com.example.gestioninventario.model.estado.EstadoSolicitudHerramienta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class SolicitudViewModel(application: Application) : AndroidViewModel(application) {

    private val solicitudDao: SolicitudHerramientaDao
    private val herramientaDao: HerramientaDao
    private val devolucionDao: DevolucionHerramientaDao

    // Herramientas disponibles para solicitar
    val herramientasDisponibles: StateFlow<List<Herramienta>>

    // Todas las solicitudes para el Administrador de Inventario
    val todasLasSolicitudes: StateFlow<List<SolicitudHerramienta>>

    init {
        val db = appDatabase.getDatabase(application)
        solicitudDao = db.solicitudHerramientaDao()
        herramientaDao = db.herramientaDao()
        devolucionDao = db.devolucionHerramientaDao()

        herramientasDisponibles = herramientaDao.listarHerramientasDisponibles().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        todasLasSolicitudes = solicitudDao.listarSolicitudes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Solicitudes creadas por el usuario conectado
    fun solicitudesPorUsuario(idUsuario: Int): Flow<List<SolicitudHerramienta>> {
        return solicitudDao.listarPorUsuario(idUsuario)
    }

    // Herramientas actualmente asignadas al usuario conectado (vía DevolucionHerramienta)
    fun herramientasACargo(idUsuario: Int): Flow<List<Herramienta>> {
        return devolucionDao.listarHerramientasACargo(idUsuario)
    }

    // Crear solicitud de herramienta
    fun solicitarHerramienta(
        herramienta: Herramienta,
        idUsuario: Int,
        fechaInicio: LocalDate,
        fechaDevolucion: LocalDate,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val solicitud = SolicitudHerramienta(
                idMaestroObra = idUsuario,
                idObra = 1,
                idHerramienta = herramienta.id,
                nombreHerramienta = herramienta.nombre,
                tipoHerramienta = herramienta.tipo,
                cantidad = 1,
                fechaSolicitud = LocalDateTime.now(),
                fechaRequerida = fechaInicio,
                fechaDevolucionEstimada = fechaDevolucion,
                estado = EstadoSolicitudHerramienta.Pendiente,
                motivoRechazo = null
            )

            solicitudDao.agregarSolicitudHerramienta(solicitud)
            onSuccess()
        }
    }

    // Administrador aprueba o rechaza solicitud
    fun resolverSolicitud(
        solicitud: SolicitudHerramienta,
        aprobar: Boolean,
        idAdmin: Int,
        motivoRechazo: String? = null
    ) {
        viewModelScope.launch {
            if (aprobar) {
                val solicitudActualizada = solicitud.copy(
                    estado = EstadoSolicitudHerramienta.Aprobada,
                    motivoRechazo = null
                )
                solicitudDao.actualizarSolicitudHerramienta(solicitudActualizada)

                // Si tiene id de herramienta asociada, cambiar estado a Prestada
                val idH = solicitud.idHerramienta
                if (idH != null && idH > 0) {
                    val h = herramientaDao.obtenerPorId(idH)
                    if (h != null) {
                        herramientaDao.actualizarHerramienta(h.copy(estado = EstadoHerramienta.Prestada))
                    }
                }

                // Registrar en DevolucionHerramienta (fechaDevolucion = null significa que está en poder del solicitante)
                val devolucion = DevolucionHerramienta(
                    idSolicitudHerramienta = solicitud.id,
                    idHerramienta = solicitud.idHerramienta ?: 0,
                    fechaEntrega = LocalDateTime.now(),
                    fechaDevolucion = null,
                    idUsuarioEntrega = idAdmin,
                    idUsuarioDevolucion = null
                )
                devolucionDao.agregarDevolucionHerramienta(devolucion)

            } else {
                val solicitudActualizada = solicitud.copy(
                    estado = EstadoSolicitudHerramienta.Rechazada(motivoRechazo ?: "Rechazada por administrador"),
                    motivoRechazo = motivoRechazo ?: "Rechazada por administrador"
                )
                solicitudDao.actualizarSolicitudHerramienta(solicitudActualizada)
            }
        }
    }
}

