package com.example.gestioninventario.Dao
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.gestioninventario.model.Rack
import kotlinx.coroutines.flow.Flow

@Dao
interface RackDao {
    @Insert
    suspend fun agregarRack(rack: Rack)

    @Update
    suspend fun actualizarRack(rack: Rack)

    @Delete
    suspend fun eliminarRack(rack: Rack)

    @Query("SELECT * FROM Rack ORDER BY id")
    fun listarRack(): Flow<List<Rack>>

}