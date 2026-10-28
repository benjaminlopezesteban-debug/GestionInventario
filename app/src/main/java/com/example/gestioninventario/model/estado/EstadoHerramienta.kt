package com.example.gestioninventario.model.estado

sealed class EstadoHerramienta {
    object Disponible : EstadoHerramienta()
    object Prestada : EstadoHerramienta()
    object MalEstado : EstadoHerramienta()
    object DadaDeBaja : EstadoHerramienta()

    companion object {
        val TODOS: List<EstadoHerramienta> = listOf(Disponible, Prestada, MalEstado, DadaDeBaja)
    }
}