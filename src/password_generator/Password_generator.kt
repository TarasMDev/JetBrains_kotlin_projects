package password_generator

/**
 * A program that generates random password
 */

fun main() {
    print("Please enter number of: uppercase letters, lowercase letters, digits, password length")
    var (a, b, c, n) = readLine()!!.split(" ").map(String::toInt)
    val upperCase = ('A'..'Z').joinToString("")
    var password = ""
    while (password.length < n) {
        if (a > 0 || b == 0 && c == 0 && password.length < n) {
            password += upperCase[password.length % 26]
            a--
        }
        if (b > 0 || a == 0 && c == 0 && password.length < n) {
            password += upperCase[password.length % 26].toLowerCase()
            b--
        }
        if (c > 0 || a == 0 && b == 0 && password.length < n) {
            password += password.length % 9
            c--
        }
    }
    println(password)
}