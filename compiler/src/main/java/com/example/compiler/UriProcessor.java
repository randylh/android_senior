package com.example.compiler;

import com.example.interfaces.annotation.RouterUri;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(value = {Processor.class})
public class UriProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        System.out.println("----------------UriProcessor init-----------------");
        super.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println("----------------UriProcessor process-----------------");
        if (null == annotations || annotations.isEmpty()) {
            return false;
        }
        CodeBlock.Builder builder =CodeBlock.builder();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(RouterUri.class)) {
            // TODO 判断
        }
        return false;
    }
}
