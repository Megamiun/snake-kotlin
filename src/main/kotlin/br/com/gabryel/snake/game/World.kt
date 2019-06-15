package br.com.gabryel.snake.game

import br.com.gabryel.snake.game.Direction.*
import kotlin.random.Random

data class World(
    val height: Int,
    val width: Int,
    val positions: List<Point>,
    val fruit: Point,
    val status: Status = Status.ALIVE
) {

    private val cells = height * width

    fun move(direction: Direction): World {
        if (status != Status.ALIVE)
            return this

        val newHead = getNewHead(direction)
        
        if (newHead.isOutsideBounds() || newHead.isInConflict()) {
            return copy(status = Status.DEAD)
        }

        if (newHead.isOnFruit()) {
            val newPositions = positions + newHead

            val newStatus = if (positions.size == cells) Status.WIN else Status.ALIVE

            return copy(positions = newPositions, fruit = createNewFruit(newPositions), status = newStatus)
        }

        return copy(positions = positions.drop(1) + newHead)
    }

    fun onPosition(x: Int, y: Int) =
        when (Point(x, y)) {
            in positions -> Item.SNAKE
            fruit -> Item.FRUIT
            else -> Item.VOID
        }

    private fun Point.isOnFruit() = this == fruit

    private fun Point.isInConflict() = this in positions

    private fun Point.isOutsideBounds() = x !in 0 until width || y !in 0 until height

    private fun getNewHead(direction: Direction) = positions.last() to direction

    private tailrec fun createNewFruit(excluding: List<Point>): Point {
        val x = Random.nextInt(0, width - 1)
        val y = Random.nextInt(0, height - 1)
        val fruit = Point(x, y)

        return if (fruit !in excluding) {
            fruit
        } else {
            createNewFruit(excluding)
        }
    }
}

data class Point(val x: Int, val y: Int) {
    infix fun to(direction: Direction) =
        when (direction) {
            DOWN -> copy(y = y - 1)
            UP -> copy(y = y + 1)
            LEFT -> copy(x = x - 1)
            RIGHT -> copy(x = x + 1)
        }
}

enum class Direction {
    DOWN, UP, LEFT, RIGHT
}

enum class Status {
    DEAD, ALIVE, WIN
}

enum class Item {
    SNAKE, FRUIT, VOID
}