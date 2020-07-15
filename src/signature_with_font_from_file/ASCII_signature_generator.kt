package signature_with_font_from_file

import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.math.*

class Font(path: String) {
    private val dataForFont: Map<Char, Letter>

    private val heightForTheLine: Int

    class Letter(val char: Char, val width: Int, val line: List<String>)

    init {
        dataForFont = Scanner(File(path)).run scanner@{
            heightForTheLine = nextInt()
            val count = nextInt()
            nextLine()
            mutableMapOf<Char, Letter>().apply {
                repeat(count) {
                    val char = next().first()
                    val width = nextInt()
                    nextLine()
                    val linesForLetters = (0 until heightForTheLine).map { nextLine() }
                    this[char] = Letter(char, width, linesForLetters)
                }
                val a = this['a']!!
                val emptyLine = " ".repeat(a.width)

                this[' '] = Letter(' ', a.width, (0 until heightForTheLine).map { emptyLine })
            }
        }
    }

    fun from(s: String): List<String> = (0 until heightForTheLine)
        .map { "" }.toMutableList()
        .apply {
            s.forEach { c ->
                dataForFont[c]?.line?.forEachIndexed { index, line -> this[index] += line }
            }
        }

}

object SignatureGenerator {
    private val roman = Font("""Enter file path here for your font1""")
    private val medium = Font("""Enter file path here for your font2""")

    private fun Int.toPadding() = " ".repeat(this)

    fun create(name: String, status: String): String {
        val linesForNameOutput = roman.from(name)
        val linesForStatusOutput = medium.from(status)
        val widthForName = linesForNameOutput.map(String::length).max() ?: 0
        val widthForStatus = linesForStatusOutput.map(String::length).max() ?: 0
        val width = max(widthForStatus, widthForName)
        fun offset(inner: Int) = floor(width.minus(inner).div(2.0)).toInt()
        val offsetLeft = offset(widthForStatus)
        val offsetRight = width - offsetLeft - widthForStatus
        val leftOffsetForTheName = offset(widthForName)
        val rightOffsetForTheName = width - leftOffsetForTheName - widthForName
        val leftPaddingForName = leftOffsetForTheName.toPadding()
        val rightPaddingForName = rightOffsetForTheName.toPadding()
        val leftPadding = offsetLeft.toPadding()
        val rightPadding = offsetRight.toPadding()
        val border = "8".repeat(width + 8)
        var res = "$border\n"
        res += linesForNameOutput.joinToString("\n") { "88  $leftPaddingForName$it$rightPaddingForName  88" } + "\n"
        res += linesForStatusOutput.joinToString("\n") { "88  $leftPadding$it$rightPadding  88" } + "\n"
        res += border
        return res
    }
}

fun main() {
    print("Enter name and surname: ")
    val name = readLine()!!.toString()
    print("Enter person's status: ")
    val status = readLine()!!.toString()
    try {
        println(SignatureGenerator.create(name, status))
    } catch (e: FileNotFoundException) {
        println("$e Sadly, file was not found.")
    }
}