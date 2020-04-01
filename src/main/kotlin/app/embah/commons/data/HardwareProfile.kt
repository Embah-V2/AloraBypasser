package app.embah.commons.data

data class HardwareProfile(
    var name  : String,
    var mac   : MacAddress,
    var serial: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is HardwareProfile) {
            other.hashCode() == hashCode()
        } else false
    }

    override fun hashCode() = name.hashCode()
}