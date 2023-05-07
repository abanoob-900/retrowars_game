package com.bob.retrowars_collection_games.games.snake

import com.bob.retrowars_collection_games.audio.SoundLibrary

class SnakeSoundLibrary: SoundLibrary(
    mapOf(
        "snake_tick" to "snake_tick.ogg",
        "snake_eat" to "snake_eat.ogg",
        "snake_hit" to "snake_hit.ogg",
    )
) {

    fun tick() = play("snake_tick")
    fun eat() = play("snake_eat")
    fun hit() = play("snake_hit")

}