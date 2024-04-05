package net.khalidnecron.eventpokemon.command

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.moves.BenchedMove
import com.cobblemon.mod.common.api.moves.Moves
import com.cobblemon.mod.common.api.permission.PermissionLevel
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.item.CobblemonItem
import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.util.commandLang
import com.cobblemon.mod.common.util.permission
import com.cobblemon.mod.common.util.toProperties
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.khalidnecron.eventpokemon.api.permission.EventPokemonPermission
import net.khalidnecron.eventpokemon.util.PokeEvents
import net.khalidnecron.eventpokemon.util.Pokemons
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object ClaimPokeEventCommand {

    private const val NAME = "claimpokeevent"
    private const val NUMBER = "number"

    private val NEGATIVE_NUMBER_EXCEPTION = DynamicCommandExceptionType{a -> commandLang("$NAME.negative_number", a).red() }

    private val COBBLEMON_HELD_ITEMS: HashMap<String, CobblemonItem> = hashMapOf(
        Pair(CobblemonItems.ASSAULT_VEST.name.string, CobblemonItems.ASSAULT_VEST),
        Pair(CobblemonItems.BLACK_BELT.name.string, CobblemonItems.BLACK_BELT),
        Pair(CobblemonItems.BLACK_GLASSES.name.string, CobblemonItems.BLACK_GLASSES),
        Pair(CobblemonItems.BLACK_SLUDGE.name.string, CobblemonItems.BLACK_SLUDGE),
        Pair(CobblemonItems.CHARCOAL.name.string, CobblemonItems.CHARCOAL),
        Pair(CobblemonItems.CHOICE_BAND.name.string, CobblemonItems.CHOICE_BAND),
        Pair(CobblemonItems.CHOICE_SCARF.name.string, CobblemonItems.CHOICE_SCARF),
        Pair(CobblemonItems.CHOICE_SPECS.name.string, CobblemonItems.CHOICE_SPECS),
        Pair(CobblemonItems.CLEANSE_TAG.name.string, CobblemonItems.CLEANSE_TAG),
        Pair(CobblemonItems.DESTINY_KNOT.name.string, CobblemonItems.DESTINY_KNOT),
        Pair(CobblemonItems.DRAGON_FANG.name.string, CobblemonItems.DRAGON_FANG),
        Pair(CobblemonItems.EVERSTONE.name.string, CobblemonItems.EVERSTONE),
        Pair(CobblemonItems.EXP_SHARE.name.string, CobblemonItems.EXP_SHARE),
        Pair(CobblemonItems.FAIRY_FEATHER.name.string, CobblemonItems.FAIRY_FEATHER),
        Pair(CobblemonItems.FLAME_ORB.name.string, CobblemonItems.FLAME_ORB),
        Pair(CobblemonItems.FOCUS_BAND.name.string, CobblemonItems.FOCUS_BAND),
        Pair(CobblemonItems.HARD_STONE.name.string, CobblemonItems.HARD_STONE),
        Pair(CobblemonItems.HEAVY_DUTY_BOOTS.name.string, CobblemonItems.HEAVY_DUTY_BOOTS),
        Pair(CobblemonItems.LEFTOVERS.name.string, CobblemonItems.LEFTOVERS),
        Pair(CobblemonItems.LIFE_ORB.name.string, CobblemonItems.LIFE_ORB),
        Pair(CobblemonItems.LUCKY_EGG.name.string, CobblemonItems.LUCKY_EGG),
        Pair(CobblemonItems.MAGNET.name.string, CobblemonItems.MAGNET),
        Pair(CobblemonItems.MIRACLE_SEED.name.string, CobblemonItems.MIRACLE_SEED),
        Pair(CobblemonItems.MUSCLE_BAND.name.string, CobblemonItems.MUSCLE_BAND),
        Pair(CobblemonItems.MYSTIC_WATER.name.string, CobblemonItems.MYSTIC_WATER),
        Pair(CobblemonItems.NEVER_MELT_ICE.name.string, CobblemonItems.NEVER_MELT_ICE),
        Pair(CobblemonItems.POISON_BARB.name.string, CobblemonItems.POISON_BARB),
        Pair(CobblemonItems.POWER_ANKLET.name.string, CobblemonItems.POWER_ANKLET),
        Pair(CobblemonItems.POWER_BAND.name.string, CobblemonItems.POWER_BAND),
        Pair(CobblemonItems.POWER_BELT.name.string, CobblemonItems.POWER_BELT),
        Pair(CobblemonItems.POWER_BRACER.name.string, CobblemonItems.POWER_BRACER),
        Pair(CobblemonItems.POWER_LENS.name.string, CobblemonItems.POWER_LENS),
        Pair(CobblemonItems.POWER_WEIGHT.name.string, CobblemonItems.POWER_WEIGHT),
        Pair(CobblemonItems.QUICK_CLAW.name.string, CobblemonItems.QUICK_CLAW),
        Pair(CobblemonItems.ROCKY_HELMET.name.string, CobblemonItems.ROCKY_HELMET),
        Pair(CobblemonItems.SAFETY_GOGGLES.name.string, CobblemonItems.SAFETY_GOGGLES),
        Pair(CobblemonItems.SHARP_BEAK.name.string, CobblemonItems.SHARP_BEAK),
        Pair(CobblemonItems.SILK_SCARF.name.string, CobblemonItems.SILK_SCARF),
        Pair(CobblemonItems.SILVER_POWDER.name.string, CobblemonItems.SILVER_POWDER),
        Pair(CobblemonItems.SOFT_SAND.name.string, CobblemonItems.SOFT_SAND),
        Pair(CobblemonItems.SPELL_TAG.name.string, CobblemonItems.SPELL_TAG),
        Pair(CobblemonItems.SMOKE_BALL.name.string, CobblemonItems.SMOKE_BALL),
        Pair(CobblemonItems.TOXIC_ORB.name.string, CobblemonItems.TOXIC_ORB),
        Pair(CobblemonItems.TWISTED_SPOON.name.string, CobblemonItems.TWISTED_SPOON)
    )

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val claimPokeEventCommand = CommandManager.literal(NAME)
            .permission(EventPokemonPermission("command.claimpokeevent", PermissionLevel.NONE))
            .then(CommandManager.argument("number", IntegerArgumentType.integer())
                .executes { execute(it) }
            )
        dispatcher.register(claimPokeEventCommand)
    }

    private fun execute(context: CommandContext<ServerCommandSource>): Int {
        val number = IntegerArgumentType.getInteger(context, NUMBER)

        if(number < 0) {
            throw NEGATIVE_NUMBER_EXCEPTION.create(number)
        }

        val player = context.source.playerOrThrow
        val events = PokeEvents.getSerializedEvents()

        var pokeEvent: Pokemons? = null

        for(i in 0..<number) {
            pokeEvent = events.pokemons?.get(i)
        }

        if(pokeEvent == null) {
            return Command.SINGLE_SUCCESS
        }

        var propertiesString = "${pokeEvent.species} pokeball=${pokeEvent.pokeBall} level=${pokeEvent.level} nature=${pokeEvent.nature} hp_iv=${pokeEvent.ivs.hp} attack_iv=${pokeEvent.ivs.attack} defence_iv=${pokeEvent.ivs.defence} special_attack_iv=${pokeEvent.ivs.specialAttack} special_defence_iv=${pokeEvent.ivs.specialDefence} speed_iv=${pokeEvent.ivs.speed} ability=${pokeEvent.ability} "

        if(pokeEvent.shiny) {
            propertiesString += "shiny "
        }

        if(pokeEvent.gender != Gender.GENDERLESS.toString()) {
            propertiesString += "gender=${pokeEvent.gender.lowercase()} "
        }

        if(pokeEvent.evs.hp > 0) {
            propertiesString += "hp_ev=${pokeEvent.evs.hp} "
        }

        if(pokeEvent.evs.attack > 0) {
            propertiesString += "attack_ev=${pokeEvent.evs.attack} "
        }

        if(pokeEvent.evs.defence > 0) {
            propertiesString += "defence_ev=${pokeEvent.evs.defence} "
        }

        if(pokeEvent.evs.specialAttack > 0) {
            propertiesString += "special_attack_ev=${pokeEvent.evs.specialAttack} "
        }

        if(pokeEvent.evs.specialDefence > 0) {
            propertiesString += "special_defence_ev=${pokeEvent.evs.specialDefence} "
        }

        if(pokeEvent.evs.speed > 0) {
            propertiesString += "speed_ev=${pokeEvent.evs.speed} "
        }

        val pokemonProperties = propertiesString.toProperties()

        if (pokemonProperties.species == null) {
            player.sendMessage(commandLang("${NAME}.nospecies").red())
            return Command.SINGLE_SUCCESS
        }

        if(pokeEvent.name != null) {
            pokemonProperties.nickname = Text.literal(pokeEvent.name)
        }

        val pokemon = pokemonProperties.create()

        for(m in pokeEvent.moves) {
            val move = Moves.getByName(m) ?: break

            if (pokemon.moveSet.getMoves().any { it.template == move } || pokemon.benchedMoves.any { it.moveTemplate == move }) {
                continue
            }

            if (pokemon.moveSet.hasSpace()) {
                pokemon.moveSet.add(move.create())
            }
            else {
                pokemon.benchedMoves.add(BenchedMove(move, 0))
            }
        }

        if (COBBLEMON_HELD_ITEMS.containsKey(pokeEvent.item)) {
           pokemon.swapHeldItem(COBBLEMON_HELD_ITEMS[pokeEvent.item]!!.asItem().defaultStack)
        }

        val party = Cobblemon.storage.getParty(player)

        party.add(pokemon)

        return Command.SINGLE_SUCCESS
    }
}