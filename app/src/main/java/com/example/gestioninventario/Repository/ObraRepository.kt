package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.ObraDao
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.Obra
import kotlinx.coroutines.flow.Flow

class ObraRepository(private val obraDao: ObraDao) {

    val obra: Flow<List<Obra>> = obraDao.listarObras()

    suspend fun AlmacenarObra(obra: Obra){
        obraDao.agregarObra(obra)
    }

    suspend fun eliminarobra(obra: Obra){
        obraDao.eliminarHerramienta(obra)
    }

    suspend fun actualizarObra(obra: Obra){
        obraDao.actualizarHerramienta(obra)
    }
}