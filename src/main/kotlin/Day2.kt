/**
 * Part 1 answer: 548
 * Part 2 answer: 502
 */
class Day2 {

    /**
     * Track running total of valid passwords, starting from zero.
     * Parse each line of text to get lower bound, upper bound, and character.
     * Iterate through password, counting occurrences of character.
     * If occurrences is between bounds, increment running total.
     * Run time is O(n) - simply running through every character in entire input
     */
    fun part1(arr: List<String>): Int {
        var totalCorrect = 0

        for (line in arr) {
            val reqs = line.substringBefore(':').split("-"," ")
            val min = reqs[0].toInt()
            val max = reqs[1].toInt()
            val letter = reqs[2][0] // effectively toChar()

            val password = line.substringAfter(':')
            var occurrences = 0

            for (ch in password) {
                if (letter == ch) {
                    occurrences++
                }
            }

            if (occurrences > min-1 && occurrences < max+1) {
                totalCorrect++
            }
        }
        return totalCorrect
    }

    /**
     * Setup is similar to Part 1, but now min -> first index and max -> last index.
     */
    fun part2(arr: List<String>): Int {
        var totalCorrect = 0

        for (line in arr) {
            val meta = line.split("-"," ")
            val left = meta[0].toInt()-1    // -1 to turn cardinality to indices
            val right = meta[1].toInt()-1
            val letter = meta[2][0]

            val sub = line.substringAfterLast(" ")

            // Love how Kotlin has boolean operators as functions
            // I didn't know Java had it (^ operator), but seeing 'xor' is much nicer
            if ((sub[left] == letter).xor(sub[right] == letter)) {
                totalCorrect++
            }
        }
        return totalCorrect
    }
}
