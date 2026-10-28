package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.UsuarioDao
import com.example.gestioninventario.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    val usuario: Flow<List<Usuario>> = usuarioDao.listarUsuario()

    suspend fun almacenarUsuario(usuario: Usuario){
        usuarioDao.agregarUsuario(usuario)
    }

    suspend fun eliminarUsuario(usuario: Usuario){
        usuarioDao.eliminarUsuario(usuario)
    }

    suspend fun actualizarUsuario(usuario: Usuario){
        usuarioDao.actualizarUsuario(usuario)
    }

    suspend fun usuarioLogin(mail: String, pass: String): Usuario?{
        return usuarioDao.usuarioLogin(mail, pass)
    }

    suspend fun obtenerPorCorreo(mail: String): Usuario? {
        return usuarioDao.obtenerPorCorreo(mail)
    }
}
