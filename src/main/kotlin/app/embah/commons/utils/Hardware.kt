package app.embah.commons.utils

import app.embah.commons.data.MacAddress
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.random.Random

object Hardware {

    fun getRealMacAddress(): ByteArray = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).hardwareAddress

    fun getRealMacAsString(delimiter: String = ":"): String {
        val builder = StringBuilder()
        val mac = getRealMacAddress()
        for (i in mac.indices) {
            builder.append(String.format("%02X%s", mac[i], if (i < mac.size - 1) delimiter else ""))
        }
        return builder.toString()
    }

    fun generateVendoredMac(): MacAddress {
        //grab a vendor prefix
        val line   = File(Configuration.MAC_VENDORS).readLines().random().trim()
        val alphas = "ABCDEF0123456789"
        var prefix = line.substring(0, 6)
        val vendor = line.substring(7, line.length)
        //append 6 new random alpha bytes
        for (i in 0..5) {
            prefix += alphas.random()
        }
        return MacAddress(stringToMac(prefix), vendor)
    }

    fun stringToMac(string: String): ByteArray {
        return ByteArray(6).also {
            //split the string into chunks of two, then convert to bytes
            string.chunked(2).forEachIndexed { index, string ->
                it[index] = string.toInt(16).toByte()
            }
        }
    }

    fun macToString(delimiter: String = ":", bytes: ByteArray): String {
        val builder = StringBuilder()
        for (i in bytes.indices) {
            builder.append(String.format("%02X%s", bytes[i], if (i < bytes.size - 1) delimiter else ""))
        }
        return builder.toString()
    }

    fun getRealSerialNumber(): String {
        when (Configuration.DETECTED_OS) {

            Configuration.OperatingSystem.WIN -> {

                val builder = ProcessBuilder("wmic", "baseboard", "get", "serialnumber")
                val process = builder.start()
                val inputStream = process.inputStream

                var character: Int
                val sb = StringBuilder()
                while (inputStream.read().also { character = it } != -1) {
                    sb.append(character.toChar())
                }
                var info = sb.toString()

                if (info.contains("SerialNumber")) {
                    info = info.replace("SerialNumber", "")
                }

                return info.trim()
            }
            else -> return "To be filled by O.E.M.".also { println("Application is unable to retrieve serial number on this OS, specify a value via cli, or consider making a pull request to add support") }
        }
    }

    fun generateSerialNumber(): String {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(IntRange(0, Random.nextInt(10, 16)))
    }
}