package com.noogler.annotationprocessing.processor;

import com.google.auto.service.AutoService;
import com.noogler.annotationprocessing.annotation.Singleton;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class SingletonAnnotationProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotations = new LinkedHashSet<>();
        supportedAnnotations.add(Singleton.class.getCanonicalName());
        return supportedAnnotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            // Scan classes to get ones annotated with @Singleton
            // TODO: Dont hardcode Singleton.class here
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Singleton.class)) {
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(annotatedElement, "Only classes can be annotated with @Singleton");
                }
                checkValidity((TypeElement) annotatedElement);
                SingletonGenerator.generateClass(elementUtils, filer, (TypeElement) annotatedElement);
            }
        } catch (ProcessingException e) {
            logError(e.getElement(), e.getMessage());
        } catch (IOException e) {
            logError(null, e.getMessage());
        }
        return true;
    }

    private void logError(Element element, String errorMsg) {
        messager.printMessage(Diagnostic.Kind.ERROR, errorMsg, element);
    }

    private void checkValidity(TypeElement typeElement) throws ProcessingException {
        // Class should be public
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(typeElement, "The class %s is not public",
                    typeElement.getQualifiedName().toString());
        }

        // Class should not be abstract
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(typeElement, "The class %s is abstract. " +
                    "Classes annotated with @Singleton cannot be abstract", typeElement.getQualifiedName().toString());
        }

        // Check if a public zero args constructor is present
        for (Element enclosed: typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (!(constructorElement.getParameters().size() == 0 &&
                        constructorElement.getModifiers().contains(Modifier.PUBLIC))) {
                    throw new ProcessingException(typeElement, "The class %s must provide a public constructor with " +
                            "zero args", typeElement.getQualifiedName().toString());
                }
            }
        }
    }
}