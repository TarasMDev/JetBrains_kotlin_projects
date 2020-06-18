package simple_chatty_bot

fun main() {
    greet("MegaTron", "2020")
    remindName()
    guessAge()
    count()
    test()
    end()
}

fun greet(assistantName: String, birthYear: String) {
    println("""
        Hello! My name is $assistantName.
        I was created in $birthYear.
        Please, remind me your name.
    """.trimIndent())
}

fun remindName() {
    val name = readLine()!!.toString()
    println("What a great name you have, $name!")
}

fun guessAge() {
    println("""
        Let me guess your age.
        Enter remainders of dividing your age by 3, 5 and 7.
    """.trimIndent())
    val (rem3, rem5, rem7) = readLine()!!.split(" ").map(String::toInt)
    val age = (rem3 * 70 + rem5 * 21 + rem7 * 15) % 105
    println("Your age is $age; that's a good time to start programming!")
}

fun count() {
    println("Now I will prove to you that I can count to any number you want.")
    val number = readLine()!!.toInt()
    for (i in 0..number) {
        println("$i!")
    }
}

fun test() {
    println("Let's test your programming knowledge.")
    println("Why do we use methods?")
    println("""
        1. To repeat a statement multiple times.
        2. To decompose a program into several small subroutines.
        3. To determine the execution time of a program.
        4. To interrupt the execution of a program.
    """.trimIndent())
    do {
        val answer = readLine()!!.toInt()
        if (answer != 2) {
            println("Please try again.")
        }
    } while (answer != 2)
}

fun end() {
    println("Completed, have a nice day!")
}



