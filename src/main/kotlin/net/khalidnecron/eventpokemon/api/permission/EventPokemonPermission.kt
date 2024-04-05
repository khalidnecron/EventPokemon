package net.khalidnecron.eventpokemon.api.permission

import com.cobblemon.mod.common.api.permission.Permission
import com.cobblemon.mod.common.api.permission.PermissionLevel
import net.khalidnecron.eventpokemon.EventPokemon
import net.minecraft.util.Identifier

data class EventPokemonPermission(
    private val node: String,
    override val level: PermissionLevel
) : Permission {

    override val identifier = Identifier(EventPokemon.MOD_ID ,this.node)

    override val literal = "${EventPokemon.MOD_ID}.${this.node}"
}
