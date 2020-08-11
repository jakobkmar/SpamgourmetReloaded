package net.axay.spamgourmetreloaded.mail

import kotlin.random.Random
import kotlin.random.nextLong

object SpamgourmetAddressGenerator {

    // TODO do not use hashCode()
    fun generateAnswerAddress(username: String, answerToAddress: String): String {

        val keyString = generateKeyFromString(username + answerToAddress)

        // TODO use ifAlreadyExists
        while (false) {

        }

        return "$keyString.answer@axay.net"

    }

    private fun generateKeyFromString(string: String): String {

        val weakSeed = string.hashCode()
        val weakRandom = Random(weakSeed)

        val strongSeed = kotlin.run {
            val builder = StringBuilder()
            while (builder.length < 18)
                builder.append(Random(string[weakRandom.nextInt(string.length)].hashCode()).nextInt(10).toString())
            return@run if (builder.length <= 18) builder.toString() else builder.toString().slice(0 until 19)
        }
        val strongRandom = Random(strongSeed.toLong())

        val key = weakRandom.nextLong(1000_0000_0000_0000 until 1_0000_0000_0000_0001)
        return if (key == 1_0000_0000_0000_0000) "0000000000000000" else key.toString()

    }

}

fun main() {
    println(SpamgourmetAddressGenerator.generateAnswerAddress("blue", "info@insta.net"))
    println(SpamgourmetAddressGenerator.generateAnswerAddress("blue", "info@insta.neta"))
    println(SpamgourmetAddressGenerator.generateAnswerAddress("bluea", "info@insta.neta"))
}