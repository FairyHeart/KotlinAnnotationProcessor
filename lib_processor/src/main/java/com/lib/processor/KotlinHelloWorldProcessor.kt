package com.lib.processor

import com.google.auto.service.AutoService
import com.lib.anno.KotlinHelloWorldAnn
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement


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

        typeElements.forEach {
            if (it.qualifiedName.toString() == KotlinHelloWorldAnn::class.java.canonicalName) {
                val funSpec = FunSpec.builder("main")
                    .addModifiers(KModifier.PRIVATE)
                    .addParameter("args", String::class, KModifier.VARARG)
                    .build()

                val className = "${it.simpleName}\$Processor"
                val typeSpec = TypeSpec.classBuilder(className)
                    .addFunction(funSpec)
                    .addModifiers(KModifier.FINAL)
                    .build()

                val fileType = FileSpec
                    .builder("com.test", className)
                    .addType(typeSpec)
                    .build()
                if (filer != null) {
                    fileType.writeTo(filer!!)
                }
            }
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(KotlinHelloWorldAnn::class.java.canonicalName)
    }

    /*
     roundEnvironment?.getElementsAnnotatedWith(KotlinHelloWorldAnn::class.java)?.forEach { element ->
            when (element) {
                is TypeElement -> {

                }
                is ExecutableElement -> {

                }
                is VariableElement -> {
                }
            }
        }
    * */
}