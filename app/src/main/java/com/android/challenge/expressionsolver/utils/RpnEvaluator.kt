package com.android.challenge.expressionsolver.utils

import com.android.challenge.expressionsolver.model.RpnToken
import com.android.challenge.expressionsolver.model.RpnTokenType
import java.util.*

/**
 * Utility class for evaluating RPN expression (in internal format)
 */
object RpnEvaluator {

    /**
     * Computes value of the RPN expression
     * It follows standard algorithm for evaluating RPN expressions.
     */
    fun evaluate(rpnExpression: List<RpnToken>): Float {
        val stack = Stack<Float>()

        rpnExpression.forEach { token ->
            when {
                token.type == RpnTokenType.NUMBER -> stack.push(token.value)
                token.isOperator() -> {

                    val a = stack.pop()
                    val b = stack.pop()

                    @Suppress("NON_EXHAUSTIVE_WHEN")
                    when(token.type) {
                        RpnTokenType.ADD -> stack.push(b + a)
                        RpnTokenType.SUBTRACT -> stack.push(b - a)
                        RpnTokenType.DIVIDE -> stack.push(b / a)
                        RpnTokenType.MULTIPLY -> stack.push(b * a)
                    }
                }
            }
        }

        return stack[0]
    }

}