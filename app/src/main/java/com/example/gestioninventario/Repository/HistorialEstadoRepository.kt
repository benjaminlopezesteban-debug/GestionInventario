package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.HistorialEstadoDao
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.HistorialEstado
import kotlinx.coroutines.flow.Flow

class HistorialEstadoRepository(private val historialEstadoDao: HistorialEstadoDao) {

    val historialEstado: Flow<List<HistorialEstado>> = historialEstadoDao.listarHistorial()

    suspend fun almacenarHistorial(historialEstado: HistorialEstado){
        historialEstadoDao.agregarHistorialEstado(historialEstado)
    }

    suspend fun eliminarHistorial(historialEstado: HistorialEstado){
        historialEstadoDao.eliminarHistorialEstado(historialEstado)
    }

    suspend fun actualizarHistorial(historialEstado: HistorialEstado){
        historialEstadoDao.actualizarHistorialEstado(historialEstado)
    }

}