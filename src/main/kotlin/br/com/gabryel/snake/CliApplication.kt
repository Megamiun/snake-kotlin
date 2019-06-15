package br.com.gabryel.snake

import br.com.gabryel.snake.game.*
import java.util.*
import kotlin.concurrent.thread

const val clearSequence = "${27.toChar()}[H${27.toChar()}[2J\n"

fun main() {

    var currentWorld = World(15, 10, listOf(Point(3, 5)), Point(8, 2))
    var currentDirection = Direction.UP

    thread {
        Thread.sleep(2000)
        while (true) {
            currentWorld = currentWorld.move(currentDirection)
            Thread.sleep(1000 / 2)
        }
    }

    thread {
        Scanner(System.`in`).use { scanner ->
            while (true) {
                // TODO("Learn to react to user input")
                currentDirection =
                    when (scanner.nextLine()?.firstOrNull()) {
                        'a' -> Direction.LEFT
                        's' -> Direction.DOWN
                        'd' -> Direction.RIGHT
                        'w' -> Direction.UP
                        else -> currentDirection
                    }
            }
        }
    }

    while (true) {
        currentWorld.print()
        Thread.sleep(1000 / 20)
    }
}

private fun World.print() {
    print(clearSequence)

    if (status == Status.DEAD) {
        printMessage("YOU DIED")
    }

    if (status == Status.WIN) {
        printMessage("YOU  WON")
    }

    val board = (0 until height).reversed().joinToString("\n") { y ->
        (0 until width).joinToString("") { x ->
            when (onPosition(x, y)) {
                Item.SNAKE -> "+"
                Item.FRUIT -> "*"
                Item.VOID -> "."
            }
        }
    }

    println(board)
}

private fun printMessage(message: String) {
    println("\t--------")
    println("\t$message")
    println("\t--------")
}