package com.example.gestioninventario.viewModel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestioninventario.Database.appDatabase
import com.example.gestioninventario.Repository.UsuarioRepository
import com.example.gestioninventario.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    /* Capa intermedia entre la pantalla y los datos: controlador */

    private val repository: UsuarioRepository

    // Estado del Login
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)
    var mostrarLoginPass by mutableStateOf(false)

    var usuarioActual by mutableStateOf<Usuario?>(null)

    private val _loginCorrecto = MutableStateFlow(false)
    val loginCorrecto = _loginCorrecto.asStateFlow()

    // Estado del Modo Registro vs Modo Login
    var esModoRegistro by mutableStateOf(false)

    // Roles oficiales del sistema (Readme líneas 56 - 107)
    val rolesDisponibles = listOf(
        "Maestro de obra",
        "Jefe de obra",
        "Administrador de inventario",
        "Administrador general"
    )

    // Estado de los campos de registro
    var regMail by mutableStateOf("")
    var regPass by mutableStateOf("")
    var regConfirmPass by mutableStateOf("")
    var regRol by mutableStateOf(rolesDisponibles.first())

    var mostrarRegPass by mutableStateOf(false)
    var mostrarRegConfirmPass by mutableStateOf(false)

    var regError by mutableStateOf<String?>(null)
    var regExito by mutableStateOf<String?>(null)

    init {
        val usuarioDao = appDatabase.getDatabes(application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
    }

    fun cambiarMail(nuevoMail: String) {
        mail = nuevoMail
        textoVisible = false
    }

    fun cambiarPass(nuevaPass: String) {
        pass = nuevaPass
        textoVisible = false
    }

    fun toggleMostrarLoginPass() {
        mostrarLoginPass = !mostrarLoginPass
    }

    fun cambiarModoRegistro(esRegistro: Boolean) {
        esModoRegistro = esRegistro
        regError = null
        regExito = null
        textoVisible = false
    }

    fun cambiarRegMail(nuevoMail: String) {
        regMail = nuevoMail
        regError = null
        regExito = null
    }

    fun cambiarRegPass(nuevaPass: String) {
        regPass = nuevaPass
        regError = null
        regExito = null
    }

    fun cambiarRegConfirmPass(nuevaConfirmPass: String) {
        regConfirmPass = nuevaConfirmPass
        regError = null
        regExito = null
    }

    fun cambiarRegRol(nuevoRol: String) {
        regRol = nuevoRol
        regError = null
        regExito = null
    }

    fun toggleMostrarRegPass() {
        mostrarRegPass = !mostrarRegPass
    }

    fun toggleMostrarRegConfirmPass() {
        mostrarRegConfirmPass = !mostrarRegConfirmPass
    }

    fun cerrarSesion() {
        _loginCorrecto.value = false
        usuarioActual = null
        pass = ""
        textoVisible = false
        regExito = null
        regError = null
    }

    val UsuarioCorrecto =
        Usuario(1, "Juan", "Juan123@gmail.com", "benja1234", "MAESTRO DE OBRA")

    fun login() {
        viewModelScope.launch {
            try {
                val usuario = repository.usuarioLogin(mail.trim(), pass)

                if (usuario != null) {
                    usuarioActual = usuario
                    _loginCorrecto.value = true
                    textoVisible = false
                    println("FRS: Login ok")
                } else {
                    println("FRS: Login error")
                    textoVisible = true
                }
            } catch (e: Exception) {
                textoVisible = true
            }
        }
    }

    fun registrarUsuario(onSuccess: (() -> Unit)? = null) {
        val trimmedMail = regMail.trim()

        if (trimmedMail.isBlank()) {
            regError = "Por favor ingresa un correo electrónico."
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedMail).matches()) {
            regError = "El formato de correo no es válido."
            return
        }

        if (regPass.isBlank()) {
            regError = "Por favor ingresa una contraseña."
            return
        }

        if (regPass.length < 4) {
            regError = "La contraseña debe tener al menos 4 caracteres."
            return
        }

        if (regConfirmPass.isBlank()) {
            regError = "Por favor repite la contraseña para confirmarla."
            return
        }

        if (regPass != regConfirmPass) {
            regError = "Las contraseñas no coinciden."
            return
        }

        if (regRol.isBlank()) {
            regError = "Por favor selecciona un rol."
            return
        }

        viewModelScope.launch {
            try {
                val usuarioExistente = repository.obtenerPorCorreo(trimmedMail)
                if (usuarioExistente != null) {
                    regError = "El correo ya se encuentra registrado."
                    return@launch
                }

                // Normalizamos el rol para máxima compatibilidad con las validaciones del sistema
                val rolDb = when (regRol.trim().lowercase()) {
                    "maestro de obra" -> "MAESTRO DE OBRA"
                    "jefe de obra" -> "JEFE DE OBRA"
                    "administrador de inventario" -> "ADMINISTRADOR INVENTARIO"
                    "administrador general" -> "ADMINISTRADOR GENERAL"
                    else -> regRol.trim().uppercase()
                }

                val nombreUsuario = trimmedMail.substringBefore("@")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

                val nuevoUsuario = Usuario(
                    nombre = nombreUsuario,
                    correo = trimmedMail,
                    contrasena = regPass,
                    rol = rolDb
                )

                repository.almacenarUsuario(nuevoUsuario)

                regExito = "¡Usuario registrado exitosamente!"
                regError = null

                // Prellenar login con el correo registrado y limpiar contraseñas
                mail = trimmedMail
                pass = ""
                regPass = ""
                regConfirmPass = ""

                onSuccess?.invoke()
            } catch (e: Exception) {
                regError = "Error al registrar usuario: ${e.localizedMessage ?: "desconocido"}"
            }
        }
    }
}
