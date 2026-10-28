package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.RackDao
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.Rack
import kotlinx.coroutines.flow.Flow

class RackRepository(private val rackDao: RackDao) {

    val rack: Flow<List<Rack>> = rackDao.listarRack()

    suspend fun rackAlmacenar(rack: Rack){
        rackDao.agregarRack(rack)
    }

    suspend fun eliminarRack(rack: Rack){
        rackDao.agregarRack(rack)
    }

    suspend fun actualizarRack(rack: Rack){
        rackDao.actualizarRack(rack)
    }
}