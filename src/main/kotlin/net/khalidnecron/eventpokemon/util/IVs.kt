package net.khalidnecron.eventpokemon.util

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import kotlinx.serialization.Serializable

@Serializable
data class IVs(
    val hp: Int,
    val attack: Int,
    val defence: Int,
    val specialAttack: Int,
    val specialDefence: Int,
    val speed: Int
) {
    companion object {
        fun parse(it: Iterator<MutableMap.MutableEntry<Stat, Int>>): IVs {
            return IVs(it.next().value, it.next().value, it.next().value, it.next().value, it.next().value, it.next().value)
        }
    }
}
