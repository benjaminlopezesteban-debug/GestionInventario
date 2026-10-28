package com.example.gestioninventario.Database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.ColumnTypeConverters
import com.example.gestioninventario.Dao.DevolucionHerramientaDao
import com.example.gestioninventario.Dao.HerramientaDao
import com.example.gestioninventario.Dao.HistorialEstadoDao
import com.example.gestioninventario.Dao.MaterialDao
import com.example.gestioninventario.Dao.MovimientoBodegaDao
import com.example.gestioninventario.Dao.NotificacionDao
import com.example.gestioninventario.Dao.ObraDao
import com.example.gestioninventario.Dao.RackDao
import com.example.gestioninventario.Dao.SolicitudCompraDao
import com.example.gestioninventario.Dao.SolicitudHerramientaDao
import com.example.gestioninventario.Dao.SolicitudMaterialDao
import com.example.gestioninventario.Dao.UsuarioDao
import com.example.gestioninventario.model.DevolucionHerramienta
import com.example.gestioninventario.model.Herramienta
import com.example.gestioninventario.model.HistorialEstado
import com.example.gestioninventario.model.Material
import com.example.gestioninventario.model.MovimientoBodega
import com.example.gestioninventario.model.Notificacion
import com.example.gestioninventario.model.Obra
import com.example.gestioninventario.model.Rack
import com.example.gestioninventario.model.SolicitudCompra
import com.example.gestioninventario.model.SolicitudHerramienta
import com.example.gestioninventario.model.SolicitudMaterial
import com.example.gestioninventario.model.Usuario

@Database(
    entities = [
        Usuario::class,
        Herramienta::class,
        Material::class,
        Rack::class,
        Obra::class,
        SolicitudHerramienta::class,
        SolicitudMaterial::class,
        SolicitudCompra::class,
        DevolucionHerramienta::class,
        MovimientoBodega::class,
        HistorialEstado::class,
        Notificacion::class
    ],
    version = 3,
    exportSchema = false
)
@ColumnTypeConverters(Converters::class)
abstract class appDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun herramientaDao(): HerramientaDao
    abstract fun materialDao(): MaterialDao
    abstract fun rackDao(): RackDao
    abstract fun obraDao(): ObraDao
    abstract fun solicitudHerramientaDao(): SolicitudHerramientaDao
    abstract fun solicitudMaterialDao(): SolicitudMaterialDao
    abstract fun solicitudCompraDao(): SolicitudCompraDao
    abstract fun devolucionHerramientaDao(): DevolucionHerramientaDao
    abstract fun movimientoBodegaDao(): MovimientoBodegaDao
    abstract fun historialEstadoDao(): HistorialEstadoDao
    abstract fun notificacionDao(): NotificacionDao

    companion object {
        @Volatile
        private var INSTANCE: appDatabase? = null

        // Mantenemos getDatabes por compatibilidad y getDatabase como alias
        fun getDatabes(context: Context): appDatabase = getDatabase(context)

        fun getDatabase(context: Context): appDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    appDatabase::class.java,
                    "db_proyecto"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
