package de.holisticon.annotationprocessortoolkit.tools;

import de.holisticon.annotationprocessortoolkit.internal.FrameworkToolWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Utility class and wrapper for / of {@link Types}.
 */
public class TypeUtils {

    protected final FrameworkToolWrapper frameworkToolWrapper;

    private TypeUtils(FrameworkToolWrapper frameworkToolWrapper) {
        this.frameworkToolWrapper = frameworkToolWrapper;
    }

    /**
     * Gets a type element for a full qualified class name
     *
     * @param fullQualifiedClassName
     * @return the type element for the passed full qualified class name or null if type element can't be found
     */
    public TypeElement getTypeElementForFullQualifiedClassName(String fullQualifiedClassName) {

        if (fullQualifiedClassName == null) {
            return null;
        }

        return frameworkToolWrapper.getElements().getTypeElement(fullQualifiedClassName);

    }

    /**
     * Gets a TypeMirror for a full qualified class name
     *
     * @param fullQualifiedClassName
     * @return the type mirror for the passed full qualified class name or null if corresponding type element can't be found
     */
    public TypeMirror getTypeMirrorForFullQualifiedClassName(String fullQualifiedClassName) {

        TypeElement typeElement = getTypeElementForFullQualifiedClassName(fullQualifiedClassName);
        return typeElement != null ? typeElement.asType() : null;

    }

    /**
     * Gets {@link TypeElement} for class.
     *
     * @param type the class to get the {@link TypeElement} for
     * @return The {@link TypeElement} that is related with the passed class or null if a TypeElement can't be found for passed class (f.e. if passed type represents an array)
     */
    public TypeElement getTypeElementForClass(Class type) {

        TypeMirror typeMirror = getTypeMirrorForClass(type);
        return typeMirror == null ? null : (TypeElement) frameworkToolWrapper.getTypes().asElement(typeMirror);

    }

    /**
     * Gets {@link TypeMirror} for class.
     *
     * @param type the class to get the {@link TypeMirror} for
     * @return The {@link TypeMirror} that is related with the passed class
     */
    public TypeMirror getTypeMirrorForClass(Class type) {

        if (type == null) {
            return null;
        }

        if (type.isArray()) {
            return frameworkToolWrapper.getTypes().getArrayType(getTypeMirrorForClass(type.getComponentType()));
        }

        return frameworkToolWrapper.getElements().getTypeElement(type.getCanonicalName()).asType();
    }

    /**
     * Checks if passed typeElement is assignable to passed type
     *
     * @param typeElement the type element to check
     * @param type        the class which typeElement must assignable to
     * @return true if typeElement is assignable to type otherwise false.
     */
    public boolean isAssignableToType(TypeElement typeElement, Class type) {
        return isAssignableToTypeMirror(typeElement, frameworkToolWrapper.getElements().getTypeElement(type.getCanonicalName()).asType());
    }

    /**
     * Checks if passed typeElement is assignable to passed typeMirror
     *
     * @param typeElement the type element to check
     * @param typeMirror  the type mirror which typeElement must assignable to
     * @return true if typeElement is assignable to typeMirror otherwise false.
     */
    public boolean isAssignableToTypeMirror(TypeElement typeElement, TypeMirror typeMirror) {
        return typeElement == null || typeMirror == null ? false : frameworkToolWrapper.getTypes().isAssignable(typeElement.asType(), typeMirror);
    }

    /**
     * Checks whether passed first {@link TypeElement} is assignable to passed second {@link TypeElement}.
     *
     * @param typeElement1 the type element to check
     * @param typeElement2 the type element which typeElement1 must be assignable to
     * @return true if typeElement1 is assignable to typeElement2 otherwise false.
     */
    public boolean isAssignableToTypeElement(TypeElement typeElement1, TypeElement typeElement2) {
        return isAssignableToTypeMirror(typeElement1, typeElement2.asType());
    }

    /**
     * Check if passed typeElement matches the passed type.
     *
     * @param typeElement the type element to check
     * @param type        the class which the typeElement must match
     * @return
     */

