package com.android.challenge.expressionsolver.model

/**
 * Type of token - math expressions, brackets and numbers
 */
enum class RpnTokenType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    NUMBER,
}