package net.axay.spamgourmet.database

import com.mongodb.client.model.IndexOptions
import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmet.database.data.AnswerAddressData
import net.axay.spamgourmet.database.data.BounceAddressData
import net.axay.spamgourmet.database.data.UserAddressData
import net.axay.spamgourmet.database.data.UserData
import net.axay.spamgourmet.main.ValueHolder.DATA_PREFIX
import org.litote.kmongo.descendingIndex

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class DataManager(val mongoDB: MongoDB) {

    val userCollection = mongoDB.getCollection<UserData>("${DATA_PREFIX}users") {
        it.createIndex(descendingIndex(UserData::username), IndexOptions().unique(true))
    } ?: throw IllegalStateException("The user collection could not be loaded!")


    val userAddressCollection = mongoDB.getCollection<UserAddressData>("${DATA_PREFIX}user_addresses") {
        it.createIndex(descendingIndex(UserAddressData::address), IndexOptions().unique(true))
    } ?: throw IllegalStateException("The user addresses collection could not be loaded!")

    val answerAddressCollection = mongoDB.getCollection<AnswerAddressData>("${DATA_PREFIX}answer_addresses") {
        it.createIndex(descendingIndex(AnswerAddressData::address), IndexOptions().unique(true))
    } ?: throw IllegalStateException("The answer addresses collection could not be loaded!")


    val spamBounceAddressCollection = mongoDB.getCollection<BounceAddressData>("${DATA_PREFIX}spam_bounce_addresses") {
        it.createIndex(descendingIndex(BounceAddressData::address), IndexOptions().unique(true))
    } ?: throw IllegalStateException("The spam bounce address collection could not be loaded!")

    val answerBounceAddressCollection = mongoDB.getCollection<BounceAddressData>("${DATA_PREFIX}answer_bounce_addresses") {
        it.createIndex(descendingIndex(BounceAddressData::address), IndexOptions().unique(true))
    } ?: throw IllegalStateException("The answer bounce address collection could not be loaded!")

}