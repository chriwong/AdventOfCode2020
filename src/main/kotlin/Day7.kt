/**
 * Part 1 answer: 242
 * Part 2 answer: 176035
 */
class Day7 : Day<Int,Int>("day7.txt") {

    private val graph = Graph(input)

    /**
     * Represent bags/children as graph using adjacency matrix.
     * Traverse all vertices and track hashmap/"cachemap" as solution and to quick reference if node has been visited.
     * TODO Try my idea of representing map as singleton nodes and traverse from Node("shiny gold") to all parents
     * Time complexity is O(v + e + v+e)?
     *   v for creating indices for all bag colors
     *   e for populating adjacency list
     *   v+e for traversing through list (because of 'visited' map, each node is visited at most twice (no redundant recursion))
     */
    override fun part1(): Int {
        return graph.getShinyGoldCount()
    }

    /**
     * Traverse children of "shiny gold", keeping track of child counts.
     * Changed populateGraph() to allow for storing {"no other":0} entries in map (though I *just realized it could be replaced with .isEmpty())
     * Time complexity is O(v + e + v+e) for same reasons as above.
     *   In practice, the last (v+e) is actually just the descendents of "shiny gold", which I doubt is all of the other bags.
     *   But worst-case would be every single bag is a descendent of "shiny gold".
     */
    override fun part2(): Int {
        return graph.shinyGoldBagCount()
    }

    /**
     * Represents which bags can be stored in which other bags.
     * Uses adjacency list: array of map, where each element in array represents parent bag, and map holds pairs of {child color : count}
     */
    private class Graph(input: List<String>) {
        // Each bag has at most 3 children, so adjacency list is better representation than adjacency matrix
        val adjacencyList = Array(input.size) { mutableListOf<Pair<String,Int>>()}
        val colorToIndex = mutableMapOf<String,Int>()

        init {
            this.translateColors(input)
            this.populateGraph(input)
        }

        /**
         * Map all adjective+color combinations to an integer for use in adjacency list array
         */
        private fun translateColors(input: List<String>) {
            for (i in input.indices) {
                val color = input[i].substringBefore(" bags")
                colorToIndex[color] = i
            }
        }

        /**
         * Get list of counts of child colors and add to map at parent bag's index in adjacency list array.
         */
        private fun populateGraph(input: List<String>) {
            for (line in input) {
                val parentBagColor = line.substringBefore(" bags")
                val childBags = line.substringAfter("contain ").split(Regex(" bags?(, |.)"))

                for (child in childBags) {
                    if (child == "") continue
                    else if (child == "no other") {
                        this.setEdge(parentBagColor, "no other", 0)
                    } else {
                        val childBagColor = child.substring(2) // I verified that no parent has > 9 of one child (search input for regex [0-9]{2}).
                        val childBagCount = child[0].toInt()-48 // String indexing returns Chars, and Char.toInt() returns ASCII, so -48 is needed.
                        this.setEdge(parentBagColor, childBagColor, childBagCount)
                    }
                }
            }
        }

        /**
         * Adds {child color : count} to map after converting parent color to an index
         */
        fun setEdge(from: String, to: String, weight: Int) {
            val fromIndex = colorToIndex.getValue(from)
            adjacencyList[fromIndex].add(Pair(to,weight))
        }

        /**
         * Calls the recursive function for each parent bag in the adjacency list array, counting which did contain shiny gold in descendents graph
         */
        fun getShinyGoldCount(): Int {
            val visited = mutableMapOf<String,Boolean>()
            var shinyGoldCount = 0

            for (bagColor in colorToIndex.keys) {
                if (hasShinyGold(bagColor, visited)) shinyGoldCount++
            }
            println("Visited count: $visited\nVisited list size: ${visited.size}")
            return shinyGoldCount
        }

        /**
         * If "shiny gold" is in this map of for this bag color, or in the "cache" visited map, return true.
         * Else recursively call this function with the other bags in the map.
         */
        private fun hasShinyGold(bagColor: String, visitedMap: MutableMap<String, Boolean>): Boolean {
            // Check "cache" to avoid graph traversal
            if (visitedMap[bagColor] != null) {
                return visitedMap[bagColor]!!
            }

            val colorIndex = colorToIndex[bagColor]!!
            var foundInChildren = false

            for (childBag in adjacencyList[colorIndex]) {

                when (childBag.first) {
                    "shiny gold" -> {
                        visitedMap[bagColor] = true
                        return true
                    }
                    "no other" -> {
                        return false
                    }
                    else -> {
                        foundInChildren = foundInChildren.or(hasShinyGold(childBag.first, visitedMap))
                    }
                }
            }

            return if (foundInChildren) {
                visitedMap[bagColor] = true
                true
            } else {
                false
            }
        }

        /**
         * Calls recursive function for Part 2
         */
        fun shinyGoldBagCount(): Int {
            return getBagCount("shiny gold")
        }

        /**
         * Return 0 if bag has no children.
         * Else return sum of immediate children bags plus their recursive returns.
         */
        private fun getBagCount(bagColor: String): Int {
            val index = colorToIndex[bagColor]!!

            return if (adjacencyList[index][0].first == "no other") {
                0
            } else {
//                val immediateChildrenCount = adjacencyList[index].reduce { acc, pair -> Pair("", acc.second+pair.second) }.second
                var childrenBagCount = 0
                for (child in adjacencyList[index]) {
                    // Add immediate child count + immediate child count * all children of that bag (missed this multiplication step initially)
                    childrenBagCount += child.second + child.second*getBagCount(child.first)
                }
                childrenBagCount
            }
        }
    }
}
