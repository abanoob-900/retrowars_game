package com.bob.retrowars_collection_games.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.bob.retrowars_collection_games.RetrowarsGame
import com.bob.retrowars_collection_games.utils.DesktopPlatform

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        if (arg.contains("--stats")) {

            val config = LwjglApplicationConfiguration()
            LwjglApplication(AnalyseStats(), config)

        } else {

            val verbose = arg.contains("--verbose")
            val randomAvatar = arg.contains("--force-random-avatar")

            val config = LwjglApplicationConfiguration()
            config.title = "Super Retro Mega Wars ${arg.joinToString(" ")}"
            LwjglApplication(RetrowarsGame(DesktopPlatform(), verbose, randomAvatar), config)

        }
    }
}