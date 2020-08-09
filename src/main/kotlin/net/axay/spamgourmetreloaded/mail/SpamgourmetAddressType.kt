package net.axay.spamgourmetreloaded.mail

enum class SpamgourmetAddressType {

    // the address does not belong to the spamgourmet system
    SPAMMER,
    USER,

    // the address is some kind of address which belongs to the system
    SPAMGOURMET_USER_ADDRESS,
    SPAMGOURMET_ANSWER_ADDRESS,
    SPAMGOURMET_SPAM_BOUNCE_ADDRESS,
    SPAMGOURMET_ANSWER_BOUNCE_ADDRESS,

    // the address is invalid
    INVALID, UNKNOWN;

    companion object {

        fun typeOf(sender: SpamgourmetAddress?, recipient: SpamgourmetAddress): SpamgourmetAddressTypePair {

            val recipientType = if (recipient.isValid) {
                if (recipient.isSpamgourmetAddress) {
                    recipient.type
                } else UNKNOWN
            } else INVALID

            val senderType = if (sender != null && sender.isValid) {
                when (recipientType) {
                    SPAMGOURMET_USER_ADDRESS, SPAMGOURMET_ANSWER_BOUNCE_ADDRESS -> SPAMMER
                    SPAMGOURMET_ANSWER_ADDRESS, SPAMGOURMET_SPAM_BOUNCE_ADDRESS -> USER
                    else -> UNKNOWN
                }
            } else INVALID

            return SpamgourmetAddressTypePair(senderType, recipientType)

        }

    }

    class SpamgourmetAddressTypePair(val senderType: SpamgourmetAddressType, val recipientType: SpamgourmetAddressType)

}