@file:Suppress("PackageDirectoryMismatch")

package com.sk.ktl.fortest.demo2

interface Files {
    fun exists(logDirectoryPath: Path): Boolean
    fun createFile(path: Path): Path

}

interface Paths {
    fun get(first: String, vararg more: String): Path
}

interface Path {
    fun toFile(): File
    fun toPath(): java.nio.file.Path
}

interface File {
    val name: String
    val absoluteFile: File
    val isFile: Boolean
    val isDirectory: Boolean

    fun listFiles(): List<File>

    companion object {
        val separator : String
            get() = java.io.File.separator
    }
}





class NioFiles : Files {
    override fun createFile(path: Path): Path {
        return NioPath( java.nio.file.Files.createFile(path.toPath()))
    }

    override fun exists(path: Path): Boolean {
        return java.nio.file.Files.exists(path.toPath())
    }

}

class NioPaths() : Paths {
    override fun get(first: String, vararg more: String): Path {
        return NioPath(java.nio.file.Paths.get(first, *more));
    }
}

class NioPath(val path: java.nio.file.Path) : Path {
    override fun toPath(): java.nio.file.Path {
        return path;
    }

    override fun toFile(): File {
        return IoFile(path.toFile());
    }
}

class IoFile(private val file: java.io.File) : File {

    override val name: String
        get() = file.name

    override val absoluteFile: File
        get() = IoFile(file.absoluteFile)
    override val isFile: Boolean
        get() = file.isFile
    override val isDirectory: Boolean
        get() = file.isDirectory

    override fun listFiles(): List<File> {
        return file.listFiles().map { it -> IoFile(it) }
    }

}
