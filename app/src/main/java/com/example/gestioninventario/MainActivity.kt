package com.example.gestioninventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestioninventario.ui.theme.GestionInventarioTheme
import com.example.gestioninventario.view.HomeScreen
import com.example.gestioninventario.view.LoginScreen
import com.example.gestioninventario.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestionInventarioTheme {
                val loginViewModel: LoginViewModel = viewModel()
                val loginCorrecto by loginViewModel.loginCorrecto.collectAsState()

                if (loginCorrecto) {
                    HomeScreen(
                        usuario = loginViewModel.usuarioActual,
                        onCerrarSesion = { loginViewModel.cerrarSesion() }
                    )
                } else {
                    LoginScreen(
                        LoginViewModel = loginViewModel
                    )
                }
            }
        }
    }
}
