package com.android.challenge.expressionsolver.utils

import com.android.challenge.expressionsolver.model.RpnToken
import com.android.challenge.expressionsolver.model.RpnTokenType
import java.util.*

/**
 * Utility class for parsing expression input and converting it to RPN representation
 */
object ExpressionParser {

    /**
     * Different characters and group of characters for parser to act on
     */
    private const val LEFT_BRACKET = '('
    private const val RIGHT_BRACKET = ')'
    private const val DECIMAL_SEPARATOR = '.'
    private val NUMBER_SIGNS = arrayOf('+', '-')
    private val OPERATORS = arrayOf('+', '-', '*', '/')

    /**
     * Mapping of math operator characters to internal type - used while parsing
     */
    private val OPERATORS_TO_TOKEN_TYPE = mapOf(
            '+' to RpnTokenType.ADD,
            '-' to RpnTokenType.SUBTRACT,
            '*' to RpnTokenType.MULTIPLY,
            '/' to RpnTokenType.DIVIDE)

    /**
     * Mapping of math operators priority - useful when converting expression
     * to the RPN representation where i.e. multiplying comes before addition
     */
    private val OPERATORS_PRIORITY = mapOf(
            RpnTokenType.ADD to 1,
            RpnTokenType.SUBTRACT to 1,
            RpnTokenType.MULTIPLY to 2,
            RpnTokenType.DIVIDE to 2)

    /**
     * Splits expression (string representation) into list of tokens (RpnToken).
     * In this representation it's easier to manipulate contents and convert it to RPN format.
     *
     * @param expression
     * String expression to parse/convert to internal representation
     */
    fun splitExpression(expression: String): List<RpnToken> {

        val tokens = mutableListOf<RpnToken>()
        val numberBuffer = StringBuffer()

        expression.forEach { char ->

            /*
            If previous parsed token was not a number
            and we're not in the middle of parsing a number
            and char may represent sign of the number we use it when parsing the number that may
            appear after that.
             */
            if (tokens.lastOrNull()?.type != RpnTokenType.NUMBER
                    && numberBuffer.isEmpty()
                    && char in NUMBER_SIGNS)
                numberBuffer.append(char)

            /*
            If char is a digit or decimal separator we add it to the buffer to parse it as a number later
             */
            else if (char.isDigit() || char == DECIMAL_SEPARATOR)
                numberBuffer.append(char)

            else {
                /*
                At this point we know we don't parse number anymore
                so we can convert content of the buffer to the RpnToken
                 */
                resolveNumbersBuffer(numberBuffer, tokens)

                when {

                    /* Ignore whitespaces */
                    char.isWhitespace() -> { }

                    /* Parse brackets */
                    char == LEFT_BRACKET -> tokens.add(RpnToken(RpnTokenType.LEFT_BRACKET))
                    char == RIGHT_BRACKET -> tokens.add(RpnToken(RpnTokenType.RIGHT_BRACKET))

                    /* Parse operators using predefined mapping */
                    char in OPERATORS -> OPERATORS_TO_TOKEN_TYPE[char]?.let { tokens.add(RpnToken(it)) }

                    /* Throw an error on unexpected character */
                    else -> throw IllegalArgumentException("Invalid expression has been provided.")
                }
            }
        }

        /* In case last characters represented number we parse it as a token at the very end */
        resolveNumbersBuffer(numberBuffer, tokens)

        return tokens
    }

    /**
     * Checks content of the numberBuffer and parse it as a Number Token adding it to the tokens list.
     */
    private fun resolveNumbersBuffer(numberBuffer: StringBuffer, tokens: MutableList<RpnToken>) {
        if (numberBuffer.isNotEmpty()) {
            tokens.add(RpnToken(RpnTokenType.NUMBER, numberBuffer.toString().toFloat()))
            numberBuffer.delete(0, numberBuffer.length)
        }
    }

    /**
     * Takes Infix representation of the expression (written as list of tokens) and converts it to the RPN one.
     */
    fun convertInfixToRpn(splitExpression: List<RpnToken>): List<RpnToken> {

        val output = mutableListOf<RpnToken>()
        val stack = Stack<RpnToken>()

        splitExpression.forEach { token ->
            when {
                /* Type Number is add to the final output right away */
                token.type == RpnTokenType.NUMBER -> output.add(token)

                /* Left brackets are added to the stack */
                token.type == RpnTokenType.LEFT_BRACKET -> stack.push(token)

                /*
                Right bracket causes all content of stack to be transferred from the top of the stack
                to the final output until left bracket is found.
                Brackets themselves are not added to the output.
                 */
                token.type == RpnTokenType.RIGHT_BRACKET -> {
                    var unfinished = true
                    while (stack.isNotEmpty() && unfinished) {
                        val topToken = stack.pop()
                        when {
                            topToken.isOperator() -> output.add(topToken)
                            topToken.type == RpnTokenType.LEFT_BRACKET -> unfinished = false
                        }
                    }

                    /* When the end of the stack has been reached and matching right bracket hasn't been found we throw an error */
                    if (unfinished) throw IllegalArgumentException("Not a valid infix expression. Brackets don't match")
                }

                /*
                When the operator type is found we transfer all other operators that are of equal or greater priority from the stack
                to the final output and than put found operator on top of the stack
                 */
                token.isOperator() -> {
                    while (!stack.empty() && stack.peek().isOperator() && hasGreaterOrEqualPriority(stack.peek().type, token.type))
                        output.add(stack.pop())
                    stack.push(token)
                }
            }
        }

        /* We transfer all leftovers from the stack to the final output */
        while (stack.isNotEmpty()) output.add(stack.pop())

        return output
    }

    /**
     * Checks if first token type has greater or equal priority to the second one
     */
    private fun hasGreaterOrEqualPriority(firstTokenType: RpnTokenType, secondTokenType: RpnTokenType) =
            (OPERATORS_PRIORITY[firstTokenType] ?: 0) >= (OPERATORS_PRIORITY[secondTokenType] ?: 0)

}