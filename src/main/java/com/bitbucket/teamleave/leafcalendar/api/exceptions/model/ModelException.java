package com.bitbucket.teamleave.leafcalendar.api.exceptions.model;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"AbstractClassExtendsConcreteClass", "AbstractClassWithoutAbstractMethods"})
public abstract class ModelException extends Exception {
    private final String invalidField;
    private final String invalidValue;

    @SuppressWarnings("MagicCharacter")
    ModelException(final String field, final String value) {
        super('"' + field + '"' + ": " + '"' + value + '"');
        this.invalidField = field;
        this.invalidValue = value;
    }

    public String getInvalidField() {
        return invalidField;
    }

    public String getInvalidValue() {
        return invalidValue;
    }
}