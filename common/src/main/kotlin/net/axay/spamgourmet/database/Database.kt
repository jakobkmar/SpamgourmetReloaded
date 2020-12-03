package net.axay.spamgourmet.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmet.data.*
import net.axay.spamgourmet.main.Values

class Database(mongoDB: MongoDB) {

    val userData =
        mongoDB.getCollectionOrCreate<UserData>("${Values.PROJECT_PREFIX}user_data")

    val userAddressData =
        mongoDB.getCollectionOrCreate<UserAddressData>("${Values.PROJECT_PREFIX}user_address_data")
    val answerAddressData =
        mongoDB.getCollectionOrCreate<AnswerAddressData>("${Values.PROJECT_PREFIX}answer_address_data")

    val bounceData =
        mongoDB.getCollectionOrCreate<BounceData>("${Values.PROJECT_PREFIX}bounce_data")
    val userBounceData =
        mongoDB.getCollectionOrCreate<UserBounceData>("${Values.PROJECT_PREFIX}user_bounce_data")

    val answerBounceAddressData =
        mongoDB.getCollectionOrCreate<AnswerBounceAddressData>("${Values.PROJECT_PREFIX}answer_bounce_address_data")
    val spamBounceAddressData =
        mongoDB.getCollectionOrCreate<SpamBounceAddressData>("${Values.PROJECT_PREFIX}spam_bounce_address_data")

}