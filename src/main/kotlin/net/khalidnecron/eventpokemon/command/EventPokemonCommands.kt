package net.khalidnecron.eventpokemon.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource

object EventPokemonCommands {


    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        CreatePokeEventCommand.register(dispatcher)
        ClaimPokeEventCommand.register(dispatcher)
    }
}