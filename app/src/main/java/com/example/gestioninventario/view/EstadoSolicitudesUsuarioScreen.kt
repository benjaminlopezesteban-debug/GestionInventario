package com.example.gestioninventario.view

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestioninventario.model.estado.EstadoSolicitudHerramienta
import com.example.gestioninventario.ui.theme.Black
import com.example.gestioninventario.ui.theme.DarkGray
import com.example.gestioninventario.ui.theme.LightGray
import com.example.gestioninventario.ui.theme.Red
import com.example.gestioninventario.viewModel.SolicitudViewModel
import java.time.format.DateTimeFormatter

@Composable
fun EstadoSolicitudesUsuarioScreen(
    idUsuario: Int,
    solicitudViewModel: SolicitudViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val solicitudes by solicitudViewModel.solicitudesPorUsuario(idUsuario)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mis Solicitudes",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Total de solicitudes realizadas: ${solicitudes.size}",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (solicitudes.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no has creado ninguna solicitud.",
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
                items(solicitudes, key = { it.id }) { solicitud ->
                    val estadoTexto = when (solicitud.estado) {
                        is EstadoSolicitudHerramienta.Pendiente -> "Pendiente"
                        is EstadoSolicitudHerramienta.Aprobada -> "Aprobada"
                        is EstadoSolicitudHerramienta.Rechazada -> "Rechazada"
                        is EstadoSolicitudHerramienta.SinStock -> "Sin Stock"
                        is EstadoSolicitudHerramienta.Entregada -> "Entregada"
                        is EstadoSolicitudHerramienta.Devuelta -> "Devuelta"
                        is EstadoSolicitudHerramienta.FueraDePlazo -> "Fuera de Plazo"
                    }

                    val colorEstado = when (solicitud.estado) {
                        is EstadoSolicitudHerramienta.Pendiente -> Red
                        is EstadoSolicitudHerramienta.Aprobada -> DarkGray
                        is EstadoSolicitudHerramienta.Rechazada -> Red
                        else -> DarkGray
                    }

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
                                    text = if (solicitud.nombreHerramienta.isNotBlank()) solicitud.nombreHerramienta else "Herramienta",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Black
                                )

                                Text(
                                    text = "Sol. #${solicitud.id}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Red
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Tipo: ${solicitud.tipoHerramienta}",
                                fontSize = 14.sp,
                                color = DarkGray
                            )

                            Text(
                                text = "Período: ${solicitud.fechaRequerida.format(dateFormatter)} al ${solicitud.fechaDevolucionEstimada.format(dateFormatter)}",
                                fontSize = 13.sp,
                                color = DarkGray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Estado: $estadoTexto",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorEstado
                            )

                            if (solicitud.motivoRechazo != null) {
                                Text(
                                    text = "Motivo: ${solicitud.motivoRechazo}",
                                    fontSize = 13.sp,
                                    color = Red
                                )
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

