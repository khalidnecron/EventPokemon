package net.khalidnecron.eventpokemon.util

import kotlinx.serialization.Serializable

@Serializable
data class Events(
    val pokemons: MutableList<Pokemons>?
)
