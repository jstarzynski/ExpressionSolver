package com.android.challenge.expressionsolver

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExpressionParserTest {

    @Test
    fun splitExpressions() {

        val firstExpression = " 5  + 7 * 2 + 3.14 *   122.7554  "
        val firstSplitExpression = listOf(
                RpnToken(RpnTokenType.NUMBER, 5f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 7f),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 3.14f),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.NUMBER, 122.7554f)
        )

        val secondExpression = "((5+6)*(7-8))/(2+3)"
        val secondSplitExpression = listOf(
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.NUMBER, 5f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 6f),
                RpnToken(RpnTokenType.RIGHT_BRACKET),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.NUMBER, 7f),
                RpnToken(RpnTokenType.SUBTRACT),
                RpnToken(RpnTokenType.NUMBER, 8f),
                RpnToken(RpnTokenType.RIGHT_BRACKET),
                RpnToken(RpnTokenType.RIGHT_BRACKET),
                RpnToken(RpnTokenType.DIVIDE),
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 3f),
                RpnToken(RpnTokenType.RIGHT_BRACKET)
        )

        Assert.assertEquals(firstSplitExpression, ExpressionParser.splitExpression(firstExpression))
        Assert.assertEquals(secondSplitExpression, ExpressionParser.splitExpression(secondExpression))
    }

    @Test
    fun infixToRpnConversion() {

        val firstExpression = " 5  + 7 * 2 + 3.14 *   122.7554  "
        val firstRpnNotation = listOf(
                RpnToken(RpnTokenType.NUMBER, 5f),
                RpnToken(RpnTokenType.NUMBER, 7f),
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 3.14f),
                RpnToken(RpnTokenType.NUMBER, 122.7554f),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.ADD)
        )

        val secondExpression = "((5+6)*(7-8))/(2+3)"
        val secondRpnNotation = listOf(
                RpnToken(RpnTokenType.NUMBER, 5f),
                RpnToken(RpnTokenType.NUMBER, 6f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 7f),
                RpnToken(RpnTokenType.NUMBER, 8f),
                RpnToken(RpnTokenType.SUBTRACT),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.NUMBER, 3f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.DIVIDE)
        )

        Assert.assertEquals(firstRpnNotation, ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(firstExpression)))
        Assert.assertEquals(secondRpnNotation, ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(secondExpression)))

    }
}
