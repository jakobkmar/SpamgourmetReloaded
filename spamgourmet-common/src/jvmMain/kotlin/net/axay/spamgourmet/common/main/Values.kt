package net.axay.spamgourmet.common.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Values {

    const val DATA_PREFIX = "spamgourmet_"

    val CHARSET = Charsets.UTF_8

}

val COROUTINE_SCOPE = CoroutineScope(Dispatchers.Default)