package me.jikud.tdgame.helpers

import java.awt.Font

class FFont(name: String, style: Int, size: Int) : Font(name, style, size) {
    override fun equals(other: Any?): Boolean {
        return other is Font && other.name == name && other.style == style && other.size == size
    }

    override fun deriveFont(size: Float): FFont {
        return FFont(name, style, (this.size * size).toInt())
    }

    override fun hashCode(): Int {
        return name.hashCode() xor style xor size
    }
}