package net.khalidnecron.eventpokemon.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object EventPokemonCommands {


    fun register(dispatcher: CommandDispatcher<ServerCommandSource>, registry: CommandRegistryAccess, environment: CommandManager.RegistrationEnvironment) {
        CreatePokeEventCommand.register(dispatcher)
        ClaimPokeEventCommand.register(dispatcher)
    }
}