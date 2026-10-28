package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Material
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {

    @Insert
    suspend fun agregarMaterial(material: Material)

    @Update
    suspend fun actualizarMaterial(material: Material)

    @Delete
    suspend fun eliminarMaterial(material: Material)

    @Query("SELECT * FROM Material ORDER BY id")
    fun listarMateriales(): Flow<List<Material>>

    @Query("SELECT * FROM Material WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Int): Material?

}