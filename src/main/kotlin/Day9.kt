/**
 * Part 1 answer: 1212510616
 * Part 2 answer: 171265123
 */
class Day9 : Day<Int,Long>("day9.txt") {

    /**
     * Generate all 325 possible pairwise sums of previous 25 elements, for each element.
     * Check if element is in the list.
     *
     * Time Complexity is O(n*(325*2)) (so verging on n^2)
     *   325 operations to generate pairwise sums
     *   325 operations to check if current number is in the list
     *
     * Space Complexity is O(325) - just need one array to hold pairwise sums.
     */
    override fun part1(): Int {

        for (i in 25..input.size) {
            val sums = getPairSums(i)
            if (input[i].toInt() !in sums) return input[i].toInt()
        }
        throw Exception("Not found")
    }

    /**
     * Returns array of all possible combinations of pairwise sums of previous 25
     *   in input, with respect to function parameter.
     */
    private fun getPairSums(curr: Int): IntArray {
        val arr = IntArray(325)
        var a = 0
        for (i in curr-25 until curr) {
            for (j in i+1 until curr) {
                arr[a++] = input[i].toInt() + input[j].toInt()
            }
        }
        return arr
    }

    /**
     * Dynamic Programming?
     * Use 2D array, where each row holds aggregate sums of sequential numbers, starting with the input at the row number.
     * So matrix[i,j] will hold (input[i] + input[i+1] + input[i+2] ... + input[j]).
     * Once the target is found, use the indices (i and j) as lower and upper bounds of an input sublist.
     * Add min and max from that sublist.
     *
     * Time Complexity is O(n^2)
     *   Every input element is read only once, but it is used n-1 times to form its column in the matrix.
     *   I think this means it's considered n^2, but in reality its more like (n^2)/2 (half the matrix is unpopulated)
     *
     * Space Complexity is O(n^2)
     *   Use an nxn matrix
     */
    override fun part2(): Long {
        val target = 1212510616L
        val matrix = Array(input.size) { LongArray(input.size) }

        for (i in input.indices) {
            for (n in 0 until i) {
                if (n != i-1) {
                    matrix[n][i] = matrix[n][i-1] + input[i].toInt()
                } else {
                    matrix[n][i] = input[n].toLong() + input[i].toLong()
                }
                // Check for solution presence
                if (matrix[n][i] == target) {
                    val solutionRow = input.subList(n, i).map { it.toLong() } // Was trying to keep everything in arrays, but nah
                    return solutionRow.minOrNull()!! + solutionRow.maxOrNull()!!
                }
            }
        }
        throw Exception("Not found")
    }
}
