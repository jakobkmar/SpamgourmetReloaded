@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.spamgourmet.mailserver.mail

import net.axay.spamgourmet.mailserver.main.Manager
import net.axay.spamgourmet.mailserver.main.Constants
import org.subethamail.smtp.internal.util.EmailUtils

class SpamgourmetAddress(val fullAddress: String) {

    constructor(address: String, isOnlyFirstPart: Boolean) : this(kotlin.run {
        if (!isOnlyFirstPart) address else "$address@${Manager.configManager.domainInformation.mainAddressDomain}"
    })

    val splitParts by lazy { fullAddress.split('@') }
    val firstPart by lazy { splitParts[0] }
    val secondPart by lazy { splitParts[1] }

    val firstPartValues by lazy { firstPart.split('.') }

    val isValid by lazy { splitParts.size == 2 && EmailUtils.isValidEmailAddress(fullAddress) }
    val isSpamgourmetAddress by lazy { secondPart == Manager.configManager.domainInformation.mainAddressDomain }

    val type by lazy {

        when (firstPartValues.size) {
            3 -> {
                when {
                    firstPartValues[1].toIntOrNull() != null -> SpamgourmetAddressType.SPAMGOURMET_USER_ADDRESS
                    else -> SpamgourmetAddressType.UNKNOWN
                }
            }
            2 -> {
                when(firstPartValues.last()) {
                    Constants.ANSWER_ADDRESS_KEY -> SpamgourmetAddressType.SPAMGOURMET_ANSWER_ADDRESS
                    Constants.SPAM_BOUNCE_ADDRESS_KEY -> SpamgourmetAddressType.SPAMGOURMET_SPAM_BOUNCE_ADDRESS
                    Constants.ANSWER_BOUNCE_ADDRESS_KEY -> SpamgourmetAddressType.SPAMGOURMET_ANSWER_BOUNCE_ADDRESS
                    else -> SpamgourmetAddressType.UNKNOWN
                }
            }
            else -> SpamgourmetAddressType.UNKNOWN
        }

    }

}