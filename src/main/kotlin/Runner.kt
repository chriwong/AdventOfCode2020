import java.io.BufferedReader
import java.io.FileReader

fun main() {
    val day = Day2()
    val arr = mutableListOf<String>()
    val reader = BufferedReader(FileReader("input/day2.txt"))
    //TODO change input ^^

    // Read input into an array
    var line = reader.readLine()
    while (line != null) {
        arr.add(line)
        line = reader.readLine()
    }

    println("Solution 1: ${day.part1(arr)}")
    println("Solution 2: ${day.part2(arr)}")
}
