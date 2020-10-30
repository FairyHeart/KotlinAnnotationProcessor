package com.lib.processor

import com.google.auto.service.AutoService
import com.lib.anno.BindView
import com.squareup.kotlinpoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class BindViewProcessor : AbstractProcessor() {

    var filer: Filer? = null
    var messager: Messager? = null

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        filer = processingEnvironment?.filer
        messager = processingEnvironment?.messager
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        environment: RoundEnvironment?
    ): Boolean {
        val element = environment?.getElementsAnnotatedWith(BindView::class.java)
        element?.forEach {
            when (it) {
                is TypeElement -> {

                }
                is ExecutableElement -> {

                }
                is VariableElement -> {
                    this.createFile(it)
                }
            }
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(BindView::class.java.canonicalName)
    }

    private fun createFile(element: VariableElement) {
        val kind = Diagnostic.Kind.WARNING

        val view = ClassName("android.view", "View")

        val packageName = element.enclosingElement.enclosingElement.toString()
        val fileName = element.enclosingElement.simpleName.toString() + "_ViewBind"
        val target = element.enclosingElement.asType().asTypeName()

        val funSpec = FunSpec.builder("bindView")
            .addParameter("target", target)
            .addParameter("view", view)
            .addStatement("target.%L = view.findViewById(%L)", element.simpleName, element)
            .build()

        val typeSpec = TypeSpec
            .classBuilder(fileName)
            .addFunction(funSpec)
            .build()

        val fileSpec = FileSpec
            .builder(packageName, fileName)
            .addType(typeSpec)
            .build()
        filer?.let { fileSpec.writeTo(it) }
    }
}