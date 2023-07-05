package com.hayalgucu.albawms.util

import com.hayalgucu.albawms.models.ItemLocationModel
import com.hayalgucu.albawms.models.LocationListModel
import com.hayalgucu.albawms.models.LocationModel
import java.net.Inet6Address
import java.net.NetworkInterface
import java.util.Collections
import kotlin.experimental.xor

fun LocationListModel.toItemLocationModel(): ItemLocationModel =
    ItemLocationModel(altkonum = location, hcrKonumNo = shelfNo, hcrMakineNo = machineNumber, sipStokKodu = "", stkAdi = "")

fun getMacAddressFromIP(): String {
    var mac = ""
    try {
        val networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces())

        for (networkInterface in networkInterfaceList) {
            if (networkInterface.name.contains(
                    "wlan0",
                    true
                ) && !networkInterface.name.contains("swlan", true)
            ) {
                val a = networkInterface.inetAddresses.toList().first()
                mac = getMac(a as Inet6Address)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        mac = "Error"
    }
    return macCryptor(mac)
}

private fun macCryptor(macAddress: String): String {
    val normal =
        arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")
    val crypto =
        arrayListOf('c', 'z', 't', '5', '1', 'h', 'm', 'u', '6', 'a', 'p', 'l', '3', '2', 'y', 'j')
    val crypt = arrayListOf<Char>()
    macAddress.forEach {
        val idx = normal.indexOf(it.toString())
        crypt.add(crypto[idx])
    }

    return String(crypt.toCharArray())
}

private fun getMac(ipv6: Inet6Address?): String {
    var eui48mac: ByteArray? = null
    if (ipv6 != null) {
        val ipv6Bytes = ipv6.address
        if (ipv6Bytes != null /*&&
                ipv6Bytes.size == 16 &&
                ipv6Bytes[0] == 0xfe.toByte() &&
                ipv6Bytes[1] == 0x80.toByte() &&
                ipv6Bytes[11] == 0xff.toByte() &&
                ipv6Bytes[12] == 0xfe.toByte()*/
        ) {
            eui48mac = ByteArray(6)
            eui48mac[0] = (ipv6Bytes[8] xor 0x2)
            eui48mac[1] = ipv6Bytes[9]
            eui48mac[2] = ipv6Bytes[10]
            eui48mac[3] = ipv6Bytes[13]
            eui48mac[4] = ipv6Bytes[14]
            eui48mac[5] = ipv6Bytes[15]
        }
    }
    return eui48mac!!.toHexString()
}

fun ByteArray.toHexString() = joinToString("") {
    Integer.toUnsignedString(java.lang.Byte.toUnsignedInt(it), 16).padStart(2, '0')
}