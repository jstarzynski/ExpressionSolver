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

        val thirdExpression = "((2+7)/3+(14-3)*4)/2"
        val thirdSplitExpression = listOf(
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 7f),
                RpnToken(RpnTokenType.RIGHT_BRACKET),
                RpnToken(RpnTokenType.DIVIDE),
                RpnToken(RpnTokenType.NUMBER, 3f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.LEFT_BRACKET),
                RpnToken(RpnTokenType.NUMBER, 14f),
                RpnToken(RpnTokenType.SUBTRACT),
                RpnToken(RpnTokenType.NUMBER, 3f),
                RpnToken(RpnTokenType.RIGHT_BRACKET),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.NUMBER, 4f),
                RpnToken(RpnTokenType.RIGHT_BRACKET),
                RpnToken(RpnTokenType.DIVIDE),
                RpnToken(RpnTokenType.NUMBER, 2f)
        )

        Assert.assertEquals(firstSplitExpression, ExpressionParser.splitExpression(firstExpression))
        Assert.assertEquals(secondSplitExpression, ExpressionParser.splitExpression(secondExpression))
        Assert.assertEquals(thirdSplitExpression, ExpressionParser.splitExpression(thirdExpression))

        try {
            ExpressionParser.splitExpression("12,34+5,25")
            Assert.fail()
        } catch (e: Exception) {}

        try {
            ExpressionParser.splitExpression("12.5.34 + 53.11.23")
            Assert.fail()
        } catch (e: Exception) {}
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

        val thirdExpression = "((2+7)/3+(14-3)*4)/2"
        val thirdRpnNotation = listOf(
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.NUMBER, 7f),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 3f),
                RpnToken(RpnTokenType.DIVIDE),
                RpnToken(RpnTokenType.NUMBER, 14f),
                RpnToken(RpnTokenType.NUMBER, 3f),
                RpnToken(RpnTokenType.SUBTRACT),
                RpnToken(RpnTokenType.NUMBER, 4f),
                RpnToken(RpnTokenType.MULTIPLY),
                RpnToken(RpnTokenType.ADD),
                RpnToken(RpnTokenType.NUMBER, 2f),
                RpnToken(RpnTokenType.DIVIDE)
        )

        Assert.assertEquals(firstRpnNotation, ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(firstExpression)))
        Assert.assertEquals(secondRpnNotation, ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(secondExpression)))
        Assert.assertEquals(thirdRpnNotation, ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(thirdExpression)))

        try {
            ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression("325.23 + 45) / (25.36+11) * 5"))
            Assert.fail()
        } catch (e: Exception) { }

    }
}
