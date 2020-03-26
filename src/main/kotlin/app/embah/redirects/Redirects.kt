package app.embah.redirects

import java.net.InetAddress
import java.net.NetworkInterface

class Redirects {

    companion object {

        val serial : String
        val mac    : ByteArray
        val args   = listOf("java", "-Xmx800m", "-jar", "client.jar")

        init {
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

            serial = info.trim()
            mac    = (NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).hardwareAddress)

            val str = StringBuilder()
            for (byte in mac.indices) {
                str.append(String.format("%02X%s", mac[byte], if (byte < mac.size - 1) "-" else ""))
            }

            println("Serial HWID: $serial")
            println("MAC: $str")
        }

        @JvmStatic fun getInputArguments() : List<String> {
            println("Alora requested input args")
            return args
        }

        @JvmStatic fun getProperty(str: String) : String {
            println("Alora requested property '$str'")
            return System.getProperty(str)
        }

        @JvmStatic fun getSerialNumber() : String {
            println("Alora requested HWID")
            return serial
        }

        fun getHardwareAddress(): ByteArray {
            println("Alora requested MAC")
            return mac
        }
    }
}