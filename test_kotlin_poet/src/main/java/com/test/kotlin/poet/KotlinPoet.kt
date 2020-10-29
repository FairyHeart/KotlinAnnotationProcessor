package com.test.kotlin.poet

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.sun.net.httpserver.Headers
import java.util.logging.LogRecord


/*
class Greeter(val name: String) {
    fun greet() {
        println("""Hello, $name""")
    }
}

fun main(vararg args: String) {
    Greeter(args[0]).greet()
}
*/
fun testHelloWorld() {

    val greeterClass = ClassName("com.test", "Greeter")


    //类TypeSpec，和java中的TypeSpec相同
    val typeSpec = TypeSpec
        .classBuilder("Greeter")
        .primaryConstructor(
            //方法FunSpec，相当于java中的MethodSpec
            FunSpec
                .constructorBuilder()
                .addParameter("name", String::class)
                .build()
        )
        .addProperty(
            //PropertySpec属性，java中的FieldSpec
            PropertySpec
                .builder("name", String::class)
                .initializer("name")
                .build()
        )
        .addFunction(
            FunSpec
                .builder("greet")
                .addStatement("println(%P)", "Hello ,\$name")
                .build()
        )
        .build()

    //包名和文件名FileSpec，相当于java中的JavaFile
    val fileSpec = FileSpec
        .builder("com.test", "HelloWorld")
        .addType(typeSpec)
        .addFunction(
            FunSpec.builder("main")
                .addParameter("args", String::class, KModifier.VARARG)
                .addStatement("%T(args[0]).greet()", greeterClass)
                .build()
        )
        .build()

    fileSpec.writeTo(System.out)
}

/*fun main() {
    var total = 0
    for (i in 0 until 10) {
        total += i
    }
}*/
fun testAddCode() {
    val fileSpec = FileSpec
        .builder("com.test", "HelloWorld")
        .addFunction(
            FunSpec.builder("main")
                .addCode(
                    "fun main() {\n" +
                            "    var total = 0\n" +
                            "    for (i in 0 until 10) {\n" +
                            "        total += i\n" +
                            "    }\n" +
                            "}"
                )
                .build()
        )
        .build()
    fileSpec.writeTo(System.out)
}

fun testControlFlow() {
    FileSpec.builder("com.test", "HelloWorld")
        .addFunction(
            FunSpec.builder("main")
                .addStatement("var total = 0")
                .beginControlFlow("for (i in 0 until 10) ")
                .addStatement("total += i")
                .endControlFlow()
                .build()
        )
        .build()
        .writeTo(System.out)
}

/*
可空类型
class HelloWorld {
    private var java: String? = null

    private val kotlin: String
}
*/
fun testNull() {
    val typeSpec = TypeSpec.classBuilder("HelloWorld")
        .addProperty(
            PropertySpec
                .builder("java", String::class.asTypeName().copy(nullable = true))//String?
                .mutable()//var
                .addModifiers(KModifier.PRIVATE)
                .initializer("null")
                .build()
        )
        .build()

    FileSpec.builder("com.test", "Test")
        .addType(typeSpec)
        .build()
        .writeTo(System.out)
}

/*拓展函数
fun Int.square(): Int {
    val s = this * this
    return s
}*/
fun testFun() {

    val funSpec = FunSpec
        .builder("square")
        .receiver(Int::class)
        .returns(Int::class)
        .addStatement("val s = this * this")
        .addStatement("return s")
        .build()

    FileSpec.builder("com.test", "Test")
        .addFunction(funSpec)
        .build()
        .writeTo(System.out)
}

/*
单表达功能
fun abs(x: Int): Int = if (x < 0) -x else x
*/
fun testFun2() {
    val funSpec = FunSpec.builder("abs")
        .addParameter("x", Int::class)
        .returns(Int::class)
        .addStatement("return if (x < 0) -x else x")
        .build()
    FileSpec.builder("com.test", "Test")
        .addFunction(funSpec)
        .build()
        .writeTo(System.out)
}


/*
默认函数参数
fun add(a: Int, b: Int = 0) {
  print("a + b = ${ a + b }")
}
*/
fun testFunDefault() {

    val funSpec = FunSpec
        .builder("add")
        .addParameter("a", Int::class)
        .addParameter(
            ParameterSpec.builder("b", Int::class)
                .defaultValue("%L", 0)
                .build()
        )
        .addStatement("print(\"a + b = +\" %L.plus(%L))", "a", "b")
        .build()

    FileSpec.builder("com.test", "Test")
        .addFunction(funSpec)
        .build()
        .writeTo(System.out)
}

