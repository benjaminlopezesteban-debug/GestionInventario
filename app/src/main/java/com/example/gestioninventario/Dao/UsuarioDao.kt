package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun agregarUsuario(usuario: Usuario)

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Delete
    suspend fun eliminarUsuario(usuario: Usuario)

    @Query("SELECT * FROM Usuario ORDER BY id")
    fun listarUsuario(): Flow<List<Usuario>>

    @Query(
        """SELECT * FROM Usuario
            WHERE correo = :mail AND contrasena = :pass
            LIMIT 1
    """)
    suspend fun usuarioLogin(mail: String, pass: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT * FROM Usuario WHERE LOWER(correo) = LOWER(:mail) LIMIT 1")
    suspend fun obtenerPorCorreo(mail: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE UPPER(rol) = UPPER(:rol) LIMIT 1")
    suspend fun notificarAdministradoresInventario(rol: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE UPPER(rol) = UPPER(:rol) LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?
}
