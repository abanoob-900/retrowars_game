package com.bob.retrowars_collection_games.games.breakout

import com.bob.retrowars_collection_games.audio.SoundLibrary

class BreakoutSoundLibrary: SoundLibrary(
    mapOf(
        "breakout_hit_floor" to "breakout_hit_floor.ogg",
        "breakout_hit_paddle" to "breakout_hit_paddle.ogg",
        "breakout_hit_brick" to "breakout_hit_brick.ogg",
        "breakout_final_life_lost" to "breakout_final_life_lost.ogg",
    )
) {

    fun hitFloor() = play("breakout_hit_floor")
    fun hitPaddle() = play("breakout_hit_paddle")
    fun hitBrick() = play("breakout_hit_brick")
    fun finalLifeLost() = play("breakout_final_life_lost")

}
