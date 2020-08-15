@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.main.Manager
import net.axay.spamgourmetreloaded.main.ValueHolder
import org.subethamail.smtp.util.EmailUtils

class SpamgourmetAddress(val fullAddress: String) {

    constructor(address: String, isOnlyFirstPart: Boolean) : this(kotlin.run {
        if (!isOnlyFirstPart) address else "$address@${Manager.configManager.mainConfig.domainConfig.mainAddressDomain}"
    })

    val splitParts by lazy { fullAddress.split('@') }
    val firstPart by lazy { splitParts[0] }
    val secondPart by lazy { splitParts[1] }

    val firstPartValues by lazy { firstPart.split('.') }

    val isValid by lazy { splitParts.size == 2 && EmailUtils.isValidEmailAddress(fullAddress) }
    val isSpamgourmetAddress by lazy { secondPart == Manager.configManager.mainConfig.domainConfig.mainAddressDomain }

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
                    ValueHolder.ANSWER_ADDRESS_KEY -> SpamgourmetAddressType.SPAMGOURMET_ANSWER_ADDRESS
                    ValueHolder.SPAM_BOUNCE_ADDRESS_KEY -> SpamgourmetAddressType.SPAMGOURMET_SPAM_BOUNCE_ADDRESS
                    ValueHolder.ANSWER_BOUNCE_ADDRESS_KEY -> SpamgourmetAddressType.SPAMGOURMET_ANSWER_BOUNCE_ADDRESS
                    else -> SpamgourmetAddressType.UNKNOWN
                }
            }
            else -> SpamgourmetAddressType.UNKNOWN
        }

    }

}