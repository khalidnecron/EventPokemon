package net.khalidnecron.eventpokemon.util

import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.io.File

object PokeEvents {
    private val fabricConfigDir = FabricLoader.getInstance().configDir
    private val configDir = "$fabricConfigDir/eventpokemon"

    fun createDirAndEventsFile() {
        if(!File("$configDir/events.json").exists()) {
           File(configDir).mkdir()
           File("$configDir/events.json").createNewFile()
           File("$configDir/events.json").writeText("{\n\"pokemons\":[]\n}")
        }
    }

    fun getEventsFile(): File {
        return File("$configDir/events.json")
    }

    fun getSerializedEvents(): Events {

        val text = getEventsFile().readText()

        if(text != "") {
            val events = Json.decodeFromString<Events>(text)
            return events
        }

        return Events(mutableListOf())
    }
}