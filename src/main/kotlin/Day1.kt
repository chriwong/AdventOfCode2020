import java.io.BufferedReader
import java.io.FileReader

fun main() {
    val arr = mutableListOf<Int>()
    val reader = BufferedReader(FileReader("input/day1.txt"))

    // Read input into an array
    var line = reader.readLine()
    while (line != null) {
        arr.add(line.toInt())
        line = reader.readLine()
    }

    println("Solution 1: ${part1(arr)}")
    println("Solution 2: ${part2(arr)}")
}

/**
 * Iterate through input once.
 * For each value v, store map of {2020-v : v}. This means the solution to the current value is being stored as key.
 * After storing, check if map has key v.
 * If so, the solution is the value at key v.
 * Time complexity is O(n) because checking for pair of current v is constant time due to hashing.
 */
fun part1(arr: List<Int>): Int {
    val map = mutableMapOf<Int,Int>()
    for (v in arr.indices) {
        // Check for current p in map
        if (map[arr[v]] !== null) {
            println("-Part 1-\nValue 1: ${arr[v]}\nValue 2: ${map[arr[v]]!!}")
            return arr[v] * map[arr[v]]!!
        } else {
            // Fill map with solution for current number
            map[2020-arr[v]] = arr[v]
        }
    }
    return -1
}

/**
 * Iterate through input once.
 * For each value v, create map.
 * For all previous input values s..t, store {2020-t-v: v}.
 * After storing s..t times, check if any maps s..t have key v.
 * If so, solution is value at key v for map s.
 * Time complexity is O(n*n) because each value is being checked in map of each previous value.
 * Fortunately, checking is constant time, but still sucks that every previous value is inspected.
 */
fun part2(arr: List<Int>): Int {
    val map = mutableMapOf<Int, MutableMap<Int,Int>>()

    for (v in arr.indices) {
        for (key in map.keys) {
            if (map[key]?.get(arr[v]) !== null) {
                println("-Part 2-\nValue 1: ${key}\nValue 2: ${v}\nValue 3: ${2020-key-v}")
                return key*arr[v]*(2020-key-arr[v])
            }
            map[key]?.set(2020-key-arr[v], arr[v])
        }
        map[arr[v]] = mutableMapOf()
    }
    return -1
}
