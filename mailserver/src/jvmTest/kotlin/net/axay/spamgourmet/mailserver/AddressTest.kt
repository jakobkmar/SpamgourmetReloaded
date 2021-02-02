package net.axay.spamgourmet.mailserver

import net.axay.spamgourmet.mailserver.mail.SpamgourmetAddress
import net.axay.spamgourmet.mailserver.mail.SpamgourmetAddressType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class AddressTest {

    @Test
    fun getAddressTypeUserAddress() {
        assertEquals(
            SpamgourmetAddressType.SPAMGOURMET_USER_ADDRESS,
            SpamgourmetAddressType.typeOf(null, SpamgourmetAddress(
                "test.12.foo@axay.net"
            )).recipientType
        )
    }

    @Test
    fun getAddressTypeAnswerAddress() {
        assertEquals(
            SpamgourmetAddressType.SPAMGOURMET_ANSWER_ADDRESS,
            SpamgourmetAddressType.typeOf(null, SpamgourmetAddress(
                "36275623786572865872.answer@axay.net"
            )).recipientType
        )
    }

    @Test
    fun parseAddress() {
        val address = SpamgourmetAddress("test.12.foo@axay.net")

        assertEquals(address.firstPart, "test.12.foo")
        assertEquals(address.firstPartValues, listOf("test", "12", "foo"))
        assertEquals(address.secondPart, "axay.net")

        assertEquals(address.isSpamgourmetAddress, true)
        assertEquals(address.isValid, true)

        assertEquals(address.type, SpamgourmetAddressType.SPAMGOURMET_USER_ADDRESS)
    }

}