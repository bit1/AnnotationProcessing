package com.noogler.annotationprocessing.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;

public class SingletonGenerator {

    private static final String SUFFIX = "Singleton";
    private static final String INSTANCE_VARIABLE_NAME = "instance";

    public static void generateClass(Elements elementUtils, Filer filer, TypeElement typeElement) throws IOException {

        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        String packageName = packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();

        ClassName instanceVariable = ClassName.get(packageName, typeElement.getSimpleName().toString());

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec getInstance = MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(instanceVariable)
                .beginControlFlow("if ($N == null)", INSTANCE_VARIABLE_NAME)
                .addStatement("System.out.println(\"Getting new $T instance\")", instanceVariable)
                .addStatement("$N = new $T()", INSTANCE_VARIABLE_NAME, instanceVariable)
                .endControlFlow()
                .addStatement("return $N", INSTANCE_VARIABLE_NAME)
                .build();

        TypeSpec singletonClassBuilder = TypeSpec.classBuilder(typeElement.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethod(getInstance)
                .addField(ClassName.get(packageName, typeElement.getSimpleName().toString()),
                        INSTANCE_VARIABLE_NAME, Modifier.PRIVATE, Modifier.STATIC)
                .build();

        JavaFile.builder(packageName, singletonClassBuilder).build().writeTo(filer);
    }

}
