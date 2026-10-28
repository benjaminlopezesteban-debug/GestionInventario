package com.example.gestioninventario.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.ui.theme.Black
import com.example.gestioninventario.ui.theme.DarkGray
import com.example.gestioninventario.ui.theme.LightGray
import com.example.gestioninventario.ui.theme.Red
import com.example.gestioninventario.viewModel.SolicitudViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CrearSolicitudScreen(
    idUsuario: Int,
    solicitudViewModel: SolicitudViewModel = viewModel(),
    onHerramientaSolicitada: () -> Unit = {},
    onVolver: () -> Unit = {}
) {
    val context = LocalContext.current
    val listaDisponibles by solicitudViewModel.herramientasDisponibles.collectAsStateWithLifecycle()

    var herramientaSeleccionada by remember { mutableStateOf<Herramienta?>(null) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaDevolucion by remember { mutableStateOf<LocalDate?>(null) }
    var errorFechas by remember { mutableStateOf<String?>(null) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Ventana de diálogo nativo para seleccionar fechas y confirmar solicitud
    if (herramientaSeleccionada != null) {
        val herramienta = herramientaSeleccionada!!

        AlertDialog(
            onDismissRequest = {
                herramientaSeleccionada = null
                fechaInicio = null
                fechaDevolucion = null
                errorFechas = null
            },
            title = {
                Text(
                    text = "Solicitar ${herramienta.nombre}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ID: #${herramienta.id} - Tipo: ${herramienta.tipo}",
                        fontSize = 14.sp,
                        color = DarkGray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // 1. Selector de Fecha de Inicio
                    val hoy = LocalDate.now()
                    val datePickerInicio = remember(context) {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                fechaInicio = LocalDate.of(year, month + 1, dayOfMonth)
                                errorFechas = null
                            },
                            hoy.year,
                            hoy.monthValue - 1,
                            hoy.dayOfMonth
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerInicio.show() }
                    ) {
                        OutlinedTextField(
                            value = fechaInicio?.format(dateFormatter) ?: "",
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            label = { Text("Fecha de inicio", color = DarkGray) },
                            placeholder = { Text("Toca para seleccionar", color = DarkGray.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Black,
                                disabledBorderColor = DarkGray,
                                disabledLabelColor = DarkGray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Selector de Fecha de Devolución
                    val datePickerDevolucion = remember(context) {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                fechaDevolucion = LocalDate.of(year, month + 1, dayOfMonth)
                                errorFechas = null
                            },
                            hoy.year,
                            hoy.monthValue - 1,
                            hoy.dayOfMonth
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDevolucion.show() }
                    ) {
                        OutlinedTextField(
                            value = fechaDevolucion?.format(dateFormatter) ?: "",
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            label = { Text("Fecha de devolución", color = DarkGray) },
                            placeholder = { Text("Toca para seleccionar", color = DarkGray.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Black,
                                disabledBorderColor = DarkGray,
                                disabledLabelColor = DarkGray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (errorFechas != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorFechas ?: "",
                            color = Red,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val fInicio = fechaInicio
                        val fDevolucion = fechaDevolucion

                        if (fInicio == null || fDevolucion == null) {
                            errorFechas = "Debes seleccionar ambas fechas."
                            return@Button
                        }

                        if (fDevolucion.isBefore(fInicio)) {
                            errorFechas = "La fecha de devolución no puede ser anterior a la de inicio."
                            return@Button
                        }

                        solicitudViewModel.solicitarHerramienta(
                            herramienta = herramienta,
                            idUsuario = idUsuario,
                            fechaInicio = fInicio,
                            fechaDevolucion = fDevolucion,
                            onSuccess = {
                                herramientaSeleccionada = null
                                onHerramientaSolicitada()
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Solicitar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        herramientaSeleccionada = null
                        fechaInicio = null
                        fechaDevolucion = null
                        errorFechas = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Cancelar")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Pantalla con listado de herramientas disponibles para solicitar
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crear Solicitud",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Herramientas disponibles para solicitar: ${listaDisponibles.size}",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (listaDisponibles.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay herramientas disponibles en bodega.",
                    fontSize = 16.sp,
                    color = DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaDisponibles, key = { it.id }) { herramienta ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = LightGray)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = herramienta.nombre,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Black
                                )

                                Text(
                                    text = "#${herramienta.id}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Red
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Tipo: ${herramienta.tipo}",
                                fontSize = 14.sp,
                                color = DarkGray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    herramientaSeleccionada = herramienta
                                    fechaInicio = LocalDate.now()
                                    fechaDevolucion = LocalDate.now().plusDays(7)
                                    errorFechas = null
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Red,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Solicitar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver al Home", fontSize = 16.sp)
        }
    }
}

