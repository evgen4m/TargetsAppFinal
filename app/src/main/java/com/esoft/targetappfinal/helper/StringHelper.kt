package com.esoft.targetappfinal.helper

class StringHelper {

    var hits1: String? = ""
    var btr1: String? = ""
    var hits2: String? = ""
    var btr2: String? = ""
    var hits3: String? = ""
    var btr3: String? = ""
    var hits4: String? = ""
    var btr4: String? = ""
    var hits5: String? = ""
    var btr5: String? = ""

    fun getInfoHits1(msg: String): String {
        val first = msg.substringBefore("M2")
        hits1 = first.substring(4,7)
        return hits1 as String
    }

    fun getInfoBtr1(msg: String): String {
        val first = msg.substringBefore("M2")
        btr1 = first.substring(8)
        return btr1 as String
    }

    fun getInfoHits2(msg: String): String {
        val first = msg.substringBefore("M3")
        hits2 = first.substring(14, 17)
        return hits2.toString()
    }

    fun getInfoBtr2(msg: String): String {
        val first = msg.substringBefore("M3")
        btr2 = first.substring(18)
        return btr2.toString()
    }

    fun getInfoHits3(msg: String):String {
        val first = msg.substringBefore("M4")
        hits3 = first.substring(24,27)
        return hits3.toString()
    }

    fun getInfoBtr3(msg: String): String {
        val first = msg.substringBefore("M4")
        btr3 = first.substring(28)
        return btr3.toString()
    }

    fun getInfoHits4(msg: String):String {
        val first = msg.substringBefore("M5")
        hits4 = first.substring(34,37)
        return hits4.toString()
    }

    fun getInfoBtr4(msg: String): String {
        val first = msg.substringBefore("M5")
        btr4 = first.substring(38)
        return btr4.toString()
    }

    fun getInfoHits5(msg: String):String {
        hits4 = msg.substring(44, 47)
        return hits4.toString()
    }

    fun getInfoBtr5(msg: String): String {
        btr4 = msg.substring(48)
        return btr4.toString()
    }


}