/*
空格换行 ·
fun foo() = (100..10000).map { number -> number * number }.map { number -> number.toString() }
    .also { string -> println(string) }
*/
fun testKongGe() {
    val funSpec = FunSpec.builder("foo")
        .addStatement("return (100..10000).map { number -> number * number }.map { number -> number.toString() }.also·{ string -> println(string) }")
        .build()
    FileSpec.builder("com.test", "Test")
        .addFunction(funSpec)
        .build()
        .writeTo(System.out)
}

/*
构造函数
class HelloWorld {
    private val greeting: String

    constructor(greeting: String) {
        this.greeting = greeting
    }
}
*/
fun testConstructor() {
    val funSpec = FunSpec.constructorBuilder()
        .addParameter("greeting", String::class)
        .addStatement("this.greeting = greeting")
        .build()
    val typeSpec = TypeSpec.classBuilder("HelloWorld")
        .addProperty("greeting", String::class, KModifier.PRIVATE)
        .addFunction(funSpec)
        .build()
    FileSpec.builder("com.test", "Test")
        .addType(typeSpec)
        .build()
        .writeTo(System.out)
}

/*
class HelloWorld(private val greeting?: String="abc")
*/
fun testConstructor2() {
    val funSpec = FunSpec.constructorBuilder()
        .addParameter(
            ParameterSpec.builder("greeting", String::class.asTypeName().copy(nullable = true))
                .defaultValue("%L", "abc")
                .build()
        )
        .build()

    val typeSpec = TypeSpec.classBuilder("HelloWorld")
        .primaryConstructor(funSpec)
        .addProperty(
            PropertySpec.builder("greeting", String::class.asTypeName().copy(nullable = true))
                .addModifiers(KModifier.PRIVATE)
                .initializer("greeting")
                .build()
        )
        .build()
    FileSpec.builder("com.test", "Test")
        .addType(typeSpec)
        .build()
        .writeTo(System.out)
}

/*
内联属性:标有的属性inline应至少具有一个访问器get
inline val android: kotlin.String
    get() = "foo"
    set(value) {
    }
*/
fun testInline() {
    val propertySpec = PropertySpec
        .builder("android", String::class)
        .mutable()
        .getter(
            FunSpec.getterBuilder()
                .addModifiers(KModifier.INLINE)
                .addStatement("return %S", "foo")
                .build()
        )
        .setter(
            FunSpec.setterBuilder()
                .addModifiers(KModifier.INLINE)
                .addParameter("value", String::class)
                .build()
        )
        .build()
    FileSpec.builder("com.test", "Test")
        .addProperty(propertySpec)
        .build()
        .writeTo(System.out)
}

/*
接口 接口方法必须始终为abstract
interface HelloWorld {
    val buzz: String

    fun beep()
}
*/
fun testInterface() {
    val typeSpec = TypeSpec.interfaceBuilder("HelloWorld")
        .addFunction(
            FunSpec.builder("beep").addModifiers(KModifier.ABSTRACT).build()
        )
        .addProperty("buzz", String::class)
        .build()

    FileSpec.builder("com.test", "Test")
        .addType(typeSpec)
        .build()
        .writeTo(System.out)
}

/*
静态类
object HelloWorld {
  val buzz: String = "buzz"
}
*/
fun testObject() {
    FileSpec.builder("com.test", "Test")
        .addType(
            TypeSpec.objectBuilder("HelloWorld")
                .addProperty(
                    PropertySpec.builder("buzz", String::class)
                        .initializer("%S", "buzz")
                        .build()
                )
                .build()
        )
        .build()
        .writeTo(System.out)
}

/*public class HelloWorld {
    public companion object instance {
        public val buzz: String

        public fun beep(): Unit {
            println("beep")
        }
    }
}*/
fun testCompanion() {
    val companion = TypeSpec.companionObjectBuilder(name = "instance")
        .addProperty("buzz", String::class)
        .addFunction(
            FunSpec.builder("beep")
                .addStatement("println(%S)", "beep")
                .build()
        )
        .build()

    FileSpec.builder("com.test", "Test")
        .addType(
            TypeSpec.classBuilder("HelloWorld")
                .addType(companion)
                .build()
        )
        .build()
        .writeTo(System.out)
}

