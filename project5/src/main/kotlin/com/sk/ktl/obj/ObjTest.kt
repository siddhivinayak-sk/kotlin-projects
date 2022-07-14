package com.sk.ktl.obj

fun main(s:Array<String>) {
    //Access of object type's static member and functions
    Cources.initiazlie()
    for(e in Cources.allCourses) {
        println("Course Id: ${e.id} Name: ${e.name}")
    }

    //Access of static functions
    Person.main(s) //Static method calling of Person class
    var john = Person.create("John") //Create object by using static create method
    john.display()

    //Factory method implementation using XmlSerializer interface
    var st1 = Student()
    st1.name = "John"
    st1.display()
    Student.toXml(st1)

    //object is used to create anonymous classes
    calculator(10, 20, object : Process {
        override fun execute(value: Int) {
            println(value)
        }

    })
}

/**
 * object are similar to class, it may have properties, function and initializer. Only difference is
 * it does not have constructor means cannot create objects directly.
 * It's members and methods are accessed by object name directly by using dot (.)
 */
object Cources {
    var allCourses = arrayListOf<Course>()

    fun initiazlie() {
        allCourses.add(Course(1, "MBA"))
        allCourses.add(Course(2, "MCA"))
        allCourses.add(Course(3, "BCA"))
        allCourses.add(Course(4, "BBA"))
        allCourses.add(Course(5, "PGDM"))
    }
}
class Course(val id: Int, val name: String) {}

object CaseInsensitiveComparator: Comparator<Course> {
    override fun compare(o1: Course?, o2: Course?): Int {
        if (o1 != null) {
            if (o2 != null) {
                return o1.name.compareTo(o2.name)
            }
        }
        return -1
    }
}

/**
 * Companion Object provides way to implement:
 * - static methods
 * - factory method
 */
class Person(var name:String) {
    companion object {
        @JvmStatic //makes static method compatible with Java
        fun main(s:Array<String>) {
            println("PSVM in Person with Companion")
        }

        fun create(name: String): Person {
            return Person(name)
        }
    }

    fun display() {
        println("Name: $name")
    }
}

interface XmlSerializer<T> {
    fun toXml(item: T)
}

class Student {
    var name: String = ""

    companion object: XmlSerializer<Student> {
        override fun toXml(item: Student) {
            println("toXml: ${item.display()}")
        }
    }

    fun display() {
        println("Name: $name")
    }
}

//Interface
interface Process {
    fun execute(value: Int)
}
//Passed interface object as function argument
fun calculator(a: Int, b: Int, p: Process) {
    var c = a + b
    p.execute(c)
}