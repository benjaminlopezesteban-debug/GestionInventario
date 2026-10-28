package com.example.gestioninventario.model.estado

sealed class EstadoSolicitudCompra {
    object CompraPendiente : EstadoSolicitudCompra()
    data class CompraRealizada(val mensaje: String) : EstadoSolicitudCompra()
    data class CompraRechazada(val motivo: String) : EstadoSolicitudCompra()

    companion object {
        val TODOS: List<EstadoSolicitudCompra> = listOf(CompraPendiente)
    }
}