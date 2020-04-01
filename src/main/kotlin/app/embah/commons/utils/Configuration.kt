package app.embah.commons.utils

import app.embah.commons.data.HardwareProfile
import app.embah.commons.data.MacAddress

object Configuration {


    const val MAC_VENDORS = "src/main/resources/MacVendors.txt"

    val DETECTED_OS = OperatingSystem.values().first { it.isOS() }.also { println("OS: ${it.name}") }
    val FAKE_ARGS   = listOf("java", "-Xmx800m", "-jar", "client.jar")

    lateinit var hardwareProfile: HardwareProfile

    var debugging = false

    operator fun invoke() {
        hardwareProfile = HardwareProfile(
                "Default",
                MacAddress(Hardware.getRealMacAddress(), "Your Computer's Vendor"),
                Hardware.getRealSerialNumber()
        ).also { println("Default hardware values generated.") }

        println("Default Mac: ${hardwareProfile.mac macSeparatedBy (":")}")
        println("Default Serial Number: ${hardwareProfile.serial}\n")
    }

    enum class OperatingSystem {
        WIN,
        MAC,
        LINUX,
        UNIX,
        UNKNOWN {
            override fun isOS() = true
        };

        private var os = System.getProperty("os.name").toLowerCase()

        open fun isOS(): Boolean {
            return os.contains(values()[ordinal].toString().toLowerCase())
        }
    }
}