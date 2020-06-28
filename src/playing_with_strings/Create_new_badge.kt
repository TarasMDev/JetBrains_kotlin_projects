package playing_with_strings

//Simple project for building a badge with new font.
fun main() {
    print("Enter name and surname: ")
    val name = readLine()!!
    print("Enter person's status: ")
    val status = readLine()!!
    val generatorForFont = Gen()
    val lines = listOf(
        generatorForFont.getLine(name, 1),
        generatorForFont.getLine(name, 2),
        generatorForFont.getLine(name, 3))
    val badge = lines.stream().mapToInt { it.length }.max().orElse(0).coerceAtLeast(status.length)
    printLine(badge)
    for (line in lines) {
        printCentered(line, badge)
    }
    printCentered(status, badge)
    printLine(badge)
}

fun printCentered(line: String, badge: Int) {
    printLMargin()
    val spaces = (badge - line.length) / 2
    repeat(spaces) { print(" ") }
    print(line)
    repeat((badge - line.length - spaces)) { print(" ") }
    printRMargin()
}

private fun printRMargin() {
    println("  *")
}

private fun printLMargin() {
    print("*  ")
}

private fun printLine(badge: Int) {
    repeat(badge + 6) { print("*") }
    println()
}

//New font generator
class Gen {
    fun getLine(name: String, row: Int): String {
        val builder = StringBuilder()
        val chars = name.toUpperCase()
        for (i in chars.indices) {
            builder.append(letter(chars[i], row - 1))
            if (i != chars.lastIndex) builder.append(" ")
        }
        return builder.toString()
    }

    private fun letter(character: Char, row: Int): String {
        return if (character == ' ') "    "
        else letterRow[row][character - 'A']
    }

    private val letterRow = mutableListOf<List<String>>()

    init {
        letterRow.add(listOf("____", "___ ", "____", "___ ", "____", "____", "____", "_  _", "_", " _", "_  _", "_   ", "_  _", "_  _", "____", "___ ", "____", "____", "____", "___", "_  _", "_  _", "_ _ _", "_  _", "_   _", "___ "))
        letterRow.add(listOf("|__|", "|__]", "|   ", "|  \\", "|___", "|___", "| __", "|__|", "|", " |", "|_/ ", "|   ", "|\\/|", "|\\ |", "|  |", "|__]", "|  |", "|__/", "[__ ", " | ", "|  |", "|  |", "| | |", " \\/ ", " \\_/ ", "  / "))
        letterRow.add(listOf("|  |", "|__]", "|___", "|__/", "|___", "|   ", "|__]", "|  |", "|", "_|", "| \\_", "|___", "|  |", "| \\|", "|__|", "|   ", "|_\\|", "|  \\", "___]", " | ", "|__|", " \\/ ", "|_|_|", "_/\\_", "  |  ", " /__"))
    }
}