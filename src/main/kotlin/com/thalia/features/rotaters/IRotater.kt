package com.thalia.features.rotaters

import com.thalia.ThaliaFlares

interface IRotater {
    fun rotate()
    fun add()
    fun shutdown() {
        ThaliaFlares.rotater = null
    }
}