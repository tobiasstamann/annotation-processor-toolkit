package de.holisticon.annotationprocessortoolkit.tools;

import de.holisticon.annotationprocessortoolkit.AbstractAnnotationProcessorTestBaseClass;
import de.holisticon.annotationprocessortoolkit.tools.ElementUtils.AccessEnclosedElements;
import de.holisticon.annotationprocessortoolkit.tools.ElementUtils.CastElement;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for {@link de.holisticon.annotationprocessortoolkit.tools.TypeUtils}.
 */
@RunWith(Parameterized.class)
public class TypeUtilsTest extends AbstractAnnotationProcessorTestBaseClass {


    public TypeUtilsTest(String message, AbstractAnnotationProcessorTestBaseClass.AbstractTestAnnotationProcessorClass testcase, boolean compilationShouldSucceed) {
        super(TypeUtils.class.getSimpleName() + ": " + message, testcase, compilationShouldSucceed);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static List<Object[]> data() {
        return Arrays.asList(

                new Object[][]{
                        {
                                "TypeUtils : Get TypeElement for class",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeElement typeElement = getTypeUtils().getTypeElementForClass(AbstractTestAnnotationProcessorClass.class);

                                        MatcherAssert.assertThat(typeElement, Matchers.notNullValue());
                                        MatcherAssert.assertThat(typeElement.getSimpleName().toString(), Matchers.is(AbstractTestAnnotationProcessorClass.class.getSimpleName()));

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : Get TypeElement for array class",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeElement typeElement = getTypeUtils().getTypeElementForClass(String[].class);

                                        MatcherAssert.assertThat("An array TypeMirror can't be converted into a TypeElement so result has to be null", typeElement, Matchers.nullValue());

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : Get TypeMirror for class",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(AbstractTestAnnotationProcessorClass.class);

                                        MatcherAssert.assertThat(typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat(typeMirror.getKind(), Matchers.is(TypeKind.DECLARED));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : Get TypeMirror for array class",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat(typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat(typeMirror.getKind(), Matchers.is(TypeKind.ARRAY));
                                        MatcherAssert.assertThat(((ArrayType) typeMirror).getComponentType(), Matchers.is(getTypeUtils().getTypeMirrorForClass(String.class)));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.isTypeMirrorAnArrayOfType : ",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat(typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat(typeMirror.getKind(), Matchers.is(TypeKind.ARRAY));
                                        MatcherAssert.assertThat(((ArrayType) typeMirror).getComponentType(), Matchers.is(getTypeUtils().getTypeMirrorForClass(String.class)));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeMirrorArraysComponentType : Get component type of TypeMirror array ",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should not be null", typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should be array", typeMirror.getKind(), Matchers.is(TypeKind.ARRAY));

                                        MatcherAssert.assertThat(getTypeUtils().getTypeMirrorArraysComponentType(typeMirror), Matchers.is(getTypeUtils().getTypeMirrorForClass(String.class)));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.isTypeMirrorAnArrayOfType : Should check if the TypeMirror has a specific component type correctly",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should not be null", typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should be array", typeMirror.getKind(), Matchers.is(TypeKind.ARRAY));

                                        MatcherAssert.assertThat("Should detect matching component type correctly", getTypeUtils().isTypeMirrorAnArrayOfType(typeMirror, String.class));
                                        MatcherAssert.assertThat("Should detect non matching component type correctly", !getTypeUtils().isTypeMirrorAnArrayOfType(typeMirror, Boolean.class));

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.isTypeMirrorAnArrayOfType : Should check if the TypeMirror has a specific component type correctly",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should not be null", typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should be array", typeMirror.getKind(), Matchers.is(TypeKind.ARRAY));

                                        MatcherAssert.assertThat("Should detect matching component type correctly", getTypeUtils().isTypeMirrorAnArrayOfType(typeMirror, String.class.getCanonicalName()));
                                        MatcherAssert.assertThat("Should detect non matching component type correctly", !getTypeUtils().isTypeMirrorAnArrayOfType(typeMirror, Boolean.class.getCanonicalName()));

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.isTypeMirrorAnArrayOfType : Should check if the TypeMirror has a specific component type correctly",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        TypeMirror typeMirror = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should not be null", typeMirror, Matchers.notNullValue());
                                        MatcherAssert.assertThat("PRECONDITION : typeMirror should be array", typeMirror.getKind(), Matchers.is(TypeKind.ARRAY));

                                        MatcherAssert.assertThat("Should detect matching component type correctly", getTypeUtils().isTypeMirrorAnArrayOfType(typeMirror, getTypeUtils().getTypeMirrorForClass(String.class)));
                                        MatcherAssert.assertThat("Should detect non matching component type correctly", !getTypeUtils().isTypeMirrorAnArrayOfType(typeMirror, getTypeUtils().getTypeMirrorForClass(Boolean.class)));

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : test isAssignableToType",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat("type element should be detected as assignable to Object", getTypeUtils().isAssignableToType(element, Object.class));
                                        MatcherAssert.assertThat("type element shouldn't be detected as assignable to InputStream", !getTypeUtils().isAssignableToType(element, InputStream.class));

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : test isAssignableToTypeElement",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat("type element should be detected as assignable to Object", getTypeUtils().isAssignableToTypeElement(element, getTypeUtils().getTypeElementForClass(Object.class)));
                                        MatcherAssert.assertThat("type element shouldn't be detected as assignable to InputStream", !getTypeUtils().isAssignableToTypeElement(element, getTypeUtils().getTypeElementForClass(InputStream.class)));

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : test isAssignableToTypeMirror",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat("type element should be detected as assignable to Object", getTypeUtils().isAssignableToTypeMirror(element, getTypeUtils().getTypeMirrorForClass(Object.class)));
                                        MatcherAssert.assertThat("type element shouldn't be detected as assignable to InputStream", !getTypeUtils().isAssignableToTypeMirror(element, getTypeUtils().getTypeMirrorForClass(InputStream.class)));

                                    }
                                },
                                true

                        },
                        {
                                "TypeUtils : test check for void type ",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().isVoidType(CastElement.castMethod(AccessEnclosedElements.getEnclosedElementsByName(element, "synchronizedMethod").get(0)).getReturnType()), Matchers.is(true));
                                        MatcherAssert.assertThat(getTypeUtils().isVoidType(element.asType()), Matchers.is(false));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils : get encapsulated javax.lang.model.util.Types instance",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypes(), Matchers.notNullValue());

                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeElementForFullQualifiedClassName() : test to get Element by class name",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeElementForFullQualifiedClassName("de.holisticon.annotationprocessor.AnnotationProcessorTestClass"), Matchers.is(element));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeElementForFullQualifiedClassName() : test behavior with non existing class name parameter",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeElementForFullQualifiedClassName("de.holisticon.annotationprocessor.AnnotationProcessorTestClassXXXX"), Matchers.nullValue());


                                    }
                                },
                                true
                        },
                        {
                                "TypeUtils.getTypeElementForFullQualifiedClassName() : test behavior with null valued class name parameter",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeElementForFullQualifiedClassName(null), Matchers.nullValue());


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeElementForFullQualifiedClassName() : test to get TypeMirror by class name",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeMirrorForFullQualifiedClassName("de.holisticon.annotationprocessor.AnnotationProcessorTestClass"), Matchers.is(element.asType()));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeMirrorForFullQualifiedClassName() : test to get TypeMirror by class name",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeMirrorForFullQualifiedClassName("de.holisticon.annotationprocessor.AnnotationProcessorTestClass"), Matchers.is(element.asType()));


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeMirrorForFullQualifiedClassName() : test behavior with non existing class name parameter",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeMirrorForFullQualifiedClassName("de.holisticon.annotationprocessor.AnnotationProcessorTestClassXXXX"), Matchers.nullValue());


                                    }
                                },
                                true
                        },
                        {
                                "TypeUtils.getTypeMirrorForFullQualifiedClassName() : test behavior with null valued class name parameter",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        MatcherAssert.assertThat(getTypeUtils().getTypeMirrorForFullQualifiedClassName(null), Matchers.nullValue());


                                    }
                                },
                                true


                        },
                        {
                                "TypeUtils.getTypeMirrorArraysComponentType() : test if component type of TypeMirror of kind ARRAY is returned correctly",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        TypeMirror input = getTypeUtils().getTypeMirrorForClass(String[].class);

                                        MatcherAssert.assertThat(getTypeUtils().getTypeMirrorArraysComponentType(input), Matchers.is(getTypeUtils().getTypeMirrorForClass(String.class)));

                                    }
                                },
                                true


                        },


                }

        );


    }

}
