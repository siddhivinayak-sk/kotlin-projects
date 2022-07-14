package com.sk.ktl.collectiontest

fun main(s:Array<String>) {

    //Immutable Collection
    var p1: List<PersonB?>? = null //list variable can be nullable
    p1 = listOf<PersonB?>(PersonB("Max"), null) //List can be nullable (?) and accept null as element

    for(p: PersonB? in p1) {
        println("PersonB Name: ${p?.name}")
    }

    for(p: PersonB in p1.filterNotNull()) {
        println("PersonB Name: ${p.name}")
    }


    var p2 = setOf<PersonB>()
    var p3 = mapOf<Int, PersonB>()
    var p4 = arrayListOf<PersonB>()
    var p5 = hashSetOf<PersonB>()
    var p6 = hashMapOf<Int, PersonB>()

    //Mutable Collection
    var p7 = mutableListOf<PersonB>()
    var p8 = mutableSetOf<PersonB>()
    var p9 = mutableMapOf<Int, PersonB>()



}




class PersonB(var name:String): Organizer {

    /**
     * there could be different cases with overridden method argument or return type
     * meetingList - could be nullable or not-nullable
     * Meeting in meetingList - could be nullable or not-nullable
     * MutableList - can be MutableList or normal List
     */
    override fun processMeetings(meetingList: MutableList<Meeting?>?) {
        TODO("Not yet implemented")
    }

}