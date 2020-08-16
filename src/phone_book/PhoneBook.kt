package phone_book

import java.io.File
import java.util.Collections
import kotlin.math.sqrt

/**
 * Project for better understanding the algorithms.
 */

//path variables
const val pathToFindFile = "path to the file which contains lines to find" //example: /Users/username/find.txt
const val pathToDirFile = "directory with over 100_000_000 lines"

/**
 * Enum class to store different search algorithms
 * @param searchAlgo: type of search algorithm
 * @param info: simple notification message about execution
 */
enum class TypeOfSearchAlgo(val searchAlgo: SearchAlgo, val info: String) {
    LINEAR(LinearSearchAlgo, "Start searching (linear search)..."),
    JUMP(JumpSearchAlgo, "Start searching (bubble sort + jump search)..."),
    BINARY(BinarySearchAlgo, "Start searching (quick sort + binary search)..."),
    HASH(HashSearchAlgo, "Start searching (hash table)...")
}

fun main() {
    val informationToFind = File(pathToFindFile).readLines()
    val dir = Directory()
    dir.find(informationToFind, TypeOfSearchAlgo.LINEAR)
    dir.find(informationToFind, TypeOfSearchAlgo.JUMP, 10 * LinearSearchAlgo.stop)
    dir.find(informationToFind, TypeOfSearchAlgo.BINARY)
    dir.find(informationToFind, TypeOfSearchAlgo.HASH)
}

class Directory {
    private val entry = loadDirectory()
    //load all entry lines in dir file
    private fun loadDirectory(): MutableList<SavedData> {
        val entry = mutableListOf<SavedData>()
        File(pathToDirFile).forEachLine {
            val (number, name) = it.split(" ", limit = 2)
            entry.add(SavedData(number, name))
        }
        return entry
    }

    fun find(infoToFind: List<String>, searchAlgo: TypeOfSearchAlgo, timeLimit: Long = 0): List<SavedData> {
        println(searchAlgo.info)
        val foundRecords = searchAlgo.searchAlgo.find(entry, infoToFind, timeLimit)
        printInfoAboutOperation(searchAlgo.searchAlgo, foundRecords.size, infoToFind.size)
        return foundRecords
    }

    private fun printInfoAboutOperation(searchAlgo: SearchAlgo, found: Int, total: Int) {
        print("Found $found / $total entries. ")
        println("Time taken: ${searchAlgo.stop.toTime()}")
        if (searchAlgo !is LinearSearchAlgo) {
            print(if (searchAlgo is HashSearchAlgo) "Creating time: " else "Sorting time: ")
            print(searchAlgo.start.toTime())
            if (searchAlgo is JumpSearchAlgo && searchAlgo.stopped) {
                print(" - STOPPED, moved to linear search")
            }
            println()
            println("Searching time: ${(searchAlgo.stop - searchAlgo.start).toTime()}")
        }
        println()
    }

    private fun Long.toTime() = "${this / 60_000} min. ${this / 1_000 % 60} sec. ${this % 1_000} ms."
}

//Implementation of simple linear search algorithm
object LinearSearchAlgo : SearchAlgo() {
    override fun find(recs: MutableList<SavedData>, infoToFind: List<String>, timeLimit: Long): List<SavedData> {
        val found = mutableListOf<SavedData>()
        val startTime = System.currentTimeMillis()
        for (name in infoToFind) {
            for (record in recs) {
                if (record.name == name) {
                    found.add(record)
                    break
                }
            }
        }
        stop = System.currentTimeMillis() - startTime
        return found
    }
}

object JumpSearchAlgo : SearchAlgo() {
    override fun find(recs: MutableList<SavedData>, infoToFind: List<String>, timeLimit: Long): List<SavedData> {
        val startTime = System.currentTimeMillis()
        stopped = bubbleSort(recs, timeLimit)
        start = System.currentTimeMillis() - startTime
        val found = (if (!stopped) {
            searchInSorted(recs, infoToFind)
        } else {
            LinearSearchAlgo.find(recs, infoToFind)
        })
        stop = System.currentTimeMillis() - startTime
        return found
    }

