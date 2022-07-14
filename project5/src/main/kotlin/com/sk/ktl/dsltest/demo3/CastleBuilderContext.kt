package com.sk.ktl.dsltest.demo3

import com.sk.ktl.dsltest.demo1.Connectable
import com.sk.ktl.dsltest.demo1.Keep
import com.sk.ktl.dsltest.demo1.StringSymbolTable
import com.sk.ktl.dsltest.demo1.Tower


fun main(args : Array<String>) {
    fun test() {
        CastleDsl().buildFourWallBuilder()
    }
    fun buildDemo() {
        val builder = CastleBuilder()

        builder.start("sw").to("nw").to("ne")
    }

}

class CastleDsl {
    fun buildFourWallBuilder() {
        val builder = CastleBuilder()

        // with context variable
        builder.start("sw").to("nw").to("ne").to("se")

        // using new context variable, for tracking which to start from
        builder.fix("keep")
               .fixTo("sw")
               .fixTo("nw")
               .fixTo("ne")
               .fixTo("se")

        // context function using partial application
        val to = builder.connectFrom("keep")
        to("sw")
        to("nw")
        to("ne")
        to("se")
    }
}

class CastleBuilder {
    var towers = mutableListOf<Tower>()
    var keep: Keep? = null
    var last: String? = null
    private var connections = mutableMapOf<String, String>()

    fun start(from: String): CastleBuilder {
        last = from
        return this
    }

    fun to(to: String):CastleBuilder {
        last?.let {
            connect(it, to)
        }

        last = to
        return this
    }

    fun fix(from: String): CastleBuilder {
        last = from
        return this
    }

    fun fixTo(to: String):CastleBuilder {
        last?.let {
            connect(it, to)
        }
        return this
    }

    fun connectFrom(from: String): (String) -> CastleBuilder {
        return {to: String -> connect(from, to)}
    }

    private fun connect(from: String, to: String): CastleBuilder {
        connections[from] = to
        return this
    }

    fun build(): Castle {
        val symbols = StringSymbolTable<Connectable>()
        towers.forEach { symbols.add(it.name, it) }
        keep?.let {
            symbols.add(it.name, it)
        }

        val allWalls = connections.map { (from, to) -> Wall(symbols.lookup(from), symbols.lookup(to)) }
        return Castle(keep, towers, allWalls)
    }
}

data class Wall(var from: Connectable, var to: Connectable)
data class Castle(var keep: Keep?, var towers: List<Tower>, var walls: List<Wall>)

