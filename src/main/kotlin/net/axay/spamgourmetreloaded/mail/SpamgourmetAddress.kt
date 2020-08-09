@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.main.Manager
import org.subethamail.smtp.util.EmailUtils

class SpamgourmetAddress(val fullAddress: String) {

    val splitParts by lazy { fullAddress.split('@') }
    val firstPart by lazy { splitParts[0] }
    val secondPart by lazy { splitParts[1] }

    val firstPartValues by lazy { firstPart.split('.') }

    val isValid by lazy { splitParts.size == 2 && EmailUtils.isValidEmailAddress(fullAddress) }
    val isSpamgourmetAddress by lazy { secondPart == Manager.configManager.mainConfig.addressDomain }

    val type by lazy {

        when (firstPartValues.size) {
            3 -> SpamgourmetAddressType.SPAMGOURMET_USER_ADDRESS
            2 -> {
                when(firstPartValues.last()) {
                    "answer" -> SpamgourmetAddressType.SPAMGOURMET_ANSWER_ADDRESS
                    "bounce-s" -> SpamgourmetAddressType.SPAMGOURMET_SPAM_BOUNCE_ADDRESS
                    "bounce-a" -> SpamgourmetAddressType.SPAMGOURMET_ANSWER_BOUNCE_ADDRESS
                    else -> SpamgourmetAddressType.UNKNOWN
                }
            }
            else -> SpamgourmetAddressType.UNKNOWN
        }

    }

}