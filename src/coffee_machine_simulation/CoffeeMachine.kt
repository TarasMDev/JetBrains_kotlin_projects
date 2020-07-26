package coffee_machine_simulation

import java.lang.Exception

/**
 * A program that makes you a coffee – virtual coffee, of course.
 * It can run out of milk, it can run out of coffee beans,
 * it can make different varieties of coffee, and it can take the money for making a coffee.
 */


//Resources
var water = 400
var milk = 540
var beans = 120
var cups = 9
var money = 550

fun main() {
    while (true) {
        print("Write action (buy, fill, take, remaining, exit): > ")
        when (readLine()) {
            //information about resources
            "remaining" -> {
                getRes()
            }
            "buy" -> {
                print("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: > ")
                when (readLine()) {
                    "1" -> { // espresso
                        if (water - 250 >= 0 && beans - 16 >= 0 && cups - 1 >= 0) {
                            println("I have enough resources, making you a coffee!\n")
                            makeCoffee(-250, 0, -16, 4, -1)
                        } else {
                            checkNeededResources(250, 0, 16, 1)
                        }
                    }
                    "2" -> { // latte
                        if (water - 350 >= 0 && milk - 75 >= 0 && beans - 20 >= 0 && cups - 1 >= 0) {
                            println("I have enough resources, making you a coffee!\n")
                            makeCoffee(-350, -75, -20, 7, -1)
                        } else {
                            checkNeededResources(350, 75, 20, 1)
                        }
                    }
                    "3" -> { // cappuccino
                        if (water - 200 >= 0 && milk - 100 >= 0 && beans - 12 >= 0 && cups - 1 >= 0) {
                            println("I have enough resources, making you a coffee!\n")
                            makeCoffee(-200, -100, -12, 6, -1)
                        } else {
                            checkNeededResources(200, 100, 12, 1)
                        }
                    }
                    "back"->{
                       main()
                    }
                    else -> {
                        printError("Not an option")
                    }
                }
            }
            "fill" -> {
                print("\nWrite how many ml of water do you want to add: > ")
                try {
                    makeCoffee(readLine()!!.toInt(), 0, 0, 0, 0)
                } catch (ex: Exception) {
                    printError(ex.toString())
                }
                print("Write how many ml of milk do you want to add: > ")
                try {
                    makeCoffee(0, readLine()!!.toInt(), 0, 0, 0)
                } catch (ex: Exception) {
                    printError(ex.toString())
                }
                print("Write how many grams of coffee beans do you want to add: > ")
                try {
                    makeCoffee(0, 0, readLine()!!.toInt(), 0, 0)
                } catch (ex: Exception) {
                    printError(ex.toString())
                }
                print("Write how many disposable cups of coffee do you want to add: > ")
                try {
                    makeCoffee(0, 0, 0, 0, readLine()!!.toInt())
                } catch (ex: Exception) {
                    printError(ex.toString())
                }
                print("\n")
            }
            "take" -> {
                println("\nI gave you $$money\n")
                makeCoffee(0, 0, 0, -money, 0)
            }
            "exit" -> {
                return
            }
            else -> {
                printError("Not an option")
            }
        }
    }
}

fun makeCoffee(waterNeeded: Int, milkNeeded: Int, beansNeed: Int, moneyNeeded: Int, cupsNeed: Int) {
    water += waterNeeded
    milk += milkNeeded
    beans += beansNeed
    money += moneyNeeded
    cups += cupsNeed
}

fun getRes() {
    println("""
        
        The coffee machine has:
        $water of water
        $milk of milk
        $beans of coffee beans
        $cups of disposable cups
    """.trimIndent())

    if (money == 0) {
        println("$money of money\n")

    } else {
        println("$$money of money\n")
    }
}

fun checkNeededResources(waterNeed: Int, milkNeed: Int, beansNeed: Int, cupsNeed: Int) {
    if (water - waterNeed < 0) {
        println("Sorry not enough water\n")
    }
    if (milk - milkNeed < 0) {
        println("Sorry not enough milk\n")
    }
    if (beans - beansNeed < 0) {
        println("Sorry not enough coffee beans\n")
    }
    if (cups - cupsNeed < 0) {
        println("Sorry not enough dispensable cups\n")
    }
}

fun printError(e: String) {
    println("\nError($e)\n")
}