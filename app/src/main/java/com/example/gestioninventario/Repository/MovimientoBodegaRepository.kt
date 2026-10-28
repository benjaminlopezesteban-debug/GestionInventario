package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.MovimientoBodegaDao
import com.example.gestioninventario.model.MovimientoBodega
import kotlinx.coroutines.flow.Flow

class MovimientoBodegaRepository(
    private val movimientoBodegaDao: MovimientoBodegaDao
) {

    val movimientos: Flow<List<MovimientoBodega>> = movimientoBodegaDao.listarMovimientos()

    suspend fun movimientoAlmacenar(movimiento: MovimientoBodega) {
        return movimientoBodegaDao.agregarMovimiento(movimiento)
    }

    suspend fun eliminarMovimiento(movimiento: MovimientoBodega) {
        movimientoBodegaDao.eliminarMovimiento(movimiento)
    }

    suspend fun actualizarMovimiento(movimiento: MovimientoBodega) {
        movimientoBodegaDao.actualizarMovimiento(movimiento)
    }

    suspend fun obtenerPorId(id: Int): MovimientoBodega? {
        return movimientoBodegaDao.obtenerPorId(id)
    }
}