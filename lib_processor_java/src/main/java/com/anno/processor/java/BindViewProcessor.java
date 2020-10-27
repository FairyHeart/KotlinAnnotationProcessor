package com.anno.processor.java;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Administrator
 */
@AutoService(Process.class)
@SupportedAnnotationTypes({"com.anno.processor.java.BindView"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class BindViewProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    /**
     * 生成源代码
     */
    private Filer filer;
    /**
     * 打印信息
     */
    private Messager messager;

    /**
     * 实现的接口
     */
    private static final ClassName VIEW_BINDER = ClassName.get("injecter.api", "ViewBinder");

    /**
     * 生成类的后缀 以后会用反射去取
     */
    private static final String BINDING_CLASS_SUFFIX = "$$ViewBinder";

    /**
     * 初始化必要的数据
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    /**
     * 所支持的注解，有多少个就添加多少个
     * @SupportedAnnotationTypes注解代替
     *
     * @return
     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> types = new LinkedHashSet<>();
//        types.add(BindView.class.getCanonicalName());
//        return types;
//    }

    /**
     * 支持的源码版本
     *
     * @return
     * @SupportedSourceVersion注解代替
     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latest();
//    }

    /**
     * 传入的参数
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    /**
     * 获取注解信息，动态生成代码
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        // 获得被该注解声明的元素
        Set<? extends Element> elememts = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        TypeElement classElement = null;// 声明类元素
        List<VariableElement> fields = null;// 声明一个存放成员变量的列表
        // 存放二者
        Map<String, List<VariableElement>> maps = new HashMap<String, List<VariableElement>>();

        for (Element elememt : elememts) {
            if (elememt.getKind() == ElementKind.CLASS) {

            } else if (elememt.getKind() == ElementKind.FIELD) {

            }
        }
        return true;
    }

    /**
     * @param annotations
     * @param elements
     * @return
     */
    private Set<TypeElement> getTypeElementsByAnnotationType(Set<? extends TypeElement> annotations, Set<? extends Element> elements) {
        Set<TypeElement> result = new HashSet<>();
        //遍历包含的 package class method,Element代表程序中的包名、类、方法，这也是注解所支持的作用类型
        for (Element element : elements) {
            //匹配 class or interface
            if (element instanceof TypeElement) {
                boolean found = false;
                //遍历class中包含的 filed method constructors,getEnclosedElements()：获元素中的闭包的注解元素
                for (Element subElement : element.getEnclosedElements()) {
                    //遍历element中包含的注释,getAnnotationMirrors()：获取上述闭包元素的所有注解
                    for (AnnotationMirror annotationMirror : subElement.getAnnotationMirrors()) {
                        for (TypeElement annotation : annotations) {
                            //匹配注释
                            if (annotationMirror.getAnnotationType().asElement().equals(annotation)) {
                                result.add((TypeElement) element);
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            }
        }
        return result;
    }
}
