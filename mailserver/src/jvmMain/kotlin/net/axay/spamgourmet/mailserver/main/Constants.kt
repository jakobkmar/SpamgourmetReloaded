package net.axay.spamgourmet.mailserver.main

import java.util.*
import javax.mail.Session

object Constants {

    val DEFAULT_MAIL_SESSION: Session = Session.getDefaultInstance(Properties())

    const val ANSWER_ADDRESS_KEY = "answer"
    const val SPAM_BOUNCE_ADDRESS_KEY = "bounce-s"
    const val ANSWER_BOUNCE_ADDRESS_KEY = "bounce-a"

    const val NO_REPLY_ADDRESS_KEY = "no-reply"

}