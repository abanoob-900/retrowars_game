package com.bob.retrowars_collection_games.utils

import com.badlogic.gdx.Gdx
import com.bob.retrowars_collection_games.games.GameDetails
import com.bob.retrowars_collection_games.games.Games
import kotlin.random.Random

object Options {

    private var useVisualEffects: Boolean = prefs().getBoolean("useVisualEffects", true)

    private var playerId: Long = prefs().getLong("playerId", Random.nextLong())

    private var isMusicMuted: Boolean = prefs().getBoolean("mute", false)
    private var musicVolume: Float = prefs().getFloat("musicVolume", 1f)

    private var isSoundMuted: Boolean = prefs().getBoolean("isSoundMuted", false)
    private var soundVolume: Float = prefs().getFloat("soundVolume", 1f)

    private var softControllers: MutableMap<GameDetails, Int> = Games.all.associateWith {
        prefs().getInteger("${it.id}-softController", 0)
    }.toMutableMap()

    fun useVisualEffects(): Boolean {
        return useVisualEffects
    }

    fun setUseVisualEffects(use: Boolean) {
        prefs().putBoolean("useVisualEffects", use).flush()
        useVisualEffects = use
    }

    fun getPlayerId(): Long {
        return playerId
    }

    fun setPlayerId(value: Long) {
        prefs().putLong("playerId", value).flush()
        playerId = value
    }

    fun isMusicMuted(): Boolean {
        return isMusicMuted
    }

    fun setMusicMuted(mute: Boolean) {
        prefs()
                .putBoolean("mute", mute)
                .flush()
        this.isMusicMuted = mute
    }

    fun getMusicVolume() = musicVolume
    fun getRealMusicVolume() = if (isMusicMuted) 0f else musicVolume

    fun setMusicVolume(volume: Float) {
        prefs()
                .putBoolean("mute", volume == 0f)
                .putFloat("musicVolume", volume)
                .flush()
        this.isMusicMuted = volume == 0f
        this.musicVolume = volume
    }

    fun isSoundMuted(): Boolean {
        return isSoundMuted
    }

    fun getSoundVolume() = soundVolume
    fun getRealSoundVolume() = if (isSoundMuted) 0f else soundVolume

    fun setSoundMuted(mute: Boolean) {
        prefs()
            .putBoolean("isSoundMuted", mute)
            .flush()
        this.isSoundMuted = mute
    }

    fun setSoundVolume(volume: Float) {
        prefs()
            .putBoolean("isSoundMuted", volume == 0f)
            .putFloat("soundVolume", volume)
            .flush()
        this.isSoundMuted = volume == 0f
        this.soundVolume = volume
    }

    fun getSoftController(game: GameDetails): Int {
        return softControllers[game]!!
    }

    fun setSoftController(game: GameDetails, index: Int) {
        prefs().putInteger("${game.id}-softController", index).flush()
        softControllers[game] = index
    }

    private fun prefs() = Gdx.app.getPreferences("com.bob.retrowars_collection_games.options")

    fun forceRandomPlayerId() {
        playerId = Random.nextLong()
    }

}