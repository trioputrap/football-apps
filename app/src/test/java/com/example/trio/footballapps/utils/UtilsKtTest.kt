package com.example.trio.footballapps.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsKtTest {

    @Test
    fun toDateStringFormat() {
        val strDate: String = "2018-11-04"
        assertEquals("Sun, 04 Nov 2018", strDate.toDateStringFormat())
    }
}