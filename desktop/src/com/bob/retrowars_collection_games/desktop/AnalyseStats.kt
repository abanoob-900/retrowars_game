package com.bob.retrowars_collection_games.desktop

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.bob.retrowars_collection_games.scoring.dumpStats

class AnalyseStats : Game() {

    companion object {
        const val TAG = "Stats"
    }

    override fun create() {

        dumpStats()

        Gdx.app.exit()

    }

}
