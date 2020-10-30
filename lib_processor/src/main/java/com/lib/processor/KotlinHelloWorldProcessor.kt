package com.lib.processor

import com.google.auto.service.AutoService
import com.lib.anno.KotlinFieldAnn
import com.lib.anno.KotlinHelloWorldAnn
import com.lib.anno.KotlinMethodAnn
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class KotlinHelloWorldProcessor : AbstractProcessor() {

    private var filer: Filer? = null
    private var messager: Messager? = null

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        filer = processingEnvironment?.filer
        messager = processingEnvironment?.messager
    }

    override fun process(
        typeElements: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        if (typeElements == null || typeElements.isEmpty()) {
            return false
        }
        roundEnvironment?.getElementsAnnotatedWith(KotlinHelloWorldAnn::class.java)
            ?.forEach { element ->
                when (element) {
                    is TypeElement -> {//类
                        printTypeElement(element)
                        val funSpec = FunSpec.builder("main")
                            .addModifiers(KModifier.PRIVATE)
                            .addParameter("args", String::class, KModifier.VARARG)
                            .build()

                        val className = "${element.simpleName}\$Processor"
                        val typeSpec = TypeSpec.classBuilder(className)
                            .addFunction(funSpec)
                            .addModifiers(KModifier.FINAL)
                            .build()

                        val fileType = FileSpec
                            .builder(element.enclosingElement.toString(), className)
                            .addType(typeSpec)
                            .build()
                        if (filer != null) {
                            fileType.writeTo(filer!!)
                        }
                    }
                    is ExecutableElement -> {//方法
                    }
                    is VariableElement -> {//属性
                    }
                }
            }
        roundEnvironment?.getElementsAnnotatedWith(KotlinMethodAnn::class.java)
            ?.forEach { element ->
                when (element) {
                    is TypeElement -> {//类
                    }
                    is ExecutableElement -> {//方法
                        printExecutableElement(element)
                    }
                    is VariableElement -> {//属性
                    }
                }
            }

        roundEnvironment?.getElementsAnnotatedWith(KotlinFieldAnn::class.java)
            ?.forEach { element ->
                when (element) {
                    is TypeElement -> {//类
                    }
                    is ExecutableElement -> {//方法
                    }
                    is VariableElement -> {//属性
                        printVariableElement(element)
                    }
                }
            }

        return true
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(KotlinHelloWorldAnn::class.java.canonicalName)
    }

    /**
    enclosedElements 返回在此类或接口中直接声明的字段，方法，构造函数和成员类型: aa / param / print(java.lang.String) / onEvent(android.media.MediaDrm,byte[],int,int,byte[]) / getParam() / setParam(java.lang.String) / KotlinHelloWorld(java.lang.String) / KotlinHelloWorld() /
    enclosingElement 返回顶级类型的包: com.anno.processor.test
    interfaces 返回由此类直接实现或由此接口扩展的接口类型: android.media.MediaDrm.OnEventListener
    nestingKind 返回此类型元素的 嵌套类型: TOP_LEVEL
    qualifiedName : com.anno.processor.test.KotlinHelloWorld
    simpleName : KotlinHelloWorld
    superclass 返回此类型元素的直接超类: java.lang.Object
    typeParameters :
     */
    private fun printTypeElement(element: TypeElement) {
        val kind = Diagnostic.Kind.WARNING
        messager?.printMessage(kind, "------TypeElement--------")
        val sb = StringBuilder()
        sb.append("\nenclosedElements : ")
        element.enclosedElements.let {
            it?.forEach { ele ->
                sb.append(ele.toString()).append(" / ")
            }
        }
        sb.append("\nenclosingElement : ")
        element.enclosingElement.let {
            sb.append(it.toString())
        }
        sb.append("\ninterfaces : ")
        element.interfaces.let {
            it?.forEach { iter ->
                sb.append(iter.toString())
            }
        }
        sb.append("\nnestingKind : ").append(element.nestingKind.toString())
        sb.append("\nqualifiedName : ").append(element.qualifiedName.toString())
        sb.append("\nsimpleName : ").append(element.simpleName.toString())
        sb.append("\nsuperclass : ").append(element.superclass.toString())
        sb.append("\ntypeParameters : ")
        element.typeParameters.let {
            it.forEach { typeParameter ->
                sb.append(typeParameter.toString())
            }
        }
        messager?.printMessage(kind, sb)
    }

    /**
    AnnotationValue 如果此可执行文件是注释类型元素，则返回默认值: null
    parameters 返回此可执行文件的形式参数: test /
    receiverType 返回此可执行文件的接收方类型，如果可执行文件没有接收方类型，则返回NoType ，类型为NONE: null
    returnType 返回此可执行文件的返回类型: boolean
    simpleName 返回构造函数，方法或初始值设定项的简单名称: print
    isDefault 返回 true如果此方法是默认的方法，并返回 false其他: false
    isVarArgs 返回 true如果此方法或构造接受可变数量的参数，并返回 false否则: false
    typeParameters 以声明顺序返回此可执行文件的形式类型参数:
     */
    private fun printExecutableElement(element: ExecutableElement) {
        val kind = Diagnostic.Kind.WARNING
        messager?.printMessage(kind, "------ExecutableElement--------")
        val sb = StringBuilder()
        sb.append("AnnotationValue : ").append(element.defaultValue?.toString())
        sb.append("\nparameters : ")
        element.parameters.let {
            it?.forEach { variableElement ->
                sb.append(variableElement.toString()).append(" / ")
            }
        }
        sb.append("\nreceiverType : ").append(element.receiverType?.toString())
        sb.append("\nreturnType : ").append(element.returnType?.toString())
        sb.append("\nsimpleName : ").append(element.simpleName?.toString())
        sb.append("\nisDefault : ").append(element.isDefault.toString())
        sb.append("\nisVarArgs : ").append(element.isVarArgs.toString())
        sb.append("\ntypeParameters : ")
        element.typeParameters.let {
            it?.forEach { typeParameterElement ->
                sb.append(typeParameterElement.toString()).append(" / ")
            }
        }
        messager?.printMessage(kind, sb)
    }
    /**
    constantValue 如果这是一个初始化为编译 final量的 final字段，则返回此变量的值: xyz
    enclosingElement 返回此变量的封闭元素: com.anno.processor.test.KotlinHelloWorld
    simpleName 返回此变量元素的简单名称: aa
     */
    private fun printVariableElement(element: VariableElement) {
        val kind = Diagnostic.Kind.WARNING
        messager?.printMessage(kind, "------VariableElement--------")
        val sb = StringBuilder()
        sb.append("\nconstantValue : ").append(element.constantValue?.toString())
        sb.append("\nenclosingElement : ").append(element.enclosingElement?.toString())
        sb.append("\nsimpleName : ").append(element.simpleName?.toString())

        messager?.printMessage(kind, sb)
    }
}