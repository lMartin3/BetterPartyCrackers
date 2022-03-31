package dev.martinl.betterpartycrackers.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeEnumAsString {
    Class<? extends Enum> enumType();
}