    private fun searchInSorted(savedData: List<SavedData>, namesToFind: List<String>): List<SavedData> {
        val found = mutableListOf<SavedData>()
        val stepSize = sqrt(savedData.size.toDouble()).toInt()
        for (name in namesToFind) {
            var blockStart = 0
            var blockEnd = stepSize
            while (savedData[minOf(blockEnd, savedData.size) - 1].name < name) {
                blockStart = blockEnd
                blockEnd += stepSize
                if (blockStart >= savedData.size) continue
            }
            while (savedData[blockStart].name < name) {
                blockStart++
                if (blockStart == minOf(blockEnd, savedData.size)) continue
            }
            if (savedData[blockStart].name == name)
                found.add(savedData[blockStart])
        }
        return found
    }

    //very slow, don't use in real world projects
    private fun bubbleSort(savedData: MutableList<SavedData>, timeLimit: Long): Boolean {
        val startTime = System.currentTimeMillis()
        for (n in savedData.lastIndex - 1 downTo 0) {
            for (i in 0..n) {
                if (savedData[i].name > savedData[i + 1].name) {
                    Collections.swap(savedData, i, i + 1)
                }
            }
            if (System.currentTimeMillis() - startTime > timeLimit) return true
        }
        return false
    }
}

object BinarySearchAlgo : SearchAlgo() {
    override fun find(recs: MutableList<SavedData>, infoToFind: List<String>, timeLimit: Long): List<SavedData> {
        val startTime = System.currentTimeMillis()
        quickSort(recs)
        start = System.currentTimeMillis() - startTime
        val found = searchInSorted(recs, infoToFind)
        stop = System.currentTimeMillis() - startTime
        return found
    }

    private fun searchInSorted(savedData: List<SavedData>, namesToFind: List<String>): List<SavedData> {
        val found = mutableListOf<SavedData>()
        for (name in namesToFind) {
            var left = 0
            var right = savedData.lastIndex
            loop@ while (left <= right) {
                val middle = (left + right) / 2
                when {
                    savedData[middle].name < name -> left = middle + 1
                    savedData[middle].name > name -> right = middle - 1
                    else -> {
                        found.add(savedData[middle])
                        break@loop
                    }
                }
            }
        }
        return found
    }

    private fun quickSort(savedData: MutableList<SavedData>, low: Int = 0, high: Int = savedData.lastIndex) {
        if (low < high) {
            val p = partition(savedData, low, high)
            quickSort(savedData, low, p - 1)
            quickSort(savedData, p + 1, high)
        }
    }

    private fun partition(savedData: MutableList<SavedData>, low: Int, high: Int): Int {
        val pivot = savedData[high].name
        var i = low
        for (j in low..high) {
            if (savedData[j].name < pivot) {
                Collections.swap(savedData, i, j)
                i++
            }
        }
        Collections.swap(savedData, i, high)
        return i
    }
}

//Fastest search algorithm
object HashSearchAlgo : SearchAlgo() {
    override fun find(recs: MutableList<SavedData>, infoToFind: List<String>, timeLimit: Long): List<SavedData> {
        val startTime = System.currentTimeMillis()
        val found = mutableListOf<SavedData>()
        val hashTable = createHashTable(recs)
        start = System.currentTimeMillis() - startTime
        for (name in infoToFind) {
            val number = hashTable[name]
            if (number != null) found.add(number)
        }
        stop = System.currentTimeMillis() - startTime
        return found
    }

    private fun createHashTable(savedData: List<SavedData>): HashMap<String, SavedData> {
        val hashTable = HashMap<String, SavedData>()
        for (record in savedData) {
            hashTable[record.name] = record
        }
        return hashTable
    }
}

data class SavedData(val number: String, val name: String)

abstract class SearchAlgo {
    var stop = 0L
    var start = 0L
    var stopped = false

    abstract fun find(recs: MutableList<SavedData>, infoToFind: List<String>, timeLimit: Long = 0): List<SavedData>
}

