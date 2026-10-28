package com.example.gestioninventario.model.estado

    sealed class EstadoSolicitudHerramienta(val valor: String) {
        object Pendiente : EstadoSolicitudHerramienta("Pendiente")
        object Aprobada : EstadoSolicitudHerramienta("Aprobada")
        object SinStock : EstadoSolicitudHerramienta("Sin Stock")
        data class Rechazada(val motivo: String) : EstadoSolicitudHerramienta("Rechazada: $motivo")
        object Entregada : EstadoSolicitudHerramienta("Entregada")
        object Devuelta : EstadoSolicitudHerramienta("Devuelta")
        object FueraDePlazo : EstadoSolicitudHerramienta("Fuera de Plazo")

    companion object {
        // Rechazada queda fuera: requiere motivo
        val TODOS: List<EstadoSolicitudHerramienta> = listOf(
            Pendiente, Aprobada, SinStock, Entregada, Devuelta, FueraDePlazo
        )
    }
}