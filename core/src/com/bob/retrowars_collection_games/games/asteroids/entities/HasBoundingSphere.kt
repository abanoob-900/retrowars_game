package com.bob.retrowars_collection_games.games.asteroids.entities

import com.badlogic.gdx.math.Vector2

interface HasBoundingSphere {
    fun getPosition(): Vector2
    fun getRadius(): Float
}
