package com.example.gestioninventario.view

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestioninventario.ui.theme.Black
import com.example.gestioninventario.ui.theme.DarkGray
import com.example.gestioninventario.ui.theme.LightGray
import com.example.gestioninventario.ui.theme.Red
import com.example.gestioninventario.viewModel.HerramientaViewModel

@Composable
fun RegistrarHerramientaScreen(
    herramientaViewModel: HerramientaViewModel = viewModel(),
    onVolver: () -> Unit = {}

) {
    val context = LocalContext.current

    // Selector de imagenes exclusivo desde la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        herramientaViewModel.cambiarImagenUri(uri)
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Red,
        unfocusedBorderColor = DarkGray,
        focusedLabelColor = Red,
        unfocusedLabelColor = DarkGray,
        cursorColor = Red,
        focusedTextColor = Black,
        unfocusedTextColor = Black
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Título con el estilo simple del Login y Home
        Text(
            text = "Registrar herramienta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Previsualización de imagen 
        val selectedUri = herramientaViewModel.imagenUri
        if (selectedUri != null) {
            val bitmap = remember(selectedUri) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(context.contentResolver, selectedUri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(context.contentResolver, selectedUri)
                    }
                } catch (e: Exception) {
                    null
                }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto de la herramienta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, DarkGray, RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Botón para acceder a la galería
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (herramientaViewModel.imagenUri == null) "Seleccionar imagen (opcional)" else "Cambiar imagen de la galería",
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo 1: Nombre de la herramienta
        OutlinedTextField(
            value = herramientaViewModel.nombre,
            onValueChange = { herramientaViewModel.cambiarNombre(it) },
            label = { Text("Nombre de la herramienta") },
            singleLine = true,
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo 2: Tipo de herramienta (String)
        OutlinedTextField(
            value = herramientaViewModel.tipo,
            onValueChange = { herramientaViewModel.cambiarTipo(it) },
            label = { Text("Tipo (ej: Manual, Eléctrica, etc.)") },
            singleLine = true,
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón principal: Guardar herramienta
        Button(
            onClick = {
                herramientaViewModel.guardarHerramienta()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = "Guardar herramienta", fontSize = 16.sp)
        }

        // Botón secundario: Volver
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
            Text(text = "Volver al Home", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Mensajes de retroalimentación
        if (herramientaViewModel.mensajeError != null) {
            Text(
                text = herramientaViewModel.mensajeError ?: "",
                fontSize = 16.sp,
                color = Red,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (herramientaViewModel.mensajeExito != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = herramientaViewModel.mensajeExito ?: "",
                    fontSize = 16.sp,
                    color = Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

