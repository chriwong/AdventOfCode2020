import java.io.BufferedReader
import java.io.FileReader

/**
 * Base class for each day's solution. Takes care of different file names and input-reading.
 * I don't know if each day of Advent of Code asks for number/integer answers,
 *  so the generics are there to allow each part() to return whatever type
 */
abstract class Day<T1,T2>(private val fileName: String) {
    abstract fun part1(): T1
    abstract fun part2(): T2

    val input = this.readPuzzleInput()

    /**
     * Read the day's puzzle input as an array of Strings
     */
    private fun readPuzzleInput(): List<String> {
        val input = mutableListOf<String>()
        val reader = BufferedReader(FileReader("input/$fileName"))

        var line = reader.readLine()
        while (line != null) {
            input.add(line)
            line = reader.readLine()
        }
        return input
    }
}
