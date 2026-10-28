package com.example.gestioninventario.view

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.gestioninventario.viewModel.QrViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun QrScreen(
    qrViewModel: QrViewModel = viewModel(),
    herramientaViewModel: HerramientaViewModel = viewModel(),
    onVolver: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val contenidoQR by qrViewModel.contenidoQr.collectAsStateWithLifecycle()

    val opciones = remember {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }

    val scanner = remember(context, opciones) {
        GmsBarcodeScanning.getClient(context, opciones)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Escanear QR de Herramienta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Escanea el código QR adherido a la herramienta para consultar sus datos en bodega.",
            fontSize = 14.sp,
            color = DarkGray,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Botón principal de escaneo
        Button(
            onClick = {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        val contenido = barcode.rawValue.orEmpty().trim()
                        qrViewModel.guardarQR(contenido)
                        herramientaViewModel.consultarPorQrTexto(contenido)
                    }
                    .addOnCanceledListener {
                        // Escaneo cancelado
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(
                            context,
                            "Error al escanear: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Iniciar escaneo QR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Si se leyó un código QR
        if (contenidoQR.isNotEmpty()) {
            Text(
                text = "Código QR escaneado: $contenidoQR",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Si se encontró la herramienta
        val herramienta = herramientaViewModel.herramientaConsultada
        if (herramienta != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = LightGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HERRAMIENTA ENCONTRADA",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Red,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Imagen si existe
                    val fotoUri = herramienta.imagenUri?.let { Uri.parse(it) }
                    if (fotoUri != null) {
                        val bitmap = remember(fotoUri) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    val source = ImageDecoder.createSource(context.contentResolver, fotoUri)
                                    ImageDecoder.decodeBitmap(source)
                                } else {
                                    @Suppress("DEPRECATION")
                                    MediaStore.Images.Media.getBitmap(context.contentResolver, fotoUri)
                                }
                            } catch (e: Exception) {
                                null
                            }
                        }

                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto de herramienta",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, DarkGray, RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Text(
                        text = "ID: #${herramienta.id}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Text(
                        text = "Nombre: ${herramienta.nombre}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Black
                    )
                    Text(
                        text = "Tipo: ${herramienta.tipo}",
                        fontSize = 15.sp,
                        color = DarkGray
                    )
                    val estadoNombre = when (herramienta.estado) {
                        is com.example.gestioninventario.model.estado.EstadoHerramienta.Disponible -> "Disponible"
                        is com.example.gestioninventario.model.estado.EstadoHerramienta.Prestada -> "Prestada"
                        is com.example.gestioninventario.model.estado.EstadoHerramienta.MalEstado -> "Mal Estado"
                        is com.example.gestioninventario.model.estado.EstadoHerramienta.DadaDeBaja -> "Dada de Baja"
                    }
                    Text(
                        text = "Estado: $estadoNombre",
                        fontSize = 15.sp,
                        color = DarkGray
                    )
                }
            }
        }

        // Mensaje de consulta (por ejemplo, si no se encontró o el formato es incorrecto)
        if (herramientaViewModel.mensajeConsulta != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = herramientaViewModel.mensajeConsulta ?: "",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Red
            )
        }

        if (onVolver != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onVolver,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver", fontSize = 16.sp)
            }
        }
    }
}
