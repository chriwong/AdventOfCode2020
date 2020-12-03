/**
 * Part 1 answer: 181
 * Part 2 answer: 1260601650
 */
class Day3 : Day<Int,Long>("day3.txt") {

    private val sparseMatrix = SparseMatrix()
    private val rowCount = input.size
    private val colCount = input[0].length  // could move these into SparseMatrix I guess
    // Create sparse matrix
    init {
        for (row in input.indices) {
            addLineOfTrees(input[row], row)
        }
    }

    private fun addLineOfTrees(line: String, lineNum: Int) {
        for (i in line.indices) {
            if (line[i] == '#') {
                sparseMatrix.add(lineNum, i)
            }
        }
    }

    /**
     * Create sparse matrix to represent tree locations (this is completed in class constructor/init)
     * Infinite-recurring 'eastward' map can be handled by modulo (%) of coordinate with row length
     * Check matrix at each location, incrementing position after each check
     * Time Complexity: O(n + n = 2n)? (I know coefficients are ignored, but I like to acknowledge them for real-life considerations)
     *   n for creating sparse matrix
     *   another n for iterating through matrix after creation (in effect this is more like n/rowlength because it skips down rows)
     *   * This is assuming n is total characters, not rows of text
     */
    override fun part1(): Int {
        var treesHit = 0
        val pos = arrayOf(0,0)
        while (pos[0] < rowCount) {
            pos[0]++
            pos[1]+=3
            if (sparseMatrix.get(pos[0], pos[1]%colCount)){
                treesHit++
            }
        }
        return treesHit
    }

    /**
     * With a sparse matrix already created, the approach is similar to part 1
     * Five sets of synchronized coordinates used to check positions in sparse matrix.
     * There may be some kind of advantage to calculating intersections/multiples of {1,3,5,7}
     *   so that redundant checking is avoided, but I think the speed improvement is negligible.
     * Time complexity: O(n + 4n + n = 6n)
     *   n for creating sparse matrix
     *   4n for first four 'toboggan slopes' (again like above, this is actually n/rowLength)
     *   n for last 'toboggan slope' because it travels down two rows at a time (yet again, more like n/(2*rowLength) )
     */
    override fun part2(): Long {
        val hitCounts = arrayOf(0,0,0,0,0)

        // First four slopes
        val positions = arrayOf(0,0,0,0,0)
        var r = 0

        while (r < rowCount) {
            positions[0]++
            positions[1]+=3
            positions[2]+=5
            positions[3]+=7
            r++

            // forEachIndexed() was giving incorrect answer because it included last index in positions[] array,
            //   which is supposed to remain untouched until the single-row slopes have been traversed.
            // It resulted in all trees in first column to be counted.
            for (i in 0..3) {
                if (sparseMatrix.get(r, positions[i]%colCount)){
                    hitCounts[i]++
                }
            }
        }

        // Last slope, which descends by two rows at a time
        r = 0
        while (r < rowCount) {
            positions[4]++
            r+=2
            if (sparseMatrix.get(r, positions[4]%colCount)) {
                hitCounts[4]++
            }
        }

        for (count in hitCounts) {
            println("$count")
        }

        // calculate total as Long
        var total: Long = 1
        for (count in hitCounts) {
            total *= count
        }
        return total
    }

    private class SparseMatrix {
        private val matrix: MutableList<Pair<Int,Int>> = mutableListOf()

        fun add(row: Int, col: Int) {
            matrix.add(Pair(row, col))
        }

        fun get(row: Int, col: Int): Boolean {
            return matrix.firstOrNull {it.first == row && it.second == col} != null
        }
    }
}
