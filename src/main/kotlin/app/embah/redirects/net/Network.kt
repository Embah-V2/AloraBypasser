package app.embah.redirects.net

import app.embah.redirects.Redirects
import java.net.InetAddress

class Network {

    companion object {

        private val cached = Network()

        @JvmStatic fun getByInetAddress(addr: InetAddress?): Network? {
            return cached
        }
    }

    val hardwareAddress: ByteArray
        get() = Redirects.getHardwareAddress()
}