package edu.udel.stats

import kotlin.math.roundToInt

class AnalyzeData{

    var numOfTests = 0.0
    var numOfTestsNone = 0.0
    var numOfTier0 = 0.0
    var numOfTier1 = 0.0
    var numOfTier2 = 0.0
    var numOfTier3 = 0.0
    var numOfTier4 = 0.0
    var numOfTier5 = 0.0
    var numOfTier6 = 0.0
    var unknownTier = 0.0

    fun calculatePercentage() {
        println("Number of tests: $numOfTests")
        println("Number of tests (non-empty): $numOfTestsNone")
        println("Under Level 0: ${((numOfTier0 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier0)")
        println("Under Level 1: ${((numOfTier1 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier1)")
        println("Under Level 2: ${((numOfTier2 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier2)")
        println("Under Level 3: ${((numOfTier3 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier3)")
        println("Under Level 4: ${((numOfTier4 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier4)")
        println("Under Level 5: ${((numOfTier5 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier5)")
        println("Under Level 6: ${((numOfTier6 / numOfTests)*1000).roundToInt()/10.0} % ($numOfTier6)")
        println("Under Unknown Level: ${((unknownTier / numOfTests)*1000).roundToInt()/10.0} % ($unknownTier)")
        println("Under Level 0+1: ${(((numOfTier0+numOfTier1) / numOfTests)*1000).roundToInt()/10.0} %")
        println("Under Level 0+1+2: ${(((numOfTier0+numOfTier1+numOfTier2) / numOfTests)*1000).roundToInt()/10.0} %")
        println("Under Level 0+1+2+3: ${(((numOfTier0+numOfTier1+numOfTier2+numOfTier3) / numOfTests)*1000).roundToInt()/10.0} %")
        println("Under Level 0+1+2+3+4: ${(((numOfTier0+numOfTier1+numOfTier2+numOfTier3+numOfTier4) / numOfTests)*1000).roundToInt()/10.0} %")
        println("Under Level 0+1+2+3+4+5: ${(((numOfTier0+numOfTier1+numOfTier2+numOfTier3+numOfTier4+numOfTier5) / numOfTests)*1000).roundToInt()/10.0} %")
        println("Under Level 0+1+2+3+4+5+6: ${(((numOfTier0+numOfTier1+numOfTier2+numOfTier3+numOfTier4+numOfTier5+numOfTier6) / numOfTests)*1000).roundToInt()/10.0} %")
    }
}