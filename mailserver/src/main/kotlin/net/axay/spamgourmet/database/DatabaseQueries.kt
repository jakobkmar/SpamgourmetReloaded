package net.axay.spamgourmet.database

import net.axay.spamgourmet.database.data.UserData
import net.axay.spamgourmet.main.Manager
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

object DatabaseQueries {

    fun getUserDataFromUsername(username: String)
            = Manager.dataManager.userCollection.findOne(UserData::username eq username)

}