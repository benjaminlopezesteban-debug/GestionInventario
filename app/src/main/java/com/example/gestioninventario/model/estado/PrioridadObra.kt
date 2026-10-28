package com.example.gestioninventario.model.estado

sealed class PrioridadObra {

    object Baja : PrioridadObra()

    object Media : PrioridadObra()

    object Alta : PrioridadObra()

    companion object {
        val TODOS: List<PrioridadObra> = listOf(Baja, Media, Alta)
    }


}