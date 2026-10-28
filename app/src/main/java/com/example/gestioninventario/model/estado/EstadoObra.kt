package com.example.gestioninventario.model.estado

sealed class EstadoObra {
    object EnProceso : EstadoObra()
    object Finalizada : EstadoObra()

    companion object {
        val TODOS: List<EstadoObra> = listOf(EnProceso, Finalizada)
    }/*Que hace el companion object*/

}