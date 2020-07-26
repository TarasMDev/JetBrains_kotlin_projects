package simple_unit_converter

import java.util.Scanner
/**
 * program that can convert length, weight, and temperature
 * from one common unit of measurement to another,
 * such as inches to meters, kilograms to pounds, and so on.
 *
 * usage: Enter what you want to convert (or exit): 1 km to feet or 1 degree Celsius to kelvins
 */

val scanner = Scanner(System.`in`)

fun measure(): String {
    val input = scanner.next()
    return when (input.toLowerCase()) {
        "degree", "degrees" -> scanner.next()
        else -> input
    }.toLowerCase()
}

fun main() {
    println("Enter what you want to convert (or exit): ")
    try {
        val inputLine = scanner.next()
        if (inputLine == "exit") return
        val number = inputLine.toDouble()
        val measure = Units.getUnit(measure())
        scanner.next()
        val finalMeasure = Units.getUnit(measure())

        println(measure.to(number, finalMeasure))

    } catch (e: Exception) {
        println("Parse error")
    }
    main()
}

enum class Units(val group: Int,
                 val shortName: String,
                 val singular: String,
                 val plural: String,
                 val value: Double = 1.0) {

    METER(1, "m", "meter", "meters"),
    KILOMETER(1, "km", "kilometer", "kilometers", 1000.0),
    CENTIMETER(1, "cm", "centimeter", "centimeters", 0.01),
    MILLIMETER(1, "mm", "millimeter", "millimeters", 0.001),
    YARD(1, "yd", "yard", "yards", 0.9144),
    MILE(1, "mi", "mile", "miles", 1609.35),
    INCH(1, "in", "inch", "inches", 0.0254),
    FOOT(1, "ft", "foot", "feet", 0.3048),

    GRAM(2, "g", "gram", "grams"),
    KILOGRAM(2, "kg", "kilogram", "kilograms", 1000.0),
    MILLIGRAM(2, "mg", "milligram", "milligrams", 0.001),
    POUND(2, "lb", "pound", "pounds", 453.592),
    OUNCE(2, "oz", "ounce", "ounces", 28.3495),

    CELSIUS(3, "c", "dc", "celsius"),
    FAHRENHEIT(3, "f", "df", "fahrenheit"),
    KELVIN(3, "k", "kelvin", "kelvins"),

    NULL(0, "???", "???", "???");

    companion object {
        fun getUnit(input: String): Units {
            for (enum in values())
                when (input) {
                    enum.shortName, enum.plural, enum.singular -> return enum
                }
            return NULL
        }
    }
}

//extension function to
fun Units.to(n: Double, unit: Units): String {

    fun s(num: Double = n, u: Units): String {
        val checkingTempGroup = u.group == 3 && u != Units.KELVIN
        return if (num != 1.0)
            if (checkingTempGroup) "degrees ${u.plural.capitalize()}" else u.plural
        else if (checkingTempGroup) "degree ${u.plural.capitalize()}" else u.singular
    }

    val impossible = "Conversion from ${s(0.0, this)} to ${s(0.0, unit)} is impossible"
    val output = if (this.group == unit.group) {

        val nonNeg = " shouldn't be negative"
        when {
            group == 1 && n < 0 -> return "Length$nonNeg"
            group == 2 && n < 0 -> return "Weight$nonNeg"
        }

        when (this.group) {
            1, 2 -> n * this.value / unit.value
            3 -> when {
                this == unit -> n
                this == Units.CELSIUS -> if (unit == Units.FAHRENHEIT) n * 9 / 5 + 32 else n + 273.15 // to F or to K
                this == Units.FAHRENHEIT -> if (unit == Units.CELSIUS) (n - 32) * 5 / 9 else (n + 459.67) * 5 / 9 // to C or to K
                else -> if (unit == Units.CELSIUS) n - 273.15 else n * 9 / 5 - 459.67 // K to C or to F
            }
            else -> return impossible
        }
    } else return impossible

    return "$n ${s(n, this)} is $output ${s(output, unit)}"
}