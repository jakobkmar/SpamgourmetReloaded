package net.axay.spamgourmet.common.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmet.common.data.*
import net.axay.spamgourmet.common.main.Values

class Database(mongoDB: MongoDB) {

    val userData =
        mongoDB.getCollectionOrCreate<UserData>("${Values.DATA_PREFIX}user_data")
    val userLoginData =
        mongoDB.getCollectionOrCreate<UserLoginData>("${Values.DATA_PREFIX}user_login_data")

    val userAddressData =
        mongoDB.getCollectionOrCreate<UserAddressData>("${Values.DATA_PREFIX}user_address_data")
    val answerAddressData =
        mongoDB.getCollectionOrCreate<AnswerAddressData>("${Values.DATA_PREFIX}answer_address_data")

    val bounceData =
        mongoDB.getCollectionOrCreate<BounceData>("${Values.DATA_PREFIX}bounce_data")
    val userBounceData =
        mongoDB.getCollectionOrCreate<UserBounceData>("${Values.DATA_PREFIX}user_bounce_data")

    val answerBounceAddressData =
        mongoDB.getCollectionOrCreate<AnswerBounceAddressData>("${Values.DATA_PREFIX}answer_bounce_address_data")
    val spamBounceAddressData =
        mongoDB.getCollectionOrCreate<SpamBounceAddressData>("${Values.DATA_PREFIX}spam_bounce_address_data")

    val sessionData =
        mongoDB.getCollectionOrCreate<SessionData>("${Values.DATA_PREFIX}session_data")

}