rootProject.name = "EventPokemon"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        gradlePluginPortal()
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "6.1.0"
}


