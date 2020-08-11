package net.axay.spamgourmetreloaded.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmetreloaded.database.data.*
import net.axay.spamgourmetreloaded.main.ValueHolder.DATA_PREFIX

class DataManager(mongoDB: MongoDB) {

    val userCollection = mongoDB.getCollection<UserData>("${DATA_PREFIX}users")
            ?: throw IllegalStateException("The user collection could not be loaded!")

    val userAddressCollection = mongoDB.getCollection<UserAddressData>("${DATA_PREFIX}user_addresses")
            ?: throw IllegalStateException("The user addresses collection could not be loaded!")
    val answerAddressCollection = mongoDB.getCollection<AnswerAddressData>("${DATA_PREFIX}answer_addresses")
            ?: throw IllegalStateException("The answer addresses collection could not be loaded!")

    val spamBounceAddressCollection = mongoDB.getCollection<BounceAddressData>("${DATA_PREFIX}spam_bounce_addresses")
            ?: throw IllegalStateException("The spam bounce address collection could not be loaded!")
    val answerBounceAddressCollection = mongoDB.getCollection<BounceAddressData>("${DATA_PREFIX}answer_bounce_addresses")
            ?: throw IllegalStateException("The answer bounce address collection could not be loaded!")

}