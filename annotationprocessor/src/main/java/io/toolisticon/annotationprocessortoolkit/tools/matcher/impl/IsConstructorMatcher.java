package io.toolisticon.annotationprocessortoolkit.tools.matcher.impl;

import io.toolisticon.annotationprocessortoolkit.tools.ElementUtils;
import io.toolisticon.annotationprocessortoolkit.tools.matcher.ImplicitMatcher;

import javax.lang.model.element.Element;


/**
 * Implicit matcher that checks if a passed element is a constructor.
 */
public class IsConstructorMatcher<ELEMENT extends Element> implements ImplicitMatcher<ELEMENT> {

    @Override
    public boolean check(ELEMENT element) {
        return ElementUtils.CheckKindOfElement.isConstructor(element);
    }

}