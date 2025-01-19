package dev.pavatus.lib.util;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.pavatus.lib.container.RegistryContainer;

public class ReflectionUtil {
    // why god why
    private static <T, R extends RegistryContainer<T>, A extends Annotation> HashMap<T, A> getAnnotatedValues(Class<R> parent, Class<T> value, Class<A> annotationClass, boolean inverse, A defaultValue) {
        return Stream.of(parent.getDeclaredFields())
                .filter(field -> inverse != field.isAnnotationPresent(annotationClass))
                .filter(field -> value.isAssignableFrom(field.getType())) // Ensure it's a T
                .map(field -> {
                    try {
                        if (inverse) {
                            return new AbstractMap.SimpleEntry<>((T) field.get(null), defaultValue);
                        }

                        A annotation = field.getAnnotation(annotationClass);
                        return new AbstractMap.SimpleEntry<>((T) field.get(null), annotation);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, HashMap::new));
    }

    public static <T, R extends RegistryContainer<T>, A extends Annotation> HashMap<T, A> getAnnotatedValues(Class<R> parent, Class<T> value, Class<A> annotationClass, boolean inverse) {
        return getAnnotatedValues(parent, value, annotationClass, inverse, null);
    }
}