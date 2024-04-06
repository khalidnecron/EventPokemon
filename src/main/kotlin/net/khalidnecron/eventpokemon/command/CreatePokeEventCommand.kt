package net.khalidnecron.eventpokemon.command

import com.cobblemon.mod.common.command.argument.PartySlotArgumentType
import com.cobblemon.mod.common.util.player
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.khalidnecron.eventpokemon.util.EVs
import net.khalidnecron.eventpokemon.util.IVs
import net.khalidnecron.eventpokemon.util.PokeEvents
import net.khalidnecron.eventpokemon.util.Pokemons
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity

object CreatePokeEventCommand {

    private const val NAME = "createpokeevent"
    private const val PLAYER = "player"
    private const val SLOT = "slot"


    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val createPokeEventCommand = CommandManager.literal(NAME)
            .requires { it.hasPermissionLevel(2) }
              .then(CommandManager.argument(PLAYER, EntityArgumentType.player())
                  .then(CommandManager.argument(SLOT, PartySlotArgumentType.partySlot())
                    .executes { execute(it, it.player()) }
                ))

        dispatcher.register(createPokeEventCommand)
    }

    private fun execute(context: CommandContext<ServerCommandSource>, player: ServerPlayerEntity): Int {
        val pokemon = PartySlotArgumentType.getPokemonOf(context, SLOT, player)

        val events = PokeEvents.getSerializedEvents()

        events.pokemons?.add(
            Pokemons(
                pokemon.nickname?.string,
                pokemon.species.toString(),
                pokemon.gender.toString(),
                pokemon.caughtBall.name.path,
                pokemon.level,
                pokemon.shiny,
                pokemon.heldItem().name.string,
                pokemon.ability.name,
                pokemon.nature.name.path,
                IVs.parse(pokemon.ivs.iterator()),
                EVs.parse(pokemon.evs.iterator()),
                pokemon.moveSet.getMoves().map { it.name },
                pokemon.benchedMoves.map { it.moveTemplate.name }
            )
        )

        val json = Json.encodeToString(events)

        PokeEvents.getEventsFile().writeText(json)

        return Command.SINGLE_SUCCESS
    }
}