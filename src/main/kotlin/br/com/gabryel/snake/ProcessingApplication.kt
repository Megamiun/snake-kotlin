package br.com.gabryel.snake

import br.com.gabryel.snake.game.*
import processing.core.PApplet
import java.awt.event.KeyEvent
import kotlin.concurrent.fixedRateTimer

class Snake(private var currentWorld: World): PApplet() {

    private val scale = 50

    private val scaleFloat = scale.toFloat()

    private var currentDirection = Direction.UP

    override fun settings() {
        size(currentWorld.width * scale, currentWorld.height * scale)
    }

    override fun setup() {
        frameRate(30F)

        fixedRateTimer(period = 200) {
            if (looping) {
                currentWorld = currentWorld.move(currentDirection)
            }
        }
    }

    override fun draw() {
        clear()
        currentWorld.render()
    }

    override fun keyPressed() {
        when (keyCode) {
            KeyEvent.VK_RIGHT -> currentDirection = Direction.RIGHT
            KeyEvent.VK_LEFT -> currentDirection = Direction.LEFT
            KeyEvent.VK_DOWN -> currentDirection = Direction.DOWN
            KeyEvent.VK_UP -> currentDirection = Direction.UP
            KeyEvent.VK_SPACE -> togglePause()
        }
    }

    private fun togglePause() = if (looping) noLoop() else loop()

    private fun World.render() {
        val ys = 0 until height
        (0 until width).forEach { x ->
            ys.forEach { y ->
                paint(x, y, onPosition(x, y))
            }
        }

        if (status == Status.WIN) {
            printCentered("YOU WON", blue = 255F)
        }

        if (status == Status.DEAD) {
            printCentered("YOU DIED", red = 255F)
        }
    }

    private fun printCentered(message: String, blue: Float = 0F, green: Float = 0F, red: Float = 0F) {
        textSize(width.toFloat() / 10)
        fill(red, green, blue)
        textAlign(CENTER, CENTER)
        text(message, width.toFloat() / 2, height.toFloat() / 2)
    }

    private fun paint(x: Int, y: Int, type: Item) {
        when (type) {
            Item.SNAKE -> fill(255)
            Item.FRUIT -> fill(255F, 0F, 0F)
            Item.VOID -> return
        }

        rect(x * scaleFloat, ((currentWorld.height - 1) - y) * scaleFloat, scaleFloat, scaleFloat)
    }
}

fun main() {
    val snake = Snake(World(15, 10, listOf(Point(5, 5)), Point(8, 2)))

    PApplet.runSketch(arrayOf("Snake"), snake)
}