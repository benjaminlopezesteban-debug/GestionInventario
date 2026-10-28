package com.example.gestioninventario.model.estado

sealed class EstadoSolicitudMaterial {
    object Pendiente : EstadoSolicitudMaterial()
    object Aprobada : EstadoSolicitudMaterial()
    object SinStock : EstadoSolicitudMaterial()
    data class Rechazada(val motivo: String) : EstadoSolicitudMaterial()
    object Entregada : EstadoSolicitudMaterial()

    companion object {
        val TODOS: List<EstadoSolicitudMaterial> = listOf(
            Pendiente, Aprobada, SinStock, Entregada
        )
    }
}