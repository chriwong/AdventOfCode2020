/**
 * Part 1 answer: 1832
 * Part 2 answer: 662
 */
class Day8 : Day<Int,Int>("day8.txt") {

    /**
     * I'm lazy and doing naive/brute force:
     * Store input as array of strings. Track "program counter" along with total accumulator.
     * Also store an array of bools representing which instructions have been executed already.
     * Jump around the array using the pc and execute after checking the "already executed" array.
     * Soon as the pc reaches an instruction that has been already executed, return.
     * Time Complexity is O(2n)
     *   n for reading input
     *   n for executing every instruction (worst-case)
     * Memory Complexity is O(2n)
     *   n for storing input
     *   n for storing "already executed" boolean
     */
    override fun part1(): Int {
        var solution = 0
        var pc = 0
        val alreadyExecuted = BooleanArray(input.size)

        while (true) {
            if (alreadyExecuted[pc]) break

            alreadyExecuted[pc] = true

            val instruction = input[pc].split(' ')
            when (instruction[0]) {
                "nop" -> {
                    pc++
                }
                "acc" -> {
                    solution += instruction[1].toInt()
                    pc++
                }
                "jmp" -> {
                    pc += instruction[1].toInt()
                }
            }
        }

        return solution
    }


    /**
     * This is puzzling - no algorithm/data structure comes to mind immediately.
     * Picturing a digraph, the point at which a loop occurs would be the final node before a duplicate instruction is executed.
     * So if you just flip the last jump before the duplicate, it should proceed.
     * But this jump may not be the instruction immediately before the duplicate;
     *   there may be a few instructions between the faulty jump and the first duplicate.
     * ...
     * Nope that won't work - the bad instruction can be anywhere, not necessarily at the end.
     *
     * Next approach: keep a stack of jumps/nops plus the PC and accumulator value - this is like the "state".
     * Once a duplicate is hit, work backwards by loading the "state" into the machine
     *   and seeing if flipping the instruction results in success.
     *
     * Ok that ^ worked.
     * Time Complexity is O(2n + n^2 == n^2)
     *   n for reading input
     *   n for first run of program to build stack
     *   n^2 (worst-case) for testing each jmp/nop change.
     *     Absolute worst case is the bottom of stack is the critical instruction,
     *     and that almost the whole program runs before a instruction is repeated.
     * Space complexity is O(4n)
     *   n for holding input
     *   n for "already executed" array
     *   n for *another "already executed" array each time a state is tested
     *   n for mutable instruction set
     */
    override fun part2(): Int {
        var solution = 0
        var pc = 0
        val alreadyExecuted = BooleanArray(input.size)
        val stateStack: ArrayDeque<State> = ArrayDeque()
        val inputCopy = input.toMutableList()

        // Try to execute entire program, while building stack of jmp/nop
        while (true) {
            if (alreadyExecuted[pc]) break

            alreadyExecuted[pc] = true

            val instruction = input[pc].split(' ')
            when (instruction[0]) {
                "nop" -> {
                    stateStack.addFirst(State(pc, solution))
                    pc++
                }
                "acc" -> {
                    solution += instruction[1].toInt()
                    pc++
                }
                "jmp" -> {
                    stateStack.addFirst(State(pc, solution))
                    pc += instruction[1].toInt()
                }
            }
        }

        println("First run complete - hit duplicate\nStack size: ${stateStack.size}")

        while (stateStack.isNotEmpty()) {
            val state = stateStack.removeFirst()

            // flip instruction at state.pc
            flipInstruction(state.pc, inputCopy)

            // reset pc, acc to new (really it's old) state
            try {
                return executeProgram(state, BooleanArray(input.size), inputCopy)
            } catch (e: Exception) {
                println("Program infinite loop. Flipping back...")
                flipInstruction(state.pc, inputCopy)
            }
        }
        return solution
    }

    private fun flipInstruction(pc: Int, program: MutableList<String>) {
        println("Flipping instruction #${pc}")
        when (program[pc].substring(0,3)) {
            "jmp" -> program[pc] = "nop" + program[pc].substring(3)
            "nop" -> program[pc] = "jmp" + program[pc].substring(3)
        }
    }

    /**
     * @param state desired state at which to start program
     * @param alreadyExecuted array of which instructions have already been executed
     * @param program list of instructions (the puzzle input, but with an instruction changed)
     * Runs the given program starting from the given state.
     * If program completes successfully, returns accumulator value. Otherwise throws error.
     *   (I would've returned -1, but wasn't sure if negatives were valid accumulator values)
     */
    private fun executeProgram(state: State, alreadyExecuted: BooleanArray, program: List<String>): Int {
        var partialSolution = 0
        var partialPC = state.pc

        while (partialPC < program.size) {
            if (alreadyExecuted[partialPC]) {
                throw Exception()
            }
            alreadyExecuted[partialPC] = true

            val instruction = program[partialPC].split(' ')
            when (instruction[0]) {
                "nop" -> {
                    partialPC++
                }
                "acc" -> {
                    partialSolution += instruction[1].toInt()
                    partialPC++
                }
                "jmp" -> {
                    partialPC += instruction[1].toInt()
                }
            }
        }

        return state.acc + partialSolution
    }

    // Represents the program at a given point in time
    private data class State(val pc: Int, val acc: Int)
}
