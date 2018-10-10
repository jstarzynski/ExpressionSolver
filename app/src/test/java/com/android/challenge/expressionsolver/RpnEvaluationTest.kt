package com.android.challenge.expressionsolver

import com.android.challenge.expressionsolver.utils.ExpressionParser
import com.android.challenge.expressionsolver.utils.RpnEvaluator
import org.junit.Assert
import org.junit.Test

class RpnEvaluationTest {

    @Test
    fun expressionEvaluation() {
        Assert.assertEquals(23.5f, RpnEvaluator.evaluate(ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression("((2+7)/3+(14-3)*4)/2"))))
        Assert.assertEquals(-2.2f, RpnEvaluator.evaluate(ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression("((5+6)*(7-8))/(2+3)"))))
        Assert.assertEquals(404.451956f, RpnEvaluator.evaluate(ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(" 5  + 7 * 2 + 3.14 *   122.7554  "))))
        Assert.assertEquals(66.3333333333f, RpnEvaluator.evaluate(ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression("(2+7+3-(5-6-7*2+3))*2-5*(7-8/3+2-(8+2))"))))
    }

}