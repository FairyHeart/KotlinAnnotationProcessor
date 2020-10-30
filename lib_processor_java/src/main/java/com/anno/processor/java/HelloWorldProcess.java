package com.anno.processor.java;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)//支持的源码版本
//@SupportedAnnotationTypes("com.anno.processor.java.HelloWorldAnn")//支持的注解
public class HelloWorldProcess extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;

    /**
     * 初始化必要的数据
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.filer = processingEnvironment.getFiler();//返回用于创建新的源，类或辅助文件的文件管理器
        this.messager = processingEnvironment.getMessager();//打印信息
        this.elements = processingEnvironment.getElementUtils();//返回一些用于操作元素的实用方法的实现
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(HelloWorldAnn.class);
        if (elements != null) {
            for (Element element : elements) {
                if (element instanceof TypeElement) {
                    // 创建main方法
                    MethodSpec main = MethodSpec.methodBuilder("main")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .returns(void.class)
                            .addParameter(String[].class, "args")
                            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                            .build();
                    // 创建HelloWorld类
                    String className = element.getSimpleName() + "$Processor";//新生成类名
                    TypeSpec helloWorld = TypeSpec.classBuilder(className)
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addMethod(main)
                            .build();

                    try {
                        // 生成 com.example.HelloWorld.java
                        String pageName = element.getEnclosingElement().toString();//新生成的类的包名
                        messager.printMessage(Diagnostic.Kind.WARNING, pageName);
                        JavaFile javaFile = JavaFile.builder(pageName, helloWorld)
                                .addFileComment(" This codes are generated automatically. Do not modify!")
                                .build();
                        //　生成文件
                        javaFile.writeTo(filer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 所支持的注解，有多少个就添加多少个
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(HelloWorldAnn.class.getCanonicalName());
        return annotations;
    }

    /**
     * 传入的参数
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }
}
