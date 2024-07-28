package edu.udel.util

import com.google.common.base.CaseFormat
import opennlp.tools.dictionary.Dictionary
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import org.apache.commons.lang.StringUtils
import java.io.FileInputStream
import java.util.*
import java.io.IOException
import java.io.FileReader
import java.io.BufferedReader

val setOfBadElements = hashSetOf("Not","And","Or","To","After","Also","Last","Still","Then", "For")

fun checkForWord(word: String): Boolean {
    val newWord = word.toLowerCase()
    try {
        val words = BufferedReader(FileReader("/usr/share/dict/words"))
        words.useLines {  line -> if (line.indexOf(newWord) != -1) {
                return true
            }
        }
        words.close()
    } catch (e: IOException) {
        println("Reader exception!")
    }
    return false
}

fun hasDigit(bit: String): Boolean{
    val arr = bit.toCharArray()
    for (c in arr){
        if (c.isDigit()){
            return true
        }
    }
    return false
}

fun parseTestNameWithVerification (name : String): List<String>{

    val splitting1 = StringUtils.splitByCharacterType(name).asList()
    val splitting2 = StringUtils.splitByCharacterTypeCamelCase(name).asList()
    val countUpperCase = name.split("(?<=[A-Z]{3,})(?!=[a-z])|(?<=[a-z])(?=[A-Z]{3,})".toRegex())
    if (countUpperCase.any {element -> element.toCharArray().all { c -> c.isUpperCase() }}){
//        println("c4")
        return splitting2.subList(1, splitting2.size).filterNot { m -> hasDigit(m) }.filter { m -> !setOfBadElements.contains(m)}
    }
    if (splitting1.all { checkForWord(it)}){
//        println("c1")
        return splitting1.subList(1, splitting1.size).filterNot { m -> hasDigit(m) }.filter { m -> !setOfBadElements.contains(m)}
    }
    if (splitting2.all { checkForWord(it)}){
//        println("c2")
        return splitting2.subList(1, splitting2.size).filterNot { m -> hasDigit(m) }.filter { m -> !setOfBadElements.contains(m)}
    }
    return emptyList()
}

fun parseTestName (name: String): List<String>{

    var newName = name.removePrefix("test").removePrefix("Test").removeSuffix("Test")

    if (newName.contains("_")){
        newName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newName)
        println(newName)
    }

    val result = newName.split("(?<![0-9])(?=[A-Z])".toRegex())
    val newResult = LinkedList<String>()

    for (re in result){

        if (!re.isEmpty()){
            if (hasDigit(re)) {
                newResult.add(re.replace("[0-9]".toRegex(),""))
            } else{
                newResult.add(re)
            }
        }
    }
    return newResult.filter { m -> !setOfBadElements.contains(m)}
}

//fun parseUniqueBits(path: String, name: String ){
//
////    val uniqueBits = LinkedList<String>()
////    val path = "/Users/wujianwei/IdeaProjects/concept-analysis-plugin/resources"
////    parseUniqueBits(path, uniqueBits)
//
//    val inputStream = FileInputStream("$path/en-token.bin")
//    val inputStreamPOS = FileInputStream("$path/en-pos-perceptron.bin")
//
//    val model = TokenizerModel(inputStream)
//    val tokenizer = TokenizerME(model)
//
//    val modelPOS = POSModel(inputStreamPOS)
//    val tagger = POSTaggerME(modelPOS)
//
//    val nameBitsArray = name.split("(?<![0-9])(?=[A-Z])".toRegex())
//    val testName = StringUtils.join(nameBitsArray, " ")
//
//    val tokens = tokenizer.tokenize(testName)
//    val tags = tagger.tag(tokens)
//
//    for (i in 0 until tokens.size) {
//
//        val word = tokens[i].trim()
//        val tag = tags[i].trim()
//
//        print("$tag:$word \n")
//    }
//
//    inputStream.close()
//
//}

//    println(name.split("(?<=[A-Z])(?=[a-z])|(?<=[a-z])(?=[A-Z])".toRegex()))
//    println(name.split("(?=[A-Z])(?<=[A-Z])(?=[a-z])".toRegex()))
//afro
//ambi
//amphi
//ana
//anglo
//apo
//astro
//bi
//bio
//circum
//cis
//co
//col
//com
//con
//contra
//cor
//cryo
//crypto
//de
//de
//demi
//di
//dif
//dis
//du
//duo
//eco
//electro
//em
//en
//epi
//euro
//ex
//franco
//geo
//hemi
//hetero
//homo
//hydro
//hypo
//ideo
//idio
//il
//im
//infra
//inter
//intra
//ir
//iso
//macr
//mal
//maxi
//mega
//megalo
//micro
//midi
//mini
//mis
//mon
//multi
//neo
//omni
//paleo
//para
//ped
//peri
//poly
//pre
//preter
//proto
//pyro
//re
//retro
//semi
//socio
//supra
//sur
//sy
//syl
//sym
//syn
//tele
//trans
//tri
//twi
//ultra
//un
//uni