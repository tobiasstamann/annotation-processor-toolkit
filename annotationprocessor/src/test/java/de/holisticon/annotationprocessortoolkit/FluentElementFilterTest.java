package de.holisticon.annotationprocessortoolkit;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for {@link de.holisticon.annotationprocessortoolkit.filter.FluentElementFilter}.
 */
@RunWith(Parameterized.class)
public class FluentElementFilterTest extends AbstractAnnotationProcessorTestBaseClass {

    public FluentElementFilterTest(String message, AbstractTestAnnotationProcessorClass testcase, boolean compilationShouldSucceed) {
        super(FluentElementFilterTest.class.getSimpleName() + ": " + message, testcase, compilationShouldSucceed);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static List<Object[]> data() {
        return Arrays.asList(

                new Object[][]{


                        {
                                "filterByKinds",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(element.getEnclosedElements()).filterByKinds(ElementKind.FIELD).getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(7));

                                        for (Element resultElement : results) {
                                            MatcherAssert.assertThat(resultElement.getKind(), Matchers.is(ElementKind.FIELD));
                                        }


                                    }
                                },
                                true


                        },
                        {
                                "filterByKinds and filterByModifiers",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByKinds(ElementKind.FIELD).filterByModifiers(Modifier.PUBLIC, Modifier.STATIC).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(1));
                                        MatcherAssert.assertThat(result.get(0).getSimpleName().toString(), Matchers.is("publicStaticField"));

                                    }
                                },
                                true


                        },
                        {
                                "filterByKinds : null valued element",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(null).filterByKinds(ElementKind.FIELD).getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByKinds : null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(element.getEnclosedElements()).filterByKinds(null).getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByKinds : null non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(element.getEnclosedElements()).filterByKinds().getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },

                        //--
                        {
                                "inverseFilterByKinds : return list for matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByKinds(ElementKind.FIELD).getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(element.getEnclosedElements().size() - 7));

                                        for (Element resultElement : results) {
                                            MatcherAssert.assertThat(resultElement.getKind(), Matchers.not(ElementKind.FIELD));
                                        }


                                    }
                                },
                                true


                        },


                        {
                                "inverseFilterByKinds : null valued element",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(null).inverseFilterByKinds(ElementKind.FIELD).getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByKinds : null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByKinds(null).getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByKinds : null non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        List<? extends Element> results = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByKinds().getResult();
                                        MatcherAssert.assertThat(results, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },


                        {
                                "filterByNames : returns list for one matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // one search attribute
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNames("publicStaticField").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(1));
                                        MatcherAssert.assertThat(result.get(0).getSimpleName().toString(), Matchers.is("publicStaticField"));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNames : returns list for two matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // two search attributes
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNames("publicStaticField", "synchronizedMethod").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(2));
                                        MatcherAssert.assertThat(convertToList(result.get(0).getSimpleName().toString(), result.get(1).getSimpleName().toString()), Matchers.containsInAnyOrder("publicStaticField", "synchronizedMethod"));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNames : returns empty list for non matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // returns empty result
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNames("XXX").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNames : returns empty list for non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle no passed filter args correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNames().getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNames : returns empty list for null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNames(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));

                                    }
                                },
                                true


                        },

                        {
                                "filterByNames : returns empty list for null value element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // null valued element list
                                        List<? extends Element> result =
                                                createFluentElementFilter(null).filterByNames(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));

                                    }
                                },
                                true


                        },

                        // ....


                        {
                                "inverseFilterByNames : returns list for one matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // one search attribute
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByNames("publicStaticField").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size() - 1));

                                        for (Element resultElement : result) {
                                            MatcherAssert.assertThat(resultElement.getSimpleName().toString(), Matchers.not("publicStaticField"));
                                        }


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByNames : returns list for two matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // two search attributes
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByNames("publicStaticField", "synchronizedMethod").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size() - 2));
                                        for (Element resultElement : result) {
                                            MatcherAssert.assertThat("Must nor be publicStaticField or synchronizedMethod", !resultElement.getSimpleName().equals("publicStaticField") && !resultElement.getSimpleName().equals("synchronizedMethod"));
                                        }

                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByNames : returns empty list for non matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // returns empty result
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByNames("XXX").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByNames : returns empty list for non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle no passed filter args correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByNames().getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByNames : returns empty list for null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByNames(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size()));

                                    }
                                },
                                true


                        },

                        {
                                "inverseFilterByNames : returns empty list for null value element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // null valued element list
                                        List<? extends Element> result =
                                                createFluentElementFilter(null).inverseFilterByNames(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));

                                    }
                                },
                                true


                        },


                        {
                                "filterByNameWithRegularExpressions : returns empty list for one matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // one search attribute
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNameWithRegularExpressions("publicSt.*Field").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(1));
                                        MatcherAssert.assertThat(result.get(0).getSimpleName().toString(), Matchers.is("publicStaticField"));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNameWithRegularExpressions : returns list for two matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // two search attributes
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNameWithRegularExpressions("publicSt.*Field", "synchr.*Method").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(2));
                                        MatcherAssert.assertThat(convertToList(result.get(0).getSimpleName().toString(), result.get(1).getSimpleName().toString()), Matchers.containsInAnyOrder("publicStaticField", "synchronizedMethod"));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNameWithRegularExpressions : returns empty list for non matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // returns empty result
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNameWithRegularExpressions("XXX").getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNameWithRegularExpressions : returns empty list for non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle no passed filter args correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNameWithRegularExpressions().getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNameWithRegularExpressions : returns empty list for null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByNameWithRegularExpressions(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByNameWithRegularExpressions : returns empty list for null value element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(null).filterByNameWithRegularExpressions(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },


                        {
                                "filterByAnnotation : returns empty list for one matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // one search attribute
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByAnnotation(FilterTestAnnotation1.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(1));
                                        MatcherAssert.assertThat(result.get(0).getSimpleName().toString(), Matchers.is("synchronizedMethod"));


                                    }
                                },
                                true


                        },
                        {
                                "filterByAnnotation : returns list for two matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // two search attributes
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByAnnotation(FilterTestAnnotation1.class, FilterTestAnnotation2.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(1));
                                        MatcherAssert.assertThat(convertToList(result.get(0).getSimpleName().toString()), Matchers.containsInAnyOrder("synchronizedMethod"));


                                    }
                                },
                                true


                        },
                        {
                                "filterByAnnotation : returns empty list for non matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // returns empty result
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByAnnotation(TestAnnotation.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByAnnotation : returns empty list for non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle no passed filter args correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByAnnotation().getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByAnnotation : returns empty list for null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).filterByAnnotation(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },
                        {
                                "filterByAnnotation : returns empty list for null value element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(null).filterByAnnotation(FilterTestAnnotation1.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },


                        // --

                        {
                                "inverseFilterByAnnotation : returns list for one matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // one search attribute
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByAnnotation(FilterTestAnnotation1.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size() - 1));

                                        for (Element resultElement : result) {
                                            MatcherAssert.assertThat(resultElement.getSimpleName().toString(), Matchers.not("synchronizedMethod"));
                                        }

                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByAnnotation : returns list for two matching filter arguments",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // two search attributes
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByAnnotation(FilterTestAnnotation1.class, FilterTestAnnotation2.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size() - 1));

                                        for (Element resultElement : result) {
                                            MatcherAssert.assertThat(resultElement.getSimpleName().toString(), Matchers.not("synchronizedMethod"));
                                        }


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByAnnotation : returns full list for non matching filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // returns empty result
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByAnnotation(FilterTestAnnotation1.class, TestAnnotation.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByAnnotation : returns full list for non existing filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle no passed filter args correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByAnnotation().getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByAnnotation : returns full list for null valued filter argument",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(element.getEnclosedElements()).inverseFilterByAnnotation(null).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(element.getEnclosedElements().size()));


                                    }
                                },
                                true


                        },
                        {
                                "inverseFilterByAnnotation : returns empty list for null value element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // handle nulls correctly
                                        List<? extends Element> result = createFluentElementFilter(null).inverseFilterByAnnotation(FilterTestAnnotation1.class).getResult();
                                        MatcherAssert.assertThat(result, Matchers.hasSize(0));


                                    }
                                },
                                true


                        },

                        {
                                "isEmpty : succeeding validation with empty element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects empty results correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(new ArrayList<Element>()).isEmpty(), Matchers.is(true));


                                    }
                                },
                                true
                        },
                        {
                                "isEmpty : failing validation with  multiple element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // detects non empty result correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(element.getEnclosedElements()).isEmpty(), Matchers.is(false));

                                    }
                                },
                                true
                        },
                        {
                                "isEmpty : succeeding validation with null valued element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // detects non empty result correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(null).isEmpty(), Matchers.is(true));

                                    }
                                },
                                true
                        },
                        {
                                "hasSingleElemen : failing validation with empty element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects empty results correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(new ArrayList<Element>()).hasSingleElement(), Matchers.is(false));

                                    }
                                },
                                true
                        },
                        {
                                "hasSingleElement : succeeding validation with one element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects single result elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(convertToList(element)).hasSingleElement(), Matchers.is(true));


                                    }
                                },
                                true
                        },
                        {
                                "hasSingleElement : failing validation with multiple element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // detects multiple elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(element.getEnclosedElements()).hasSingleElement(), Matchers.is(false));


                                    }
                                },
                                true
                        },
                        {
                                "hasSingleElement : failing validation with null valued element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // detects multiple elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(null).hasSingleElement(), Matchers.is(false));


                                    }
                                },
                                true
                        },
                        {
                                "hasMultipleElements : succeeding validation with multiple elements",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects multiple elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(element.getEnclosedElements()).hasMultipleElements(), Matchers.is(true));


                                    }
                                },
                                true
                        },
                        {
                                "hasMultipleElements : failing validation with empty list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects empty results correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(new ArrayList<Element>()).hasMultipleElements(), Matchers.is(false));

                                    }
                                },
                                true
                        },
                        {
                                "hasMultipleElements : failing validation with one element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects single result elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(convertToList(element)).hasMultipleElements(), Matchers.is(false));


                                    }
                                },
                                true
                        },
                        {
                                "hasMultipleElements : failing validation with null valued element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects single result elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(null).hasMultipleElements(), Matchers.is(false));


                                    }
                                },
                                true
                        },
                        {
                                "hasSize : suucceeding validition with empty empty list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects empty results correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(new ArrayList<Element>()).hasSize(0), Matchers.is(true));

                                    }
                                },
                                true
                        },
                        {
                                "hasSize : succeeding validition with one element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // detects single result elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(convertToList(element)).hasSize(1), Matchers.is(true));


                                    }
                                },
                                true
                        },
                        {
                                "hasSize : succeeding validition with multi element list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects multiple elements correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(element.getEnclosedElements()).hasSize(element.getEnclosedElements().size()), Matchers.is(true));


                                    }
                                },
                                true
                        },
                        {
                                "hasSize : succeeding validition with null valued empty list",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {


                                        // detects null valued element list correctly
                                        MatcherAssert.assertThat(createFluentElementFilter((List<Element>) null).hasSize(3), Matchers.is(false));


                                    }
                                },
                                true
                        },
                        {
                                "hasSize : failing validation",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects empty results correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(element.getEnclosedElements()).hasSize(0), Matchers.is(false));


                                    }
                                },
                                true
                        },
                        {
                                "hasSize : failing validation",
                                new AbstractTestAnnotationProcessorClass() {
                                    @Override
                                    protected void testCase(TypeElement element) {

                                        // detects empty results correctly
                                        MatcherAssert.assertThat(createFluentElementFilter(element.getEnclosedElements()).hasSize(0), Matchers.is(false));


                                    }
                                },
                                true
                        }

                }

        );


    }


    public static <T> List<T> convertToList(T... element) {

        return Arrays.asList(element);

    }

}