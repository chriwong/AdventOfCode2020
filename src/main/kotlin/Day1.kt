/**
 * Part 1 answer: 444019
 * Part 2 answer: 29212176
 */
class Day1 {

    /**
     * Iterate through input once.
     * For each value v, store map of {2020-v : v}. This means the solution to the current value is being stored as key.
     * After storing, check if map has key v.
     * If so, the solution is the value at key v.
     * Time complexity is O(n) because checking for pair of current v is constant time due to hashing.
     */
    fun part1(arr: List<String>): Int {
        val map = mutableMapOf<Int,Int>()
        for (line in arr.indices) {
            val v = arr[line].toInt()
            // Check for current p in map
            if (map[v] !== null) {
                println("-Part 1-\nValue 1: ${v}\nValue 2: ${map[v]!!}")
                return v * map[v]!!
            } else {
                // Fill map with solution for current number
                map[2020-v] = v
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
    fun part2(arr: List<String>): Int {
        val map = mutableMapOf<Int, MutableMap<Int,Int>>()

        for (line in arr.indices) {
            val v = arr[line].toInt()
            for (key in map.keys) {
                if (map[key]?.get(v) !== null) {
                    println("-Part 2-\nValue 1: ${key}\nValue 2: ${v}\nValue 3: ${2020-key-v}")
                    return key*v*(2020-key-v)
                }
                map[key]?.set(2020-key-v, v)
            }
            map[v] = mutableMapOf()
        }
        return -1
    }
}
