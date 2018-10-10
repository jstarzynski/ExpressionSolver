package com.android.challenge.expressionsolver.model

data class RpnToken(val type: RpnTokenType, val value: Float = 0f) {

    fun isOperator() = type in arrayOf(RpnTokenType.DIVIDE, RpnTokenType.MULTIPLY, RpnTokenType.SUBTRACT, RpnTokenType.ADD)

}