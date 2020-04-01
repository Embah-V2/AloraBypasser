package app.embah.commons.data

import app.embah.commons.utils.Hardware

/**
 * Simple data class that holds mac address and its' vendor
 *
 * Note: to get mac address in string form, call separatedBy(delimiter)
 *
 * ex.
 *      println(Hardware.generateVendoredMac() macSeparatedBy (":"))
 *      would generate something along the lines of 8C:C8:CD:B8:A9:1C
 */
data class MacAddress(
    val bytes : ByteArray,
    val vendor: String
) {
    override fun toString(): String {
        return "MacAddress(bytes=${bytes.contentToString()}, value=${macSeparatedBy(":")}, vendor=$vendor)"
    }

    infix fun macSeparatedBy(delimiter: String) = Hardware.macToString(delimiter, bytes)
}