/*
枚举
enum class Roshambo(private val handsign: String) {
    ROCK("fist") {
        override fun toString(): String = "avalanche!"
    },

    SCISSORS("peace"),

    PAPER("flat");
}
*/
fun testEnum() {
    FileSpec.builder("com.test", "Test")
        .addType(
            TypeSpec.enumBuilder("Roshambo")
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("handsign", String::class)
                        .build()
                )
                .addEnumConstant(
                    "ROCK", TypeSpec.anonymousClassBuilder()
                        .addSuperclassConstructorParameter("%S", "fist")
                        .addFunction(
                            FunSpec.builder("toString")
                                .addModifiers(KModifier.OVERRIDE)
                                .addStatement("return %S", "avalanche!")
                                .returns(String::class)
                                .build()
                        )
                        .build()
                )
                .addEnumConstant(
                    "SCISSORS", TypeSpec.anonymousClassBuilder()
                        .addSuperclassConstructorParameter("%S", "peace")
                        .build()
                )
                .addEnumConstant(
                    "PAPER", TypeSpec.anonymousClassBuilder()
                        .addSuperclassConstructorParameter("%S", "flat")
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("handsign", String::class, KModifier.PRIVATE)
                        .initializer("handsign")
                        .build()
                )
                .build()
        )
        .build()
        .writeTo(System.out)
}

/*
匿名内部类 TypeSpec.anonymousClassBuilder()
class HelloWorld {
    fun sortByLength(strings: List<String>) {
        strings.sortedWith(object : Comparator<String> {
            override fun compare(a: String, b: String): Int = a.length - b.length
        })
    }
}
*/
fun testInnerClass() {
    val comparator = TypeSpec.anonymousClassBuilder()
        .addSuperinterface(Comparator::class.parameterizedBy(String::class))//泛型
        .addFunction(
            FunSpec.builder("compare")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Int::class)
                .addParameter("a", String::class)
                .addParameter("b", String::class)
                .addStatement("return %L.length - %L.length", "a", "b")
                .build()
        )
        .build()

    val funSpec = FunSpec.builder("sortByLength")
        .addParameter("strings", List::class.parameterizedBy(String::class))
        .addStatement("%N.sortedWith(%L)", "strings", comparator)
        .build()

    val typeSpec = TypeSpec.classBuilder("HelloWorld")
        .addFunction(funSpec)
        .build()

    FileSpec.builder("com.test", "Test")
        .addType(typeSpec)
        .build()
        .writeTo(System.out)
}

/*
注解
@Headers(
        accept = "application/json; charset=utf-8",
        userAgent = "Square Cash"
)
fun recordEvent(logRecord: LogRecord): LogReceipt
*/
fun testAnnotation() {
    val funSpec = FunSpec.builder("recordEvent")
        .addAnnotation(
            AnnotationSpec.builder(Override::class)
                .addMember("accept = %S", "application/json; charset=utf-8")
                .addMember("userAgent = %S", "Square Cash")
                .build()
        )
        .addParameter("logRecord", LogRecord::class)
        .returns(LogRecord::class)
        .build()
    FileSpec.builder("com.test", "Test")
        .addFunction(funSpec)
        .build()
        .writeTo(System.out)
}

/*
@HeaderList([
    Header(name = "Accept", value = "application/json; charset=utf-8"),
    Header(name = "User-Agent", value = "Square Cash")
])
abstract fun recordEvent(logRecord: LogRecord): LogReceipt
*/
fun testAnnotation2() {
    val headerList = ClassName("", "HeaderList")
    val header = ClassName("", "Header")
    val logRecord = FunSpec.builder("recordEvent")
        .addModifiers(KModifier.ABSTRACT)
        .addAnnotation(
            AnnotationSpec.builder(headerList)
                .addMember(
                    "[\n⇥%L,\n%L⇤\n]",
                    AnnotationSpec.builder(header)
                        .addMember("name = %S", "Accept")
                        .addMember("value = %S", "application/json; charset=utf-8")
                        .build(),
                    AnnotationSpec.builder(header)
                        .addMember("name = %S", "User-Agent")
                        .addMember("value = %S", "Square Cash")
                        .build()
                )
                .build()
        )
        .addParameter("logRecord", LogRecord::class)
        .returns(LogRecord::class)
        .build()

    FileSpec.builder("com.test", "Test")
        .addFunction(logRecord)
        .build()
        .writeTo(System.out)
}