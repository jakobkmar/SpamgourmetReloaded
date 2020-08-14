package net.axay.spamgourmetreloaded.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmetreloaded.database.data.AnswerAddressData
import net.axay.spamgourmetreloaded.database.data.BounceAddressData
import net.axay.spamgourmetreloaded.database.data.UserAddressData
import net.axay.spamgourmetreloaded.database.data.UserData
import net.axay.spamgourmetreloaded.main.ValueHolder.DATA_PREFIX
import org.litote.kmongo.descendingIndex

class DataManager(mongoDB: MongoDB) {

    val userCollection = mongoDB.getCollection<UserData>("${DATA_PREFIX}users") {
        it.createIndex(descendingIndex(UserData::username))
    } ?: throw IllegalStateException("The user collection could not be loaded!")


    val userAddressCollection = mongoDB.getCollection<UserAddressData>("${DATA_PREFIX}user_addresses") {
        it.createIndex(descendingIndex(UserAddressData::address))
    } ?: throw IllegalStateException("The user addresses collection could not be loaded!")

    val answerAddressCollection = mongoDB.getCollection<AnswerAddressData>("${DATA_PREFIX}answer_addresses") {
        it.createIndex(descendingIndex(AnswerAddressData::address))
    } ?: throw IllegalStateException("The answer addresses collection could not be loaded!")


    val spamBounceAddressCollection = mongoDB.getCollection<BounceAddressData>("${DATA_PREFIX}spam_bounce_addresses") {
        it.createIndex(descendingIndex(BounceAddressData::address))
    } ?: throw IllegalStateException("The spam bounce address collection could not be loaded!")

    val answerBounceAddressCollection = mongoDB.getCollection<BounceAddressData>("${DATA_PREFIX}answer_bounce_addresses") {
        it.createIndex(descendingIndex(BounceAddressData::address))
    } ?: throw IllegalStateException("The answer bounce address collection could not be loaded!")


}