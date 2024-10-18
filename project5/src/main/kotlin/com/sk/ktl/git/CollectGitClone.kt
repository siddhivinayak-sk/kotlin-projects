package com.sk.ktl.git

import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

const val GIT_FAILED = "=========GIT COMMAND FILED============"

fun traverse(path: Path) {
    if (!isGitRepo(path)) {
        if(path.isDirectory()) {
            path.toFile().listFiles()?.forEach {
                traverse(it.toPath())
            }
        }
    }
}

fun isGitRepo(path: Path): Boolean {
    return if(path.isDirectory() && path.resolve(".git").isDirectory()) {
        val gitUri = gitUriFromDirectoryPath(path)
        val gitCommand = "git clone $gitUri ${path.parent.absolute()}"
        println(gitCommand)
        true
    } else {
        false
    }
}

fun gitUriFromDirectoryPath(path: Path): String? {
    val gitBinPath = "C:\\Program Files\\Git\\cmd\\git.exe"
    val pb = ProcessBuilder(gitBinPath, "-C", path.absolute().pathString, "remote", "get-url", "origin")
    val env = pb.environment()
    pb.directory(path.toFile())
    val process = pb.start()
    return StringBuilder().apply {
        process.inputStream.bufferedReader().use {
            it.lines().forEach { line ->
                append(line)
            }
        }
    }.toString().takeIf { it.isNotBlank() } ?: "$GIT_FAILED: ${path.absolute()}"
}

fun main(args: Array<String>) {
    traverse(Path.of("c:\\sandeep\\work"))
}
