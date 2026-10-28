package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.HerramientaDao
import com.example.gestioninventario.model.Herramienta
import kotlinx.coroutines.flow.Flow

class HerramientaRepository(private val herramientaDao: HerramientaDao) {

    val herramientas: Flow<List<Herramienta>> = herramientaDao.listarHerramienta()

    suspend fun herramientaAlmacenar(herramienta: Herramienta){
        herramientaDao.agregarHerramienta(herramienta)
    }

    suspend fun eliminarHerraienta(herramienta: Herramienta){
        herramientaDao.eliminarHerramienta(herramienta)
    }

    suspend fun actualizarHerramienta(herramienta: Herramienta){
        herramientaDao.actualizarHerramienta(herramienta)
    }

    suspend fun obtenerPorId(id: Int): Herramienta? {
        return herramientaDao.obtenerPorId(id)
    }

    suspend fun obtenerMaxId(): Int? {
        return herramientaDao.obtenerMaxId()
    }

    suspend fun registrarHerramientaConBase1000(herramienta: Herramienta): Herramienta {
        val maxId = herramientaDao.obtenerMaxId()
        val nuevoId = if (maxId == null || maxId < 1000) 1001 else maxId + 1
        val herramientaConId = herramienta.copy(id = nuevoId)
        herramientaDao.agregarHerramienta(herramientaConId)
        return herramientaConId
    }
}
