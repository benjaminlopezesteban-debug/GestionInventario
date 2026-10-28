package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.DevolucionHerramientaDao
import com.example.gestioninventario.model.DevolucionHerramienta
import kotlinx.coroutines.flow.Flow


class DevolucionHerramientaRepository(private val devolucionHerramientaDao: DevolucionHerramientaDao) {

    val devolucionHerramientaRepository: Flow<List<DevolucionHerramienta>> = devolucionHerramientaDao.listarDevolucionHerramientas()

    suspend fun devolucionHerramientaAlmacenar(devolucionHerramienta: DevolucionHerramienta){
        devolucionHerramientaDao.agregarDevolucionHerramienta(devolucionHerramienta)
    }

    suspend fun eliminarDevolucionHerramienta(devolucionHerramienta: DevolucionHerramienta){
        devolucionHerramientaDao.eliminarDevolucionHerramienta(devolucionHerramienta)
    }

    suspend fun actualizarDevolucionHerramienta(devolucionHerramienta: DevolucionHerramienta){
        devolucionHerramientaDao.actualizarDevolucionHerramienta(devolucionHerramienta)
    }
}