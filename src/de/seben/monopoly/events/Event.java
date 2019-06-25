package de.seben.monopoly.events;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {
    int priority() default 50;
}