package com.sk.ktl.basic

fun main(args: Array<String>) {
    var data: String = "aws-sdk-java/2.17.236 Linux/5.15.0-1015-aws OpenJDK_64-Bit_Server_VM/17.0.4+8-Debian-1deb11u1 Java/17.0.4 kotlin/1.6.21-release-334(1.6.21) vendor/Debian io/async http/NettyNio cfg/retry-mode/legacy, service/service-file-lifecycle-mgt"
    println(data.contains("service/service-file-lifecycle-mgt"))
}