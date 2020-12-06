/**
 * Part 1 answer: 6443
 * Part 2 answer: 3232
 */
class Day6 : Day<Int, Int>("day6.txt") {

    /**
     * By group:
     *   Make hashmap of {question : affirmativeAnswerCount}
     *   For line in group:
     *     hashmap[question]++
     *   Running total increments by size of hashmap (number of unique questions with affirmative answers).
     * Time complexity is O(n) - every character is read only once and hashmap operation is constant time.
     */
    override fun part1(): Int {
        var solution = 0
        val map = mutableMapOf<Char,Int>()

        for (line in input) {
            if (line != "") {
                for (c in line) {
                    map[c] = if (map[c] != null) map[c]!!+1 else 1
                }
            } else {
                solution += map.size
                map.clear()
            }
        }
        solution += map.size

        return solution
    }

    /**
     * Track number of lines in the group and check if map values are equal to it.
     * Increment running total by number of hashmap cells whose values are equal to that.
     * Time complexity is O(n) - each character is read only once and hashmap operations are constant.
     *   Not sure how filtering is implemented, but worst-case is constant time 26.
     */
    override fun part2(): Int {
        var solution = 0
        var groupMembers = 0
        var totalGroups = 0
        val map = mutableMapOf<Char,Int>()

        for (line in input) {
            if (line != "") {
                groupMembers++
                for (c in line) {
                    map[c] = if (map[c] != null) map[c]!!+1 else 1
                }
            } else {
                solution += map.filter { entry -> entry.value == groupMembers }.size
                totalGroups++
                groupMembers = 0
                map.clear()
            }
        }
        // fuck fenceposts
        solution += map.filter { entry -> entry.value == groupMembers }.size

        return solution
    }
}
