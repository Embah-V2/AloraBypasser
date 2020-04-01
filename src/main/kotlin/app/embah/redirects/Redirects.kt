package app.embah.redirects

import app.embah.commons.utils.Configuration
import java.net.InetAddress
import java.net.NetworkInterface

class Redirects {

    companion object {

        private val network  = Redirects()

        @JvmStatic
        fun getInputArguments() : List<String> {
            if (Configuration.debugging) println("Alora requested input args")
            return Configuration.FAKE_ARGS
        }

        @JvmStatic
        fun getProperty(str: String) : String {
            if (Configuration.debugging) println("Alora requested property '$str'")
            return System.getProperty(str)
        }

        @JvmStatic
        fun getSerialNumber() : String {
            if (Configuration.debugging) println("Alora requested HWID")
            return Configuration.hardwareProfile.serial
        }

        //called internally by Alora when grabbing MAC address
        @JvmStatic
        fun getByInetAddress(addr: InetAddress?): Redirects? {
            return network
        }
    }

    fun getHardwareAddress(): ByteArray {
        if (Configuration.debugging) println("Alora requested MAC")
        return Configuration.hardwareProfile.mac.bytes
    }
}