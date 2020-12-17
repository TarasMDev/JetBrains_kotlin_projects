package password_generator

import kotlin.random.Random

//  Pseudo "random" password generator
//  This program will generate a same 10-character long password depending on seed
//  If you want to get different password use Random.Default

fun main() {
    println(generatePredictablePassword(1))
    println(generatePredictablePassword1(42))
}

//With String builder
fun generatePredictablePassword(seed: Int): String {
    val rGenerator = Random(seed)
    val sBuilder = StringBuilder()
    repeat(10) {
        sBuilder.append(rGenerator.nextInt(33, 127).toChar())
    }
    return sBuilder.toString()
}

//Simple Kotlin style
fun generatePredictablePassword1(seed: Int): String {
    var randomPassword = ""
    val rGenerator = Random(seed)
    repeat(10) {
        randomPassword += rGenerator.nextInt(33, 127).toChar()
    }
    return randomPassword
}

//Kotlin style with string builder
fun generatePredictablePasswordK(seed: Int) = Random(seed)
    .let { r ->
        StringBuilder()
            .also { sb ->
                repeat(10) {
                    sb.append(r.nextInt(33, 127).toChar())
                }
            }.toString()
    }
