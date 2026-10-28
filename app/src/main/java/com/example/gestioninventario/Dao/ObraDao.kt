package com.example.gestioninventario.Dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.Obra
import kotlinx.coroutines.flow.Flow

@Dao
interface ObraDao {

    @Insert
    suspend fun agregarObra(obraDao: Obra)

    @Update
    suspend  fun actualizarHerramienta(obraDao: Obra)

    @Delete
    suspend fun eliminarHerramienta(obraDao: Obra)

    @Query("SELECT * FROM Obra ORDER BY id")
    fun listarObras(): Flow<List<Obra>>

    @Query("SELECT * FROM Obra WHERE id = :idObra")
    suspend fun obtenerPorId(idObra: Int): Obra?
}