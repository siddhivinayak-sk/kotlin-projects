package com.sk.ktl.dsltest

class CastleSymbolNotRecognizedException(name: String): Exception(name) {}

class Connectable(from: String, to: String) {}

class StringSymbolTable<T> {
    var map = mutableMapOf<String, T>()

    fun lookup(symbol: String): T {
        map[symbol]?.let {return it} ?: throw CastleSymbolNotRecognizedException("Cannot find symbol $symbol")
    }

    fun add(key: String, value: T?) {
        value?.let{ map[key] = it}
    }
}
class CastleBuilder {
    private var towers = mutableListOf<Tower>()
    private var keep: Keep? = null
    private var connections = mutableMapOf<String, String>()

    fun keep(name: String): CastleBuilder {
        keep = Keep(name)
        return this
    }

    fun tower(name: String): CastleBuilder {
        towers.add(Tower(name))
        return this
    }

    fun connect(from: String, to: String): CastleBuilder {
        connections[from] = to
        return this
    }

    fun connect(map: Map<String, String>) {
        map.forEach { from, to -> connect(from, to) }
    }

    fun connectToAll(from: String, vararg to: String): CastleBuilder {
        for(toConnect in to) {
            connections[from] = toConnect
        }
        return this
    }

    fun build(): Castle? {
        //val symbols = StringSymbolTable<Connectable>()
        //towers.forEach{ symbols.add(it.name, Connectable(keep?.name, it.name))}
        //keep?.let { symbols.add(it.name, it) }

        val allWalls = connections.map { (from, to) -> Wall("", from, to) } as MutableList<Wall>
        return keep?.let { Castle("Castle", it, towers, allWalls) }
    }
}
data class Castle(var name: String) {
    var keep: Keep? = null
    var towers: MutableList<Tower> = mutableListOf<Tower>()
    var walls: MutableList<Wall> = mutableListOf<Wall>()

    constructor(name: String, keep: Keep, towers: MutableList<Tower>, walls: MutableList<Wall>): this(name) {
        this.keep = keep
        this.towers = towers
        this.walls = walls
    }

    override fun toString(): String {
        return "Castle(name='$name', keep=$keep, towers=$towers, walls=$walls)"
    }


}
data class Keep(var name: String) {}
data class Tower(var name: String) {}
data class Wall(var name: String, var from: String, var to: String) {}


fun main(s:Array<String>) {
    var builder1 = CastleBuilder()

    //Builder pattern for castle building
    builder1.keep("keep")
    builder1.tower("sw")
    builder1.tower("se")
    builder1.tower("ne")
    builder1.tower("nw")

    builder1.connect("sw", "se")
    builder1.connect("nw", "ne")
    builder1.connect("ne", "se")
    builder1.connect("se", "sw")
    builder1.connect("keep", "sw")
    builder1.connect("keep", "nw")
    builder1.connect("keep", "se")
    builder1.connect("keep", "ne")
    var castle1: Castle? = builder1.build()
    println(castle1)

    //Using appy{} along with Builder Pattern
    var builder2 = CastleBuilder()
    builder2.apply {
        keep("keep")
        tower("sw")
        tower("se")
        tower("ne")
        tower("nw")

        connect("sw", "se")
        connect("nw", "ne")
        connect("ne", "se")
        connect("se", "sw")
        connect("keep", "sw")
        connect("keep", "nw")
        connect("keep", "se")
        connect("keep", "ne")
    }

    //Builder pattern with chaining
    var builder3 = CastleBuilder()

    builder3.keep("keep").tower("sw").tower("se").tower("ne").tower("nw")
    builder3.connect("keep", "sw").connect("keep", "se").connect("keep", "ne").connect("keep", "nw")
    builder3.connect("sw", "se").connect("nw", "ne").connect("ne", "se").connect("se", "sw")

    builder3.connectToAll("keep", "sw", "nw", "ne", "se")
    builder3.connect(mapOf("sw" to "se", "nw" to "ne", "ne" to "se", "se" to "sw"))


}