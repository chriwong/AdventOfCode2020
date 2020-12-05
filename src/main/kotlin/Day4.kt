/**
 * Part 1 answer: 233
 * Part 2 answer: 111
 */
class Day4 : Day<Int,Int>("day4.txt") {

    /**
     * "Passports" are separated by line breaks.
     * Split lines with space character delimiter, put all splits found between newlines into groups.
     * Valid passports have either seven or eight fields - anything else is invalid.
     * Eight fields is automatically valid.
     * Seven fields is valid if only "cid" is missing, which would be the 2nd field in a sorted list.
     * So, sort each group of seven and check if that's the case.
     * Time Complexity is O(n...)
     *   Not sure how to guage this one, because there is sorting being done, but since it's only
     *   for groups of 8 or fewer, I imagine that would be treated as constant operation. But
     *   there is also String.split() and I'm not sure how that's implemented.
     */
    override fun part1(): Int {
        var totalValid = 0
        val fieldList = mutableListOf<String>()
        var passportNumber = 0

        for (line in input) {
            if (line != "") {
                fieldList.addAll(line.split(' '))
            } else {
                if (checkPassportPart1(fieldList)) {
                    totalValid++
                }/* else {
                    println("Invalid passport at line $passportNumber")
                }*/
                fieldList.clear()
                passportNumber++
            }
        }
        // one additional check because it jumps out when reaching final empty line
        if (checkPassportPart1(fieldList)) {
            totalValid++
        } else {
            println("Invalid passport at line $passportNumber")
        }

        return totalValid
    }

    private fun checkPassportPart1(fields: List<String>): Boolean {
        return when (fields.size) {
            8 -> true
            7 -> checkFieldsPart1(fields.sorted())
            else -> false
        }
    }

    /**
     * Returns true if the only missing field in the sorted list is "cid"
     */
    private fun checkFieldsPart1(fields: List<String>): Boolean {
        return (
            fields[0].subSequence(0,3) == "byr" &&
            fields[1].subSequence(0,3) == "ecl" &&
            fields[2].subSequence(0,3) == "eyr" &&
            fields[3].subSequence(0,3) == "hcl" &&
            fields[4].subSequence(0,3) == "hgt" &&
            fields[5].subSequence(0,3) == "iyr" &&
            fields[6].subSequence(0,3) == "pid"
        )
    }


    /**
     * This took way longer than it should have: I had a typo in max height 192 instead of 193 fml.
     * This is not elegant but adheres to object-oriented and single-responsibility principles.
     * The various collection methods in each validation function complicate time complexity,
     *  so I won't try to give one. If you over-simplify things so that Passport.isValid() is considered
     *  single operation, then this is O(n).
     */
    override fun part2(): Int {
        var totalValid = 0
        val fieldList = mutableListOf<String>()
        var n = 0

        for (line in input) {
            if (line != "") {
                fieldList.addAll(line.split(' '))
            } else {
                val fieldMap = fieldListToFieldMap(fieldList)
                val passport = createPassport(fieldMap)
                if (passport.isValid()) {
                    totalValid++
                }
                fieldList.clear()
                n++
            }
        }

        // I didn't fix the fencepost error, so here's the check again after loop finishes
        val fieldMap = fieldListToFieldMap(fieldList)
        val passport = createPassport(fieldMap)
        if (passport.isValid()) {
            totalValid++
        }

        return totalValid
    }

    private fun fieldListToFieldMap(fieldList: List<String>): Map<String, String> {
        val map = mutableMapOf<String,String>()

        for (fieldLine in fieldList) {
            val kvp = fieldLine.split(':')
            map[kvp[0]] = kvp[1]
        }

        return map
    }

    private fun createPassport(fields: Map<String,String>): Passport {
        return Passport(
            byr = fields["byr"]?.toInt(),
            cid = fields["cid"] != null,
            ecl = fields["ecl"],
            eyr = fields["eyr"]?.toInt(),
            hcl = fields["hcl"],
            hgt = fields["hgt"],
            iyr = fields["iyr"]?.toInt(),
            pid = fields["pid"]
        )
    }

    /* Fields are in alphabetical order */
    private data class Passport(val byr: Int?, val cid: Boolean?, val ecl: String?, val eyr: Int?, val hcl: String?, val hgt: String?, val iyr: Int?, val pid: String?) {
        fun isValid(): Boolean {
            return byrValid() && eclValid() && eyrValid() && hclValid() &&
                    hgtValid() && iyrValid() && pidValid()
        }

        fun byrValid(): Boolean {
            if (byr == null) return false
            if (byr !in 1920..2002) {
                println("Bad byr $byr")
                return false
            }
            return true
        }

        fun eclValid(): Boolean {
            if (ecl == null) return false
            if (ecl !in listOf("amb","blu","brn","gry","grn","hzl","oth")) {
                println("Bad ecl $ecl")
                return false
            }
            return true
        }

        fun eyrValid(): Boolean {
            if (eyr == null) return false
            if (eyr !in 2020..2030) {
                println("Bad eyr $eyr")
                return false
            }
            return true
        }

        fun hclValid(): Boolean {
            if (hcl == null) return false
            if (!Regex("#[a-f0-9]{6}").matches(hcl)) {
                println("Bad hcl $hcl")
                return false
            }
            return true
        }

        fun hgtValid(): Boolean {
            if (hgt == null) return false

            val unit = hgt.substring(hgt.length-2, hgt.length)
            if (unit !in listOf("cm","in")) {
                println("Bad hgt units $hgt")
                return false
            }

            val magnitude = hgt.substring(0, hgt.length-2).toInt()

            return when (unit) {
                "cm" -> magnitude in 150..193
                "in" -> magnitude in 59..76
                else -> {
                    println("Bad hgt magnitude: $unit $magnitude")
                    false
                }
            }
        }

        fun iyrValid(): Boolean {
            if (iyr == null) return false
            if (iyr !in 2010..2020) {
                println("Bad iyr $iyr")
                return false
            }
            return true
        }

        fun pidValid(): Boolean {
            if (pid == null) return false
            if (!(pid.length == 9 && Regex("[0-9]{9}").matches(pid))) {
                println("Bad pid $pid")
                return false
            }
            return true
        }
    }
}
