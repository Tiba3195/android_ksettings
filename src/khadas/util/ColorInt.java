package com.khadas.util;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Denotes that the annotated element represents a packed color
 * int, {@code AARRGGBB}. If applied to an int array, every element
 * in the array represents a color integer.
 * <p>
 * Example:
 * <pre>{@code
 *  public abstract void setTextColor(@ColorInt int color);
 * }</pre>
 *
 * @hide
 */
@Retention(SOURCE)
@Target({PARAMETER,METHOD,LOCAL_VARIABLE,FIELD})
public @interface ColorInt {
}