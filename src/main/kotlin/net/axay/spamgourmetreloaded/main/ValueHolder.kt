package net.axay.spamgourmetreloaded.main

import java.util.*
import javax.mail.Session

object ValueHolder {

    const val PREFIX = "(SpamgourmetReloaded) >> "
    const val DATA_PREFIX = "spamgourmet_"

    val DEFAULT_MAIL_SESSION: Session = Session.getDefaultInstance(Properties())

}