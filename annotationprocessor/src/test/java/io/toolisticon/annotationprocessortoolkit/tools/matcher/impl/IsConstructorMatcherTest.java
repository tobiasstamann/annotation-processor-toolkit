package io.toolisticon.annotationprocessortoolkit.tools.matcher.impl;

import io.toolisticon.annotationprocessortoolkit.AbstractUnitTestAnnotationProcessorClass;
import io.toolisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.annotationprocessortoolkit.tools.MessagerUtils;
import io.toolisticon.annotationprocessortoolkit.tools.TypeUtils;
import io.toolisticon.annotationprocessortoolkit.tools.corematcher.CoreMatchers;
import io.toolisticon.compiletesting.CompileTestBuilder;
import io.toolisticon.compiletesting.JavaFileObjectUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.List;


/**
 * Unit test for {@link IsConstructorMatcher}.
 */
public class IsConstructorMatcherTest {

    private CompileTestBuilder.UnitTestBuilder unitTestBuilder = CompileTestBuilder
            .unitTest()
            .useSource(JavaFileObjectUtils.readFromResource("/AnnotationClassAttributeTestClass.java"));


    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);
    }

    @Test
    public void checkMatchingConstructor() {

        unitTestBuilder.useProcessor(new AbstractUnitTestAnnotationProcessorClass() {
            @Override
            protected void testCase(TypeElement element) {

                TypeElement typeElement = TypeUtils.TypeRetrieval.getTypeElement(IsConstructorMatcherTest.class);
                List<? extends Element> constructors = ElementUtils.AccessEnclosedElements.getEnclosedElementsOfKind(typeElement, ElementKind.CONSTRUCTOR);
                MatcherAssert.assertThat("Precondition: must have found a enum", constructors.size() >= 1);


                MatcherAssert.assertThat("Should return true for enum : ", CoreMatchers.IS_CONSTRUCTOR.getMatcher().check(constructors.get(0)));

            }
        })
                .compilationShouldSucceed()
                .testCompilation();
    }

    @Test
    public void checkMismatchingConstructor() {

        unitTestBuilder.useProcessor(new AbstractUnitTestAnnotationProcessorClass() {
            @Override
            protected void testCase(TypeElement element) {

                MatcherAssert.assertThat("Should return false for non constructor : ", !CoreMatchers.IS_CONSTRUCTOR.getMatcher().check(element));

            }
        })
                .compilationShouldSucceed()
                .testCompilation();
    }

    @Test
    public void checknullValuedElement() {

        unitTestBuilder.useProcessor(new AbstractUnitTestAnnotationProcessorClass() {
            @Override
            protected void testCase(TypeElement element) {

                MatcherAssert.assertThat("Should return false for null valued element : ", !CoreMatchers.IS_CONSTRUCTOR.getMatcher().check(null));

            }
        })
                .compilationShouldSucceed()
                .testCompilation();
    }


}