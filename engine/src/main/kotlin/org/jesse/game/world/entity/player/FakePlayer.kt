package org.jesse.game.world.entity.player

import org.jesse.net.HardwareInfo
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.PlayerInformation

class FakePlayer(username: String) : Player(
    fakePlayerInformation(username),
    null
) {

    override fun setIndex(index: Int) {
        super.setIndex(index)

        if (index == -1) {
            return
        }
        allocateInfos()
    }

    override fun isSessionActive(): Boolean {
        return true
    }

    override fun isSessionExpired(currentCycle: Long): Boolean {
        return false
    }

}

private fun fakePlayerInformation(username: String) =
    PlayerInformation(username, username, -1, ByteArray(25), fakeHardwareInfo(), IntArray(0), true)

private fun fakeHardwareInfo() = HardwareInfo(
    cpuFeatures = intArrayOf(0, 0, 0),
    osId = 1,
    osVersion = 11,
    javaVendorId = 5,
    javaVersionMajor = 18,
    javaVersionMinor = 0,
    javaVersionUpdate = 2,
    heap = 4079,
    logicalProcessors = 12,
    physicalMemory = 0,
    clockSpeed = 0,
    graphicCardReleaseMonth = 0,
    graphicCardReleaseYear = 0,
    cpuCount = 0,
    cpuBrandType = 0,
    cpuModel = 0,
    graphicCardManufacture = "",
    graphicCardName = "",
    dxVersion = "",
    cpuManufacture = "",
    cpuName = "",
    isArch64Bit = true,
    isApplet = true
)
