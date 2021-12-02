package com.swagger.api.controller.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * String Builder Builder
 */
public class SBB {

    private final StringBuilder strBuilder = new StringBuilder();

    public static SBB sbb() {
        return new SBB();
    }

    public static SBB sbb(Object basePlainText) {
        return sbb().append(basePlainText);
    }

    private SBB addToBuild(Object targetPlainText) {
        this.strBuilder.append(targetPlainText);
        return this;
    }

    /**
     * Append plain text
     */
    public SBB append(Object targetPlainText) {
        return this.addToBuild(targetPlainText);
    }

    /**
     * Append plain text
     */
    public SBB append(Field[] targetPlainText) {
        var collect = Arrays.stream(targetPlainText)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> sbb()
                        .dQuote(field.getName()).append(':').w()
                        .dQuote(field.getType().getSimpleName())
                        .bld())
                .collect(Collectors.toList());

        return this.append(collect);
    }

    /**
     * Add new line caret
     */
    public SBB n() {
        return this.append("\n");
    }

    /**
     * Add new tab
     */
    public SBB t() {
        return this.append("\t");
    }

    /**
     * Add new whitespace
     */
    public SBB w() {
        return this.append(" ");
    }

    /**
     * Add a coma
     */
    public SBB coma() {
        return this.append(',');
    }

    /**
     * append as single quoted value
     *
     * @return 'targetPlainText'
     */
    public SBB sQuoted(Object targetPlainText) {
        return this.append("'").append(targetPlainText).append("'");
    }

    /**
     * append as double quoted value
     *
     * @return "targetPlainText"
     */
    public SBB dQuote(Object targetPlainText) {
        return this.append('\"').append(targetPlainText).append('\"');
    }

    /**
     * append as double quoted value
     *
     * @return "targetPlainText"
     */
    public SBB arrayBrackets(Object targetPlainText) {
        return this.append('[').append(targetPlainText).append(']');
    }

    public SBB slash() {
        return this.append('/');
    }

    public String build() {
        return this.strBuilder.toString();
    }

    public String bld() {
        return build();
    }

    @Override
    public String toString() {
        return bld();
    }

}
