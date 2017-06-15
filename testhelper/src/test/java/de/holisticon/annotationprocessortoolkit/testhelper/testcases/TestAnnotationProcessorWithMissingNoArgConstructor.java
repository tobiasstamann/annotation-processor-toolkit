package de.holisticon.annotationprocessortoolkit.testhelper.testcases;


import de.holisticon.annotationprocessortoolkit.AbstractAnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Test annotation processor. Does nothing.
 */

@SupportedAnnotationTypes("de.holisticon.annotationprocessortoolkit.testhelper.TestAnnotation")
public class TestAnnotationProcessorWithMissingNoArgConstructor extends AbstractAnnotationProcessor {

    public TestAnnotationProcessorWithMissingNoArgConstructor(String x) {

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}