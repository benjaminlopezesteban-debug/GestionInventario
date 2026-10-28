package com.example.gestioninventario.Database

import androidx.room3.ColumnTypeConverter
import com.example.gestioninventario.model.estado.EstadoHerramienta
import com.example.gestioninventario.model.estado.EstadoMaterial
import com.example.gestioninventario.model.estado.EstadoObra
import com.example.gestioninventario.model.estado.EstadoRack
import com.example.gestioninventario.model.estado.EstadoSolicitudCompra
import com.example.gestioninventario.model.estado.EstadoSolicitudHerramienta
import com.example.gestioninventario.model.estado.EstadoSolicitudMaterial
import com.example.gestioninventario.model.estado.PrioridadObra
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

object Converters {

    // BigDecimal
    @ColumnTypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @ColumnTypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    // LocalDate
    @ColumnTypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @ColumnTypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // LocalDateTime
    @ColumnTypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    @ColumnTypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    // EstadoHerramienta
    @ColumnTypeConverter
    fun fromEstadoHerramienta(estado: EstadoHerramienta?): String? = when (estado) {
        EstadoHerramienta.Disponible -> "Disponible"
        EstadoHerramienta.Prestada -> "Prestada"
        EstadoHerramienta.MalEstado -> "MalEstado"
        EstadoHerramienta.DadaDeBaja -> "DadaDeBaja"
        null -> null
    }

    @ColumnTypeConverter
    fun toEstadoHerramienta(value: String?): EstadoHerramienta? = when (value?.trim()?.uppercase()) {
        "DISPONIBLE" -> EstadoHerramienta.Disponible
        "PRESTADA" -> EstadoHerramienta.Prestada
        "MALESTADO", "MAL_ESTADO" -> EstadoHerramienta.MalEstado
        "DADADEBAJA", "DADA_DE_BAJA" -> EstadoHerramienta.DadaDeBaja
        null -> null
        else -> EstadoHerramienta.Disponible
    }

    // EstadoMaterial
    @ColumnTypeConverter
    fun fromEstadoMaterial(estado: EstadoMaterial?): String? = when (estado) {
        EstadoMaterial.BuenEstado -> "BuenEstado"
        EstadoMaterial.MalEstado -> "MalEstado"
        EstadoMaterial.DadoDeBaja -> "DadoDeBaja"
        null -> null
    }

    @ColumnTypeConverter
    fun toEstadoMaterial(value: String?): EstadoMaterial? = when (value?.trim()?.uppercase()) {
        "BUENESTADO", "BUEN_ESTADO" -> EstadoMaterial.BuenEstado
        "MALESTADO", "MAL_ESTADO" -> EstadoMaterial.MalEstado
        "DADODEBAJA", "DADO_DE_BAJA" -> EstadoMaterial.DadoDeBaja
        null -> null
        else -> EstadoMaterial.BuenEstado
    }

    // EstadoObra
    @ColumnTypeConverter
    fun fromEstadoObra(estado: EstadoObra?): String? = when (estado) {
        EstadoObra.EnProceso -> "EnProceso"
        EstadoObra.Finalizada -> "Finalizada"
        null -> null
    }

    @ColumnTypeConverter
    fun toEstadoObra(value: String?): EstadoObra? = when (value?.trim()?.uppercase()) {
        "ENPROCESO", "EN_PROCESO" -> EstadoObra.EnProceso
        "FINALIZADA" -> EstadoObra.Finalizada
        null -> null
        else -> EstadoObra.EnProceso
    }

    // PrioridadObra
    @ColumnTypeConverter
    fun fromPrioridadObra(prioridad: PrioridadObra?): String? = when (prioridad) {
        PrioridadObra.Baja -> "Baja"
        PrioridadObra.Media -> "Media"
        PrioridadObra.Alta -> "Alta"
        null -> null
    }

    @ColumnTypeConverter
    fun toPrioridadObra(value: String?): PrioridadObra? = when (value?.trim()?.uppercase()) {
        "BAJA" -> PrioridadObra.Baja
        "MEDIA" -> PrioridadObra.Media
        "ALTA" -> PrioridadObra.Alta
        null -> null
        else -> PrioridadObra.Media
    }

    // EstadoSolicitudHerramienta
    @ColumnTypeConverter
    fun fromEstadoSolicitudHerramienta(estado: EstadoSolicitudHerramienta?): String? = when (estado) {
        EstadoSolicitudHerramienta.Pendiente -> "PENDIENTE"
        EstadoSolicitudHerramienta.Aprobada -> "APROBADA"
        EstadoSolicitudHerramienta.SinStock -> "SIN_STOCK"
        is EstadoSolicitudHerramienta.Rechazada -> "RECHAZADA:${estado.motivo}"
        EstadoSolicitudHerramienta.Entregada -> "ENTREGADA"
        EstadoSolicitudHerramienta.Devuelta -> "DEVUELTA"
        EstadoSolicitudHerramienta.FueraDePlazo -> "FUERA_DE_PLAZO"
        null -> null
    }

