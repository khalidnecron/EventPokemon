package net.khalidnecron.eventpokemon

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.khalidnecron.eventpokemon.command.CreatePokeEventCommand
import net.khalidnecron.eventpokemon.command.EventPokemonCommands
import net.khalidnecron.eventpokemon.util.PokeEvents
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object EventPokemon : ModInitializer {

    val LOGGER: Logger = LogManager.getLogger()

    const val MOD_ID = "eventpokemon"

    override fun onInitialize() {
        LOGGER.info("$MOD_ID has initialize")

        PokeEvents.createDirAndEventsFile()

        CommandRegistrationCallback.EVENT.register { dispatcher, registry, environment -> EventPokemonCommands.register(dispatcher, registry, environment) }
    }
}