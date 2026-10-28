package com.example.gestioninventario.viewModel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestioninventario.Database.appDatabase
import com.example.gestioninventario.Repository.HerramientaRepository
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.estado.EstadoHerramienta
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HerramientaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HerramientaRepository

    // Flujo con la lista de todas las herramientas
    val listaHerramientas: StateFlow<List<Herramienta>>

    // Campos de formulario para registrar herramienta
    var nombre by mutableStateOf("")
    var tipo by mutableStateOf("")
    var imagenUri by mutableStateOf<Uri?>(null)

    var ultimoIdRegistrado by mutableStateOf<Int?>(null)
    var mensajeError by mutableStateOf<String?>(null)
    var mensajeExito by mutableStateOf<String?>(null)

    // Estado para consulta de herramienta mediante QR / ID
    var herramientaConsultada by mutableStateOf<Herramienta?>(null)
    var mensajeConsulta by mutableStateOf<String?>(null)

    init {
        val dao = appDatabase.getDatabase(application).herramientaDao()
        repository = HerramientaRepository(dao)

        listaHerramientas = repository.herramientas.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun cambiarNombre(nuevoNombre: String) {
        nombre = nuevoNombre
        mensajeError = null
        mensajeExito = null
    }

    fun cambiarTipo(nuevoTipo: String) {
        tipo = nuevoTipo
        mensajeError = null
        mensajeExito = null
    }

    fun cambiarImagenUri(nuevoUri: Uri?) {
        imagenUri = nuevoUri
        mensajeError = null
        mensajeExito = null
    }

    fun limpiarCampos() {
        nombre = ""
        tipo = ""
        imagenUri = null
        mensajeError = null
        mensajeExito = null
    }

    fun guardarHerramienta(onSuccess: (() -> Unit)? = null) {
        val nombreLimpio = nombre.trim()
        val tipoLimpio = tipo.trim()

        if (nombreLimpio.isBlank()) {
            mensajeError = "Por favor ingresa el nombre de la herramienta."
            return
        }

        if (tipoLimpio.isBlank()) {
            mensajeError = "Por favor ingresa el tipo de herramienta."
            return
        }

        viewModelScope.launch {
            try {
                val nuevaHerramienta = Herramienta(
                    nombre = nombreLimpio,
                    tipo = tipoLimpio,
                    codigoQr = "", // No se solicita código QR manual
                    estado = EstadoHerramienta.Disponible,
                    imagenUri = imagenUri?.toString()
                )

                // Se registra con ID autoincremental a partir del 1001
                val herramientaGuardada = repository.registrarHerramientaConBase1000(nuevaHerramienta)

                ultimoIdRegistrado = herramientaGuardada.id
                mensajeExito = "¡Herramienta registrada con éxito! ID asignado: #${herramientaGuardada.id}"
                mensajeError = null

                limpiarCampos()
                onSuccess?.invoke()
            } catch (e: Exception) {
                mensajeError = "Error al guardar la herramienta: ${e.localizedMessage ?: "desconocido"}"
            }
        }
    }

    fun consultarPorQrTexto(codigoQr: String) {
        val textoLimpio = codigoQr.trim()
        val id = textoLimpio.toIntOrNull()

        if (id == null) {
            herramientaConsultada = null
            mensajeConsulta = "El código QR ('$textoLimpio') no corresponde a un ID numérico válido."
            return
        }

        viewModelScope.launch {
            try {
                val resultado = repository.obtenerPorId(id)
                if (resultado != null) {
                    herramientaConsultada = resultado
                    mensajeConsulta = null
                } else {
                    herramientaConsultada = null
                    mensajeConsulta = "No se encontró ninguna herramienta con el ID #$id en la base de datos."
                }
            } catch (e: Exception) {
                herramientaConsultada = null
                mensajeConsulta = "Error al consultar herramienta: ${e.localizedMessage ?: "desconocido"}"
            }
        }
    }

    fun limpiarConsulta() {
        herramientaConsultada = null
        mensajeConsulta = null
    }
}
