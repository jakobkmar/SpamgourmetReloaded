package net.axay.spamgourmetreloaded.database

import net.axay.spamgourmetreloaded.database.data.UserData
import net.axay.spamgourmetreloaded.main.Manager
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

object DatabaseQueries {

    fun getUserDataFromUsername(username: String)
            = Manager.dataManager.userCollection.findOne(UserData::username eq username)

}