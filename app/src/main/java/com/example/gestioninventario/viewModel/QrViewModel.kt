package com.example.gestioninventario.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QrViewModel: ViewModel() {
    private val _contenidoQr = MutableStateFlow("") /*Variable privada inicia con _)*/
    val contenidoQr = _contenidoQr.asStateFlow()

    //Privada es para el viewModel
    //Publica para la pantalla
    //Se debe separa en responsabilidad. Y para eso se enlazan las variables con sus contenidos.

    fun guardarQR(contenido: String){
        _contenidoQr.value = contenido
    }

}