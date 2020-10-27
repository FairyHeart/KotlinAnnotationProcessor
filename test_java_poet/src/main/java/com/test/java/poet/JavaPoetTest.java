package com.test.java.poet;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.LogRecord;

import javax.lang.model.element.Modifier;

public class JavaPoetTest {

    public static void main(String[] args) throws IOException {

        TypeSpec typeSpec = TypeSpec
                .classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(testCode())
                .addMethod(testAddStatement())
                .build();

        JavaFile javaFile = JavaFile.builder("com.test.java.poet", typeSpec).build();
        javaFile.writeTo(System.out);
        System.out.println("\n \n");

        testAbstract();
        System.out.println("\n \n");

        testConstructor();
        System.out.println("\n \n");

        testInterface();
        System.out.println("\n \n");

        testEnum();
        System.out.println("\n \n");

        testEnumParam();
        System.out.println("\n \n");

        testInnerClass();
        System.out.println("\n \n");

        testAnnotations();
        System.out.println("\n \n");
    }

    /* HelloWorld
     package com.test.java.poet;

     public final class HelloWorld {
         public static void main(String[] args) {
             System.out.println("Hello, JavaPoet!");
         }
     }
     */
    public static void testHelloWorld() throws IOException {

        MethodSpec methodSpec = MethodSpec
                .methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec typeSpec = TypeSpec
                .classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.test.java.poet", typeSpec).build();
        javaFile.writeTo(System.out);
    }

    /* 添加代码
    void main() {
        int total = 0;
        for (int i = 0; i < 10; i++) {
            total += i;
        }
    }
    */
    public static MethodSpec testCode() throws IOException {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("main")
                .addCode("" +
                        "int total = 0;\n" +
                        "for (int i = 0; i < 10; i++) {\n" +
                        "   total += i;\n" +
                        "}")
                .build();
        return methodSpec;
    }

    /* beginControlflow() 代表大括号的开始，endControlFlow()则代表大括号的结束
    void main() {
        long now = System.currentTimeMillis();
        if (System.currentTimeMillis() < now)  {
            System.out.println("Time travelling, woo hoo!");
        } else if (System.currentTimeMillis() == now) {
            System.out.println("Time stood still!");
        } else {
            System.out.println("Ok, time still moving forward");
        }
    }*/
    static MethodSpec testAddStatement() {
        return MethodSpec.methodBuilder("main")
                .addStatement("long now = $T.currentTimeMillis()", System.class)
                .beginControlFlow("if ($T.currentTimeMillis() < now) ", System.class)
                .addStatement("$T.out.println($S)", System.class, "Time travelling, woo hoo!")
                .nextControlFlow("else if (System.currentTimeMillis() == now)")
                .addStatement("$T.out.println($S)", System.class, "Time stood still!")
                .nextControlFlow(" else ")
                .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
                .endControlFlow()
                .build();
    }


    /* 抽象方法和类创建
    public abstract class HelloWorld {
        protected abstract void flux();
    }
    */
    private static void testAbstract() throws IOException {
        System.out.println("\n \n");
        MethodSpec methodSpec = MethodSpec.methodBuilder("flux")
                .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder("com.test", typeSpec).build();
        javaFile.writeTo(System.out);
    }

    /*构造方法 Constructors
    public class HelloWorld {
        private final String greeting;

        public HelloWorld(String greeting) {
            this.greeting = greeting;
        }
    }*/
    static void testConstructor() throws IOException {
        MethodSpec methodSpec = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "greeting")
                .addStatement(" this.$N = $N", "greeting", "greeting")
                .build();
        FieldSpec fieldSpec = FieldSpec
                .builder(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)
                .build();
        TypeSpec typeSpec = TypeSpec
                .classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addField(fieldSpec)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile
                .builder("com.test", typeSpec)
                .build();
        javaFile.writeTo(System.out);
    }

    /* 接口

        public interface IHelloWorld {
        String ONLY_THING_THAT_IS_CONSTANT = "change";

        void beep();
    }*/
    static void testInterface() throws IOException {

        FieldSpec fieldSpec = FieldSpec
                .builder(String.class, "ONLY_THING_THAT_IS_CONSTANT")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)//接口的参数必须要添加public static final
                .initializer("$S", "change")
                .build();

        MethodSpec methodSpec = MethodSpec
                .methodBuilder("beep")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)//接口方法名字必须添加public abstract
                .build();

        TypeSpec typeSpec = TypeSpec
                .interfaceBuilder("IHelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .addField(fieldSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.test", typeSpec).build();
        javaFile.writeTo(System.out);
    }

    /*枚举
    public enum Roshambo {
        ROCK,

        SCISSORS,

        PAPER
    }*/
    static void testEnum() throws IOException {
        TypeSpec typeSpec = TypeSpec
                .enumBuilder("Roshambo")
                .addModifiers(Modifier.PUBLIC)
                .addEnumConstant("ROCK")
                .addEnumConstant("SCISSORS")
                .addEnumConstant("PAPER")
                .build();
        JavaFile javaFile = JavaFile.builder("com.test", typeSpec).build();
        javaFile.writeTo(System.out);
    }

    /*枚举带参数
    public enum Roshambo {
        ROCK("fist") {
            @Override
            public String toString() {
                return "avalanche!";
            }
        },

        SCISSORS("peace"),

        PAPER("flat");

        private final String handsign;

        Roshambo(String handsign) {
            this.handsign = handsign;
        }
    }*/
    static void testEnumParam() throws IOException {
        FieldSpec fieldSpec = FieldSpec
                .builder(String.class, "handsign", Modifier.PRIVATE, Modifier.FINAL)
                .build();
        MethodSpec methodSpec = MethodSpec
                .constructorBuilder()
                .addParameter(String.class, "handsign")
                .addStatement("this.$L = $L", "handsign", "handsign")
                .build();
        TypeSpec typeSpec = TypeSpec
                .enumBuilder("Roshambo")
                .addModifiers(Modifier.PUBLIC)
                .addEnumConstant("ROCK",
                        TypeSpec.anonymousClassBuilder("$S", "fist")
                                .addMethod(
                                        MethodSpec
                                                .methodBuilder("toString")
                                                .addModifiers(Modifier.PUBLIC)
                                                .addAnnotation(Override.class)
                                                .returns(String.class)
                                                .addStatement("return $S", "avalanche!")
                                                .build()
                                )
                                .build())
                .addEnumConstant("SCISSORS", TypeSpec.anonymousClassBuilder("$S", "peace").build())
                .addEnumConstant("PAPER", TypeSpec.anonymousClassBuilder("$S", "flat").build())
                .addField(fieldSpec)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder("com.test", typeSpec).build();
        javaFile.writeTo(System.out);
    }

    /*Inner Class内联函数
    void sortByLength(List<String> strings) {
        Collections.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.length() - b.length();
            }
        });
    }*/
    static void testInnerClass() throws IOException {

        TypeSpec innerTypeSpec = TypeSpec
                .anonymousClassBuilder("")//内部类的名字为空
                .addSuperinterface(ParameterizedTypeName.get(Comparator.class, String.class))//内部类继承类
                .addMethod(
                        MethodSpec
                                .methodBuilder("compare")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(String.class, "a")
                                .addParameter(String.class, "b")
                                .returns(int.class)
                                .addStatement("return $L.length() - $L.length()", "a", "b")
                                .build()
                )
                .build();
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("sortByLength")
                .addParameter(ParameterizedTypeName.get(List.class, String.class), "strings")
                .addStatement("$T.sort($L, $L)", Collections.class, "strings", innerTypeSpec)
                .build();

        TypeSpec typeSpec = TypeSpec
                .classBuilder("InnerClassTest")
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.test", typeSpec).build();
        javaFile.writeTo(System.out);

    }

    /*@Headers(
            accept = "application/json; charset=utf-8",
            userAgent = "Square Cash"
    )
    @HeaderList({
        @Header(name = "Accept", value = "application/json; charset=utf-8"),
        @Header(name = "User-Agent", value = "Square Cash")
    })
    LogRecord recordEvent(LogRecord logRecord);*/

    static void testAnnotations() throws IOException {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("recordEvent")
                .addAnnotation(
                        AnnotationSpec
                                .builder(Headers.class)
                                .addMember("accept", "application/json; charset=utf-8")
                                .addMember("userAgent", "Square Cash")
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec
                                .builder(List.class)
                                .addMember("value", "$L",
                                        AnnotationSpec.builder(Headers.class)
                                                .addMember("accept", "application/json; charset=utf-8")
                                                .build()
                                )
                                .addMember("value", "$L",
                                        AnnotationSpec.builder(Headers.class)
                                                .addMember("userAgent", "Square Cash")
                                                .build()
                                )
                                .build()
                )
                .addParameter(LogRecord.class, "logRecord")
                .returns(LogRecord.class)
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("TestAnnotation").addMethod(methodSpec).build();
        JavaFile javaFile = JavaFile.builder("com.test", typeSpec).build();
        javaFile.writeTo(System.out);
    }
}