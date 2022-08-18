package com.sk.project8.annotation

import com.google.auto.service.AutoService
import java.io.PrintWriter
import java.util.stream.Collectors
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.JavaFileObject

@SupportedAnnotationTypes("com.sk.project8.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor::class)
class BuilderProcessor: AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        annotations.takeIf { it?.isNotEmpty() == true }?.forEach { typeElement ->
            var error = false
            roundEnv?.getElementsAnnotatedWith(typeElement)
                ?.filter { element -> element.kind == ElementKind.METHOD }
                ?.forEach { element ->
                    val annotation = element.annotationMirrors.getAnnotation(typeElement)
                    val priority = annotation.getPriority()
                    val simpleName = element.simpleName
                    val qualifiedName = element.enclosingElement.simpleName

                    when{
                        priority == 0 -> {
                            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "Need to change priority", element)
                        }
                        priority > 0 -> {
                            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Good priority", element)
                        }
                        priority < 0 -> {
                            error = true
                            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Priority cannot be negative", element)
                        }
                    }
                }
            if(!error) {
                val methods = roundEnv?.getElementsAnnotatedWith(typeElement)
                    ?.filter { element -> element.kind == ElementKind.METHOD }
                    ?.map { it.simpleName.toString() }
                    ?.stream()?.collect(Collectors.toList())
                val className = roundEnv?.getElementsAnnotatedWith(typeElement)
                    ?.filter { element -> element.kind == ElementKind.METHOD }
                    ?.map { it.enclosingElement.simpleName.toString() }
                    ?.first()
                //generateStubClass(methods, className)
            }
        }
        return true
    }

    private fun List<AnnotationMirror>.getAnnotation(typeElement: TypeElement): AnnotationMirror {
        return first { typeElement.toString() == it.annotationType.asElement().toString() }
    }

    private fun AnnotationMirror.getPriority(): Int {
        return elementValues.mapValues { it.value.value as Int }.values.first()
    }

    private fun generateStubClass(methods: List<String>?, className: String?) {
        var builderFile: JavaFileObject = processingEnv.filer.createSourceFile("${className}Builder")
        val body = StringBuilder()
        methods?.forEach {
            body.append("public ${className}Builder $it() {\n")
            body.append("object.${it}(); \n")
            body.append("return this; \n")
            body.append("} \n")
        }
        PrintWriter(builderFile.openWriter()).use { pw ->
            pw.print("""
                package com.sk.project8.builder;
                
                import com.sk.project8.model.$className;
                
                class ${className}Builder {
                    private $className object = ${className}();

                    $body
                    public $className build() {
                        return object;
                    }
                }
            """.trimIndent())
        }
    }
}