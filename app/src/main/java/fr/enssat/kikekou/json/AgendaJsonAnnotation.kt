package fr.enssat.kikekou.json

@Target(AnnotationTarget.TYPE,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class AgendaJsonAnnotation
