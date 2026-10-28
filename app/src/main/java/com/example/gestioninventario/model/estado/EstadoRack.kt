package com.example.gestioninventario.model.estado

sealed class EstadoRack {
    object Ocupado : EstadoRack()
    object Disponible : EstadoRack()

    companion object{
        val DISPONIBLES: List<EstadoRack> = listOf(Disponible, Ocupado)
    }

}