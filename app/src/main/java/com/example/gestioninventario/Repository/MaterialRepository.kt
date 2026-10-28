package com.example.gestioninventario.Repository

import com.example.gestioninventario.Dao.MaterialDao
import com.example.gestioninventario.model.Material
import kotlinx.coroutines.flow.Flow

class MaterialRepository(private val materialDao : MaterialDao) {

    val material: Flow<List<Material>> = materialDao.listarMateriales()

    suspend fun materialAlmacenar(material: Material){
        materialDao.agregarMaterial(material)
    }

    suspend fun eliminarMaterial(material: Material){
        materialDao.eliminarMaterial(material)
    }

    suspend fun actualizarMaterial(material: Material){
        materialDao.actualizarMaterial(material)
    }


}