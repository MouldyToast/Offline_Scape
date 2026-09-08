package cloud.rsps.worlds

/**
 * @author Jire
 */
interface World {

    val id: UShort

    val settings: Int
    val host: String
    val activity: String
    val countryFlag: Byte

    val playerCount: UShort

}
