package com.sk.ktl.types

fun main(s:Array<String>) {

    //var human: Human = Human("123") //cannot create instance of abstract class

    var john = Person("id001", "John", "Mclin")    //Default constructor
    var rohit = Student("id002", "Rohit", "Kumar") //Default constructor
    var mohit = Student("id003", "Mohit", "Kumar", "LKG", 10000) //Secondary constructor
    var emp1 = Employee("emp001", "E1", "Kumar", 50000.0) //Default constructor without optional property
    var emp2 = Employee("emp002", "E2", "Kumar", 40000.0) //Default constructor with optional property

    var emp3 = Manager("emp003", "E3", "Kumar", 140000.0)
}


/**
 * Abstract class, it may have
 * - final method
 * - abstract method
 * - open method with body
 * here, id property is immutable, cannot be changed once created
 */
abstract class Human(val id: String) {
    fun humanId():String  {return id} //cannot be override
    abstract fun work(): String       //must be override
    open fun walk() {println("Human walking")} //optional for override
}

/**
 * Class must be open then only child class can extend it. Any function which need to be overridden must be open as well
 */
open class Person(id: String, var firstName:String, var lastName:String): Human(id) {
    fun getName():String {return "$firstName $lastName"} //Cannot be override as by default it is final
    override fun work(): String {return "Person work"} //It can override
}

/**
 * Student implementation from Person with secondary constructor
 */
class Student(id: String, firstName: String, lastName: String) : Person(id, firstName, lastName) {
    lateinit var level: String
    var fee: Int = 0

    constructor(id: String, firstName: String, lastName: String, level: String, fee: Int) : this(id, firstName, lastName) {
        this.level = level
        this.fee = fee
    }

    override fun work(): String {return "Student work"}
}

/**
 * Employee implementation from Person with default parameter in default constructor
 */
open class Employee(id: String, firstName: String, lastName: String, salary: Double, deparment:String = "UnAssigned") : Person(id, firstName, lastName) {
    override fun work(): String { return "Employee work"}
}

/**
 * Used super constructor for initialization
 */
class Manager : Employee {

    //Constructor without optional parameter of super type
    constructor(id: String, firstName: String, lastName: String, salary: Double) : super(id, firstName, lastName, salary) {
    }

    //constructor with optional parameter of super type
    constructor(id: String, firstName: String, lastName: String, salary: Double, deparment:String = "UnAssigned") : super(id, firstName, lastName, salary, deparment) {
    }

}

/**
 * Used init block for initiazliation
 */
class Operator : Employee {
    var section:String

    init {
        val a = Math.random();
        val b = Math.random();
        this.section = "Section ${a * b}"
    }
    //Constructor without optional parameter of super type
    constructor(id: String, firstName: String, lastName: String, salary: Double) : super(id, firstName, lastName, salary) {
    }

    //constructor with optional parameter of super type
    constructor(id: String, firstName: String, lastName: String, salary: Double, deparment:String = "UnAssigned") : super(id, firstName, lastName, salary, deparment) {
    }

}

