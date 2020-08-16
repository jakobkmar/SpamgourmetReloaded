package net.axay.spamgourmetreloaded.database

import com.mongodb.client.ClientSession
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.CountOptions
import org.bson.conversions.Bson

fun <T> MongoCollection<T>.contains(filter: Bson) = countDocuments(filter, CountOptions().limit(1)) == 1L
fun <T> MongoCollection<T>.contains(clientSession: ClientSession, filter: Bson) = countDocuments(clientSession, filter, CountOptions().limit(1)) == 1L