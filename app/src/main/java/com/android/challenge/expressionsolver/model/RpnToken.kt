package com.android.challenge.expressionsolver.model

/**
 * Model for representing parsed string expression. It's building block for
 * internal representation of expression both for Infix and RPN notations.
 */
data class RpnToken(val type: RpnTokenType, val value: Float = 0f) {

    /**
     * Checks whether given token represents mathematical operator
     */
    fun isOperator() = type in arrayOf(RpnTokenType.DIVIDE, RpnTokenType.MULTIPLY, RpnTokenType.SUBTRACT, RpnTokenType.ADD)

}