package net.khalidnecron.eventpokemon.util

import kotlinx.serialization.Serializable

@Serializable
data class Pokemons(
    val name: String?,
    val species: String,
    val gender: String,
    val pokeBall: String,
    val level: Int,
    val shiny: Boolean,
    val item: String,
    val ability: String,
    val nature: String,
    val ivs: IVs,
    val evs: EVs,
    val moves: List<String>,
    val benchedMoves: List<String>
)