    public boolean isTypeEqual(TypeElement typeElement, Class type) {
        return isTypeEqual(typeElement, frameworkToolWrapper.getElements().getTypeElement(type.getCanonicalName()).asType());
    }


    public boolean isTypeEqual(TypeElement typeElement, TypeMirror typeMirror) {
        return typeElement == null || typeMirror == null ? false : frameworkToolWrapper.getTypes().isSameType(typeElement.asType(), typeMirror);
    }

    public boolean isTypeEqual(TypeElement typeElement1, TypeElement typeElement2) {
        return isTypeEqual(typeElement1, typeElement2.asType());
    }


    /**
     * Checks whether passed {@link TypeMirror} is a void type or not.
     *
     * @param typeMirror the {@link TypeMirror} to check
     * @return
     */
    public boolean isVoidType(TypeMirror typeMirror) {
        return isOfTypeKind(typeMirror, TypeKind.VOID);
    }

    /**
     * Checks whether passed {@link TypeMirror} is a void type or not.
     *
     * @param typeMirror the {@link TypeMirror} to check
     * @return
     */
    public boolean isArrayType(TypeMirror typeMirror) {
        return isOfTypeKind(typeMirror, TypeKind.ARRAY);
    }

    /**
     * Gets the component type of an array TypeMirror.
     *
     * @param typeMirror
     * @return returns the component TypeMirror of the passed array TypeMirror, returns null if passed TypeMirror isn't an array or null
     */
    public TypeMirror getTypeMirrorArraysComponentType(TypeMirror typeMirror) {
        return typeMirror != null && isArrayType(typeMirror) ? ((ArrayType) typeMirror).getComponentType() : null;
    }

    /**
     * Checks whether passed {@link TypeMirror} is a void type or not.
     *
     * @param typeMirror the {@link TypeMirror} to check
     * @param type       the component type to check for
     * @return true id passed type mirror is of kind array with component type, otherwise false
     */
    public boolean isTypeMirrorAnArrayOfType(TypeMirror typeMirror, Class type) {
        return type != null & isTypeMirrorAnArrayOfType(typeMirror, getTypeMirrorForClass(type));
    }

    /**
     * Checks whether passed {@link TypeMirror} is a void type or not.
     *
     * @param typeMirror             the {@link TypeMirror} to check
     * @param fullQualifiedClassName the component type to check for
     * @return true id passed type mirror is of kind array with component type, otherwise false
     */
    public boolean isTypeMirrorAnArrayOfType(TypeMirror typeMirror, String fullQualifiedClassName) {
        return fullQualifiedClassName != null & isTypeMirrorAnArrayOfType(typeMirror, getTypeMirrorForFullQualifiedClassName(fullQualifiedClassName));
    }

    /**
     * Checks whether passed {@link TypeMirror} is a void type or not.
     *
     * @param typeMirror    the {@link TypeMirror} to check
     * @param componentType the arrays component type to check for
     * @return true id passed type mirror is of kind array with component type, iotherwise false
     */
    public boolean isTypeMirrorAnArrayOfType(TypeMirror typeMirror, TypeMirror componentType) {
        return typeMirror != null && componentType != null && isArrayType(typeMirror) && frameworkToolWrapper.getTypes().isSameType(getTypeMirrorArraysComponentType(typeMirror), componentType);
    }

    /**
     * Checks whether passed {@link TypeMirror} is of passed {@link TypeKind}
     *
     * @param typeMirror
     * @param kind
     * @return
     */
    public boolean isOfTypeKind(TypeMirror typeMirror, TypeKind kind) {
        return typeMirror != null && kind.equals(typeMirror.getKind());
    }

    /**
     * Gets the wrapped {@link Types} instance.
     *
     * @return
     */
    public Types getTypes() {
        return frameworkToolWrapper.getTypes();
    }

    /**
     * Gets an instance of this TypeUtils class.
     *
     * @param frameworkToolWrapper the wrapper instance that provides the {@link javax.annotation.processing.ProcessingEnvironment} tools
     * @return the type utils
     */
    public static TypeUtils getTypeUtils(FrameworkToolWrapper frameworkToolWrapper) {
        return new TypeUtils(frameworkToolWrapper);
    }


}
