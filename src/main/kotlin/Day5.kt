/**
 * Part 1 answer: 832
 * Part 2 answer: 517
 */
class Day5 : Day<Int, Int>("day5.txt") {

    /**
     * Track upper and lower bounds (initially 127 and 0 respectively),
     *   one of which will change for each letter in the line.
     * The magnitude by which the bounds change starts at 64 (half of 128) and is halved each iteration.
     * There are only eight possible columns, so I just hardcoded them.
     * Update maximum seat ID after calculating each position.
     * Time complexity is O(n). Each letter is used exactly once.
     */
    override fun part1(): Int {
        var max = 0

        for (line in input) {
            val seat = getSeatPair(line)
            val seatId = (seat.first*8)+seat.second
            max = if (max > seatId) max else seatId
        }
        
        return max
    }
    
    private fun getSeatPair(rowCode: String): Pair<Int,Int> {
        var up = 127
        var low = 0
        var mag = 64

        for (i in 0..6) {
            when (rowCode[i]) {
                'F' -> up -= mag
                'B' -> low += mag
            }
            mag /= 2
        }

        val col = when (rowCode.subSequence(7,10)) {
            "LLL" -> 0
            "LLR" -> 1
            "LRL" -> 2
            "LRR" -> 3
            "RLL" -> 4
            "RLR" -> 5
            "RRL" -> 6
            "RRR" -> 7
            else -> -1
        }
        // If everything went right, `up` and `low` should be equal
        return Pair(low,col)
    }


    /**
     * A little confused by question prompt - how many seats will be missing from front and back?
     * Initial instinct is to use math: calculate total sum of all seat IDs, the subtract as they are found in list.
     *
     * Aha - keep track of minimum and maximum seats, as well as cumulative sum of seat IDs.
     * Then calculate total seat ID sum based off of those bounds and find difference.
     *   Eg. minimum seat is row 5, column 5; maximum seat is row 124, column 1
     *   Calculate total from 0 to 124 plus one additional seat, then subtract 0 to 5 plus 5 additional seats.
     *   Use formula "Sum from 1 to n of n = n*(n+1)/2"
     */
    fun part2_fail(): Int {
        var cumulativeTotal = 0
        var minPair = Pair(127,7)
        var maxPair = Pair(0,0)

        for (line in input) {
            // Use function from Part 1 to get Pair representing seat, then calculate seat ID
            val seatPair = getSeatPair(line)
            println(seatPair)
            val seatId = seatPair.first*8 + seatPair.second

            minPair = if (minPair < seatPair) minPair else seatPair
            maxPair = if (maxPair > seatPair) maxPair else seatPair

            cumulativeTotal += seatId
        }

        println("Min pair: $minPair\nMax pair: $maxPair")

        val totalFromMax = (8*maxPair.first * (8*maxPair.first+1) / 2) + maxPair.second //TODO - This doesn't account for 8x or for seats between column 0 and current
        val missingFromMin = (8*minPair.first * (8*minPair.first+1) / 2) + minPair.second //TODO - This doesn't account for 8x or for seats between column 0 and current
        val calculatedTotal = totalFromMax - missingFromMin

        println("Total from input: $cumulativeTotal")
        println("Total from min/max calculations: $calculatedTotal")

        return calculatedTotal - cumulativeTotal
    }

    /**
     * I could not get my original math-focused solution to work :(
     * This solution is less elegant, but somewhat more intuitive than plain brute-force:
     * Create fixed-size array of 128 elements, each cell holding summation of seat IDs for that row.
     *   E.g. [0+1+2+3...+7=28, 8+9+10+...15=92, ...]
     * As each line is parsed, subtract that seat ID value from corresponding cell in array.
     * At the end, all cells (except front and back) should be empty except one.
     * I suppose the final search for the single nonzero element could be improved in the following:
     *   Keep track of minimum seat number, start searching from there until *first nonzero element is found.
     *   This would reduce unnecessary checks by Collection.filter() since it would stop at first hit.
     *   This whole searching afterwards could also be avoided if a hashmap was kept, with zero-value
     *     cells being removed as the input was traversed...?
     * Time complexity is O(3n):
     *   n to create fixed-size array of length n
     *   n to iterate through input and subtract from array
     *   n to search array
     */
    override fun part2(): Int {
        val arr = generateSeatIdTotalsArray()

        for (line in input) {
            val seatPair = getSeatPair(line)
            arr[seatPair.first] -= 8*seatPair.first + seatPair.second
        }

        val answerSingle = arr.filterIndexed { i, cell ->
            i != 0 &&
            i != 127 &&
            cell != 0 &&
            arr[i-1]==0 &&
            arr[i+1]==0
        }

        return answerSingle[0]
    }

    /**
     * Dynamic program-y way to create array where each cell contains summation of seat ID values for that row.
     * (Σn, n=m->n) == ((Σn, n=0->n) - (Σm, m=0->m)) for 1 < m < n
     * So if you keep a running total of each cell, you can use formula for "First n natural numbers"
     *   for a current cell and subtract the running total to get just eight values for
     *   seats (8*row+0), (8*row+1), (8*row+2), etc.
     */
    private fun generateSeatIdTotalsArray(): IntArray {
        val arr = IntArray(128)
        var sub = 28
        for (i in 0..127) {
            if (i == 0) arr[i] = 28
            else {
                // summation formula, but n = 8(i+1)-1 == 8i+7
                // so n(n+1)/2 becomes (8i+7)(8i+8)/2
                arr[i] = ((8*i+7)*(8*i+8) / 2) - sub // does this count as dynamic programming...?
                sub += arr[i]
            }
        }
        return arr
    }
}

// This was for original attempt at Part 2
// Compare Pair<Int, Int> based off of first then second
private operator fun Pair<Int, Int>.compareTo(other: Pair<Int, Int>): Int {
    return when {
        this.first > other.first -> 1
        this.first < other.first -> -1
        else -> {
            return when {
                this.second > other.second -> 1
                this.second < other.second -> -1
                else -> 0
            }
        }
    }
}
