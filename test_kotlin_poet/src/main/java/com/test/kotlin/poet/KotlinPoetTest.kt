package com.test.kotlin.poet

import com.squareup.kotlinpoet.*

fun main(vararg args: String) {
    testHelloWorld()
    println("\n \n")

    testAddCode()
    println("\n \n")

    testControlFlow()
    println("\n \n")


}

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
