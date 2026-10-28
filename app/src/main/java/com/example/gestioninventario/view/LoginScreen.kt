package com.example.gestioninventario.view

import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestioninventario.ui.theme.Black
import com.example.gestioninventario.ui.theme.DarkGray
import com.example.gestioninventario.ui.theme.Red
import com.example.gestioninventario.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    LoginViewModel: LoginViewModel = viewModel()
) {
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
        if (!LoginViewModel.esModoRegistro) {
            // ==========================================
            // VISTA ORIGINAL DE LOGIN (CON COLORES FINALES Y BOTÓN REGISTRARSE)
            // ==========================================
            Text(
                text = "Inicia sesión",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            OutlinedTextField(
                value = LoginViewModel.mail,
                onValueChange = {
                    LoginViewModel.cambiarMail(it)
                },
                label = {
                    Text("Correo Electronico")
                },
                singleLine = true,
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = LoginViewModel.pass,
                onValueChange = {
                    LoginViewModel.cambiarPass(it)
                },
                label = {
                    Text("Contraseña")
                },
                singleLine = true,
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    println(" FSR: Botón Presionado..")
                    LoginViewModel.login()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "ingresar", fontSize = 16.sp)
            }

            // Botón de registrarse agregado a la pestaña de Login
            Button(
                onClick = {
                    LoginViewModel.cambiarModoRegistro(true)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Registrarse", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (LoginViewModel.textoVisible) {
                Text(
                    text = "Credenciales inválidas.",
                    fontSize = 20.sp,
                    color = Red,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (LoginViewModel.regExito != null) {
                Text(
                    text = LoginViewModel.regExito ?: "",
                    fontSize = 16.sp,
                    color = DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }

        } else {
            // ==========================================
            // VISTA DE REGISTRO DE USUARIO (MISMO FORMATO SIMPLE)
            // ==========================================
            Text(
                text = "Registrarse",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Campo Correo
            OutlinedTextField(
                value = LoginViewModel.regMail,
                onValueChange = {
                    LoginViewModel.cambiarRegMail(it)
                },
                label = {
                    Text("Correo Electronico")
                },
                singleLine = true,
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo Contraseña (1ra vez)
            OutlinedTextField(
                value = LoginViewModel.regPass,
                onValueChange = {
                    LoginViewModel.cambiarRegPass(it)
                },
                label = {
                    Text("Contraseña")
                },
                singleLine = true,
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo Contraseña (2da vez)
            OutlinedTextField(
                value = LoginViewModel.regConfirmPass,
                onValueChange = {
                    LoginViewModel.cambiarRegConfirmPass(it)
                },
                label = {
                    Text("Repetir Contraseña")
                },
                singleLine = true,
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Casilla de selección para el ROL (los 4 roles del Readme)
            var rolExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = rolExpanded,
                onExpandedChange = { rolExpanded = !rolExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = LoginViewModel.regRol,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = rolExpanded)
                    },
                    colors = textFieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = rolExpanded,
                    onDismissRequest = { rolExpanded = false }
                ) {
                    LoginViewModel.rolesDisponibles.forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(text = rol, color = Black) },
                            onClick = {
                                LoginViewModel.cambiarRegRol(rol)
                                rolExpanded = false
                            }
                        )
                    }
                }
            }

            // Botón de confirmación de Registro
            Button(
                onClick = {
                    LoginViewModel.registrarUsuario()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Registrar usuario", fontSize = 16.sp)
            }

            // Botón para volver al login
            Button(
                onClick = {
                    LoginViewModel.cambiarModoRegistro(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Volver a iniciar sesión", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (LoginViewModel.regError != null) {
                Text(
                    text = LoginViewModel.regError ?: "",
                    fontSize = 16.sp,
                    color = Red,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (LoginViewModel.regExito != null) {
                Text(
                    text = LoginViewModel.regExito ?: "",
                    fontSize = 16.sp,
                    color = DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
