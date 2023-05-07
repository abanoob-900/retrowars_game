package com.bob.retrowars_collection_games.games.missilecommand.entities

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.bob.retrowars_collection_games.games.asteroids.entities.HasBoundingSphere
import com.bob.retrowars_collection_games.ui.ENEMY_ATTACK_COLOUR

class FriendlyMissile(startTurret: Turret, target: Vector2): Missile(
    startTurret.missileSpeed,
    Color(0.2f, 1f, 0.2f, 1f),
    Color(0.2f, 1f, 0.2f, 0.5f),
    startTurret.position.cpy().add(0f, Turret.HEIGHT),
    target
)

class EnemyMissile(speed: Float, start: Vector2, val targetCity: City): Missile(
    speed,
    Color.WHITE,
    Color.WHITE,
    start,
    targetCity.position.cpy().add(0f, City.HEIGHT)
)

abstract class Missile(speed: Float, val missileColour: Color, val trailColour: Color, protected val start: Vector2, val target: Vector2) {

    companion object {

        private const val SIZE = 2f
        const val POINTS = 6000

        fun renderBulk(camera: Camera, r: ShapeRenderer, missiles: List<Missile>, toHighlight: Set<Missile> = emptySet()) {
            r.projectionMatrix = camera.combined
            r.begin(ShapeRenderer.ShapeType.Line)
            missiles.forEach {
                r.color = if (toHighlight.contains(it)) { ENEMY_ATTACK_COLOUR } else { it.trailColour }
                r.line(it.start, it.position)

                r.color = if (toHighlight.contains(it)) { ENEMY_ATTACK_COLOUR } else { it.missileColour }
                r.rect(it.position.x - SIZE / 2, it.position.y - SIZE / 2, SIZE, SIZE)
            }
            r.end()
        }

    }

    protected val position = start.cpy()
    private val velocity = target.cpy().sub(start).nor().scl(speed)
    private val distance2 = target.cpy().sub(start).len2()

    fun update(delta: Float) {
        position.mulAdd(velocity, delta)
    }

    fun isColliding(entity: HasBoundingSphere): Boolean {
        val distance2 = entity.getPosition().dst2(this.position)
        val collideDistance = (SIZE / 2 + entity.getRadius())

        return distance2 < collideDistance * collideDistance
    }

    fun hasReachedDestination(): Boolean {
        return position.dst2(start) >= distance2
    }

}