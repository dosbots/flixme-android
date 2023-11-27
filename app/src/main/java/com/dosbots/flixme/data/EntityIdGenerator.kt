package com.dosbots.flixme.data

import java.util.UUID

object EntityIdGenerator {

    fun newId() = UUID.randomUUID().toString()
}