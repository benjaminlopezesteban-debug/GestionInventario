package com.example.gestioninventario.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestioninventario.model.Usuario
import com.example.gestioninventario.ui.theme.Black
import com.example.gestioninventario.ui.theme.DarkGray
import com.example.gestioninventario.ui.theme.Red

@Composable
fun HomeScreen(
    usuario: Usuario? = null,
    onCerrarSesion: () -> Unit = {}
) {
    // Control de navegación interna en Home
    var vistaActual by remember { mutableStateOf("home") }
    var mensajeAccion by remember { mutableStateOf<String?>(null) }

    val idUsuario = usuario?.id ?: 1
    val rolUpper = usuario?.rol?.trim()?.uppercase() ?: ""

    // Validación IF del rol del usuario
    val esAdmin = rolUpper in listOf("ADMINISTRADOR GENERAL", "ADMINISTRADOR INVENTARIO") || rolUpper.contains("ADMIN")

    when (vistaActual) {
        "inventario" -> {
            // INVENTARIO ADMINISTRADORES

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Inventario",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "Gestión de Herramientas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Red,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // 1. Botón: Registrar Herramienta
                Button(
                    onClick = { vistaActual = "registrar_herramienta" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Registrar Herramienta", fontSize = 16.sp)
                }

                // 2. Botón: Ver todas las Herramientas
                Button(
                    onClick = { vistaActual = "listar_herramientas" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Ver todas las Herramientas", fontSize = 16.sp)
                }

                // 3. Botón: Escanear QR
                Button(
                    onClick = { vistaActual = "escanear_qr" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Escanear QR", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón: Volver al Home
                Button(
                    onClick = { vistaActual = "home" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Volver al Home", fontSize = 16.sp)
                }
            }
        }

        "registrar_herramienta" -> {
            RegistrarHerramientaScreen(
                onVolver = { vistaActual = "inventario" }
            )
        }

        "listar_herramientas" -> {
            ListarHerramientasScreen(
                onVolver = { vistaActual = "inventario" }
            )
        }

        "escanear_qr" -> {
            QrScreen(
                onVolver = { vistaActual = "inventario" }
            )
        }

        "crear_solicitud" -> {
            CrearSolicitudScreen(
                idUsuario = idUsuario,
                onHerramientaSolicitada = {
                    mensajeAccion = "Herramienta solicitada"
                    vistaActual = "home"
                },
                onVolver = { vistaActual = "home" }
            )
        }

        "estado_mis_solicitudes" -> {
            EstadoSolicitudesUsuarioScreen(
                idUsuario = idUsuario,
                onVolver = { vistaActual = "home" }
            )
        }

        "herramientas_a_cargo" -> {
            HerramientasACargoScreen(
                idUsuario = idUsuario,
                onVolver = { vistaActual = "home" }
            )
        }

        "revisar_solicitudes" -> {
            RevisarSolicitudesScreen(
                idAdmin = idUsuario,
                onVolver = { vistaActual = "home" }
            )
        }

        else -> {
            // ==========================================
            // PANTALLA PRINCIPAL HOME (FILTRADO POR ROL)
            // ==========================================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Home",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = usuario?.rol ?: "Usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Red,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = if (usuario != null) "Usuario: ${usuario.nombre} (${usuario.correo})" else "",
                    fontSize = 14.sp,
                    color = DarkGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )


                // VALIDACIÓN IF SEGÚN EL ROL DEL USUARIO

                if (esAdmin) {
                    // Opciones exclusivas para Administrador General y Administrador de Inventario:
                    Button(
                        onClick = {
                            mensajeAccion = null
                            vistaActual = "inventario"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Inventario", fontSize = 16.sp)
                    }

                    Button(
                        onClick = {
                            mensajeAccion = null
                            vistaActual = "revisar_solicitudes"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Ver solicitudes", fontSize = 16.sp)
                    }

                } else {
                    // Opciones para Maestro de Obra y Jefe de Obra:
                    // 1. Crear solicitud
                    Button(
                        onClick = {
                            mensajeAccion = null
                            vistaActual = "crear_solicitud"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Crear solicitud", fontSize = 16.sp)
                    }

                    // 2. Ver estado de mis solicitudes
                    Button(
                        onClick = {
                            mensajeAccion = null
                            vistaActual = "estado_mis_solicitudes"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Ver estado de mis solicitudes", fontSize = 16.sp)
                    }

                    // 3. Herramientas a mi cargo
                    Button(
                        onClick = {
                            mensajeAccion = null
                            vistaActual = "herramientas_a_cargo"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = "Herramientas a mi cargo", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Cerrar Sesión (común a todos los roles)
                Button(
                    onClick = onCerrarSesion,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Cerrar sesión", fontSize = 16.sp)
                }

                // Mensaje en pantalla tras solicitar herramienta u otra acción
                if (mensajeAccion != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = mensajeAccion ?: "",
                        fontSize = 18.sp,
                        color = Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
