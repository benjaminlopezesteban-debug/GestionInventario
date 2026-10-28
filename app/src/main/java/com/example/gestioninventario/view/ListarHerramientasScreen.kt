package com.example.gestioninventario.view

import androidx.compose.foundation.background
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
import com.example.gestioninventario.ui.theme.Black
import com.example.gestioninventario.ui.theme.DarkGray
import com.example.gestioninventario.ui.theme.LightGray
import com.example.gestioninventario.ui.theme.Red
import com.example.gestioninventario.viewModel.HerramientaViewModel

@Composable
fun ListarHerramientasScreen(
    herramientaViewModel: HerramientaViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    val listaHerramientas by herramientaViewModel.listaHerramientas.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado
        Text(
            text = "Herramientas",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Total registradas: ${listaHerramientas.size}",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //Verificación de herramientas registradas
        if (listaHerramientas.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay herramientas registradas aún.",
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
                items(listaHerramientas, key = { it.id }) { herramienta ->
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
                                color = DarkGray,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Volver
        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = "Volver al Inventario", fontSize = 16.sp)
        }
    }
}