    @ColumnTypeConverter
    fun toEstadoSolicitudHerramienta(value: String?): EstadoSolicitudHerramienta? {
        if (value == null) return null
        val upper = value.trim().uppercase()
        return when {
            upper == "PENDIENTE" -> EstadoSolicitudHerramienta.Pendiente
            upper == "APROBADA" -> EstadoSolicitudHerramienta.Aprobada
            upper in listOf("SIN_STOCK", "SINSTOCK", "SIN STOCK") -> EstadoSolicitudHerramienta.SinStock
            upper.startsWith("RECHAZADA:") -> {
                val motivo = value.substringAfter("RECHAZADA:").trim()
                EstadoSolicitudHerramienta.Rechazada(motivo)
            }
            upper.startsWith("RECHAZADA") -> EstadoSolicitudHerramienta.Rechazada("Sin motivo")
            upper == "ENTREGADA" -> EstadoSolicitudHerramienta.Entregada
            upper == "DEVUELTA" -> EstadoSolicitudHerramienta.Devuelta
            upper in listOf("FUERA_DE_PLAZO", "FUERADEPLAZO", "FUERA DE PLAZO") -> EstadoSolicitudHerramienta.FueraDePlazo
            else -> EstadoSolicitudHerramienta.Pendiente
        }
    }

    // EstadoSolicitudMaterial
    @ColumnTypeConverter
    fun fromEstadoSolicitudMaterial(estado: EstadoSolicitudMaterial?): String? = when (estado) {
        EstadoSolicitudMaterial.Pendiente -> "PENDIENTE"
        EstadoSolicitudMaterial.Aprobada -> "APROBADA"
        EstadoSolicitudMaterial.SinStock -> "SIN_STOCK"
        is EstadoSolicitudMaterial.Rechazada -> "RECHAZADA:${estado.motivo}"
        EstadoSolicitudMaterial.Entregada -> "ENTREGADA"
        null -> null
    }

    @ColumnTypeConverter
    fun toEstadoSolicitudMaterial(value: String?): EstadoSolicitudMaterial? {
        if (value == null) return null
        val upper = value.trim().uppercase()
        return when {
            upper == "PENDIENTE" -> EstadoSolicitudMaterial.Pendiente
            upper == "APROBADA" -> EstadoSolicitudMaterial.Aprobada
            upper in listOf("SIN_STOCK", "SINSTOCK", "SIN STOCK") -> EstadoSolicitudMaterial.SinStock
            upper.startsWith("RECHAZADA:") -> {
                val motivo = value.substringAfter("RECHAZADA:").trim()
                EstadoSolicitudMaterial.Rechazada(motivo)
            }
            upper.startsWith("RECHAZADA") -> EstadoSolicitudMaterial.Rechazada("Sin motivo")
            upper == "ENTREGADA" -> EstadoSolicitudMaterial.Entregada
            else -> EstadoSolicitudMaterial.Pendiente
        }
    }

    // EstadoSolicitudCompra
    @ColumnTypeConverter
    fun fromEstadoSolicitudCompra(estado: EstadoSolicitudCompra?): String? = when (estado) {
        EstadoSolicitudCompra.CompraPendiente -> "COMPRA_PENDIENTE"
        is EstadoSolicitudCompra.CompraRealizada -> "COMPRA_REALIZADA:${estado.mensaje}"
        is EstadoSolicitudCompra.CompraRechazada -> "COMPRA_RECHAZADA:${estado.motivo}"
        null -> null
    }

    @ColumnTypeConverter
    fun toEstadoSolicitudCompra(value: String?): EstadoSolicitudCompra? {
        if (value == null) return null
        val upper = value.trim().uppercase()
        return when {
            upper in listOf("COMPRAPENDIENTE", "COMPRA_PENDIENTE") -> EstadoSolicitudCompra.CompraPendiente
            upper.startsWith("COMPRA_REALIZADA:") -> {
                val msg = value.substringAfter("COMPRA_REALIZADA:").trim()
                EstadoSolicitudCompra.CompraRealizada(msg)
            }
            upper.startsWith("COMPRA_RECHAZADA:") -> {
                val motivo = value.substringAfter("COMPRA_RECHAZADA:").trim()
                EstadoSolicitudCompra.CompraRechazada(motivo)
            }
            upper == "COMPRA_REALIZADA" -> EstadoSolicitudCompra.CompraRealizada("")
            upper == "COMPRA_RECHAZADA" -> EstadoSolicitudCompra.CompraRechazada("")
            else -> EstadoSolicitudCompra.CompraPendiente
        }
    }
}
