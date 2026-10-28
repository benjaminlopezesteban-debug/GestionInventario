package com.example.gestioninventario.model.estado

sealed class EstadoMaterial {
    object BuenEstado : EstadoMaterial()
    object MalEstado : EstadoMaterial()
    object DadoDeBaja : EstadoMaterial()

    companion object {
        val TODOS: List<EstadoMaterial> = listOf(BuenEstado, MalEstado, DadoDeBaja)
    }
}