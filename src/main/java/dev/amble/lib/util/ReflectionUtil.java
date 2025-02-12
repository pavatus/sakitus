package dev.amble.lib.util;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.amble.lib.container.RegistryContainer;

public class ReflectionUtil {
    // why god why
    public static <T, R extends RegistryContainer<T>, A extends Annotation> HashMap<T, Optional<A>> getAnnotatedValues(Class<R> parent, Class<T> value, Class<A> annotationClass, boolean inverse) {
        return Stream.of(parent.getDeclaredFields())
                .filter(field -> inverse != field.isAnnotationPresent(annotationClass))
                .filter(field -> value.isAssignableFrom(field.getType())) // Ensure it's a T
                .map(field -> {
                    try {
                        return new AbstractMap.SimpleEntry<>((T) field.get(null), inverse ? Optional.<A>empty() : Optional.ofNullable(field.getAnnotation(annotationClass)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(entry -> entry != null && entry.getKey() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        HashMap::new
                ));
    }
}