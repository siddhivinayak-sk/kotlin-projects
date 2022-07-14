package com.sk.ktl

import com.sk.ktl.basic.IMyAddress

fun main(s:Array<String>) {

}


class HomeAddress: IMyAddress {
    override fun getFirstAddress(): String {  //Since Java does not annotate with @NotNull/@Nullable, return type can be nullable/notnull
        return ""
    }
}

class OfficeAddress: IMyAddress {
    override fun getFirstAddress(): String? { //Since Java does not annotate with @NotNull/@Nullable, return type can be nullable/notnull
        return null
    }
}