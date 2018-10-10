package com.android.challenge.expressionsolver.utils

import com.android.challenge.expressionsolver.model.RpnToken
import com.android.challenge.expressionsolver.model.RpnTokenType
import java.util.*

object ExpressionParser {

    private const val LEFT_BRACKET = '('
    private const val RIGHT_BRACKET = ')'
    private const val DECIMAL_SEPARATOR = '.'
    private val NUMBER_SIGNS = arrayOf('+', '-')
    private val OPERATORS = arrayOf('+', '-', '*', '/')

    private val OPERATORS_PRIORITY = mapOf(
            RpnTokenType.ADD to 1,
            RpnTokenType.SUBTRACT to 1,
            RpnTokenType.MULTIPLY to 2,
            RpnTokenType.DIVIDE to 2)

    private val OPERATORS_TO_TOKEN_TYPE = mapOf(
            '+' to RpnTokenType.ADD,
            '-' to RpnTokenType.SUBTRACT,
            '*' to RpnTokenType.MULTIPLY,
            '/' to RpnTokenType.DIVIDE)

    fun splitExpression(expression: String): List<RpnToken> {

        val tokens = mutableListOf<RpnToken>()
        val numberBuffer = StringBuffer()

        expression.forEach { char ->

            if (tokens.lastOrNull()?.type != RpnTokenType.NUMBER
                    && numberBuffer.isEmpty()
                    && char in NUMBER_SIGNS)
                numberBuffer.append(char)
            else if (char.isDigit() || char == DECIMAL_SEPARATOR)
                numberBuffer.append(char)
            else {
                resolveNumbersBuffer(numberBuffer, tokens)
                when {
                    char.isWhitespace() -> { }
                    char == LEFT_BRACKET -> tokens.add(RpnToken(RpnTokenType.LEFT_BRACKET))
                    char == RIGHT_BRACKET -> tokens.add(RpnToken(RpnTokenType.RIGHT_BRACKET))
                    char in OPERATORS -> OPERATORS_TO_TOKEN_TYPE[char]?.let { tokens.add(RpnToken(it)) }
                    else -> throw IllegalArgumentException("Invalid expression has been provided.")
                }
            }
        }

        resolveNumbersBuffer(numberBuffer, tokens)
        return tokens
    }

    private fun resolveNumbersBuffer(numberBuffer: StringBuffer, tokens: MutableList<RpnToken>) {
        if (numberBuffer.isNotEmpty()) {
            tokens.add(RpnToken(RpnTokenType.NUMBER, numberBuffer.toString().toFloat()))
            numberBuffer.delete(0, numberBuffer.length)
        }
    }

    fun convertInfixToRpn(splitExpression: List<RpnToken>): List<RpnToken> {

        val output = mutableListOf<RpnToken>()
        val stack = Stack<RpnToken>()

        splitExpression.forEach { token ->
            when {
                token.type == RpnTokenType.NUMBER -> output.add(token)

                token.type == RpnTokenType.LEFT_BRACKET -> stack.push(token)

                token.type == RpnTokenType.RIGHT_BRACKET -> {
                    var unfinished = true
                    while (stack.isNotEmpty() && unfinished) {
                        val topToken = stack.pop()
                        when {
                            topToken.isOperator() -> output.add(topToken)
                            topToken.type == RpnTokenType.LEFT_BRACKET -> unfinished = false
                        }
                    }
                    if (unfinished) throw IllegalArgumentException("Not a valid infix expression. Brackets don't match")
                }

                token.isOperator() -> {
                    while (!stack.empty() && stack.peek().type != RpnTokenType.LEFT_BRACKET && hasGreaterOrEqualPriority(stack.peek().type, token.type))
                        output.add(stack.pop())
                    stack.push(token)
                }
            }
        }

        while (stack.isNotEmpty()) output.add(stack.pop())

        return output
    }

    private fun hasGreaterOrEqualPriority(firstTokenType: RpnTokenType, secondTokenType: RpnTokenType) =
            (OPERATORS_PRIORITY[firstTokenType] ?: 0) >= (OPERATORS_PRIORITY[secondTokenType] ?: 0)

}