package day03

import models.Day

/**
 * I was relentlessly shamed for using Regex in my JS solution...
 */

class Day03: Day("src/day03/day03.txt") {
    override fun doPart1(): Int {
        val context = ExecutionContext(onlyMuls = true)
        return runProgram(context)
    }

    override fun doPart2(): Int {
        val context = ExecutionContext(onlyMuls = false)
        return runProgram(context)
    }

    private fun runProgram(context: ExecutionContext): Int {
        input.readText().toCharArray().forEach { char ->
            TOKEN.parse(char).process(char, context)
        }
        return context.sum
    }

    enum class COMMAND {
        MUL {
            override fun execute(context: ExecutionContext) {
                if (context.enabled) {
                    context.sum += (context.args.first().toInt() * context.args.last().toInt())
                }
            }
        },
        DO {
            override fun execute(context: ExecutionContext) {
                context.enabled = true
            }
        },
        DONT {
            override fun execute(context: ExecutionContext) {
                context.enabled = false
            }
        };

        companion object {
            fun parse(command: String): COMMAND {
                return when (val commandString = command.uppercase()) {
                    "MUL" -> MUL
                    "DON'T" -> DONT
                    "DO" -> DO
                    else -> throw IllegalArgumentException("Invalid command: $commandString")
                }
            }
        }

        abstract fun execute(context: ExecutionContext)
    }

    enum class TOKEN {
        OPEN_PARENTHESIS {
            override fun process(char: Char, context: ExecutionContext) {
                if (context.command == null) {
                    while (context.commandWindow.isNotEmpty()) {
                        try {
                            context.command = COMMAND.parse(context.commandWindow.joinToString(""))
                            context.commandWindow.clear()
                        } catch (e: IllegalArgumentException) {
                            context.commandWindow.removeAt(0)
                        }
                    }
                } else {
                    context.resetCommandState()
                }
            }
        },
        CLOSE_PARENTHESIS {
            override fun process(char: Char, context: ExecutionContext) {
                if (context.command == null) {
                    context.resetCommandState()
                } else {
                    context.executeCommand()
                }
            }
        },
        COMMA {
            override fun process(char: Char, context: ExecutionContext) {
                if (context.command == null) {
                    context.resetCommandState()
                } else if (context.args.isEmpty()) {
                    context.resetCommandState()
                } else {
                    context.args.add("")
                }
            }
        },
        DIGIT {
            override fun process(char: Char, context: ExecutionContext) {
                if (context.command == null) {
                    context.resetCommandState()
                } else if (context.args.isEmpty()) {
                    context.args.add(char.toString())
                } else {
                    val arg = context.args.removeLast()
                    context.args.add(arg + char)
                }
            }
        },
        OTHER {
            override fun process(char: Char, context: ExecutionContext) {
                if (context.command == null) {
                    context.commandWindow.add(char)
                    if (context.commandWindow.size > 5) {
                        context.commandWindow.removeAt(0)
                    }
                } else {
                    context.resetCommandState()
                }
            }
        };

        companion object {
            fun parse(token: Char): TOKEN {
                return when {
                    token == '(' -> OPEN_PARENTHESIS
                    token == ')' -> CLOSE_PARENTHESIS
                    token == ',' -> COMMA
                    token.isDigit() -> DIGIT
                    else -> OTHER
                }
            }
        }

        abstract fun process(char: Char, context: ExecutionContext)
    }

    data class ExecutionContext(
        val commandWindow: MutableList<Char> = mutableListOf(),
        var command: COMMAND? = null,
        val args: MutableList<String> = mutableListOf(),
        var enabled: Boolean = true,
        var sum: Int = 0,
        val onlyMuls: Boolean
    ) {
        fun resetCommandState() {
            commandWindow.clear()
            command = null
            args.clear()
        }

        fun executeCommand(){
            command?.let {
                if (!onlyMuls || it == COMMAND.MUL) {
                    it.execute(this)
                }
            }
            resetCommandState()
        }
    }
}

fun main() {
    val day = Day03()
    day.part1()
    day.part2()
}