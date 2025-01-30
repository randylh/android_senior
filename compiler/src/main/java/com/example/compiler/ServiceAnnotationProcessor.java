package com.example.compiler;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

@AutoService(value = {Processor.class})
public class ServiceAnnotationProcessor extends BaseProcessor{


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            // 处理结束，也即最后一轮,生成实体类
            generateInitClass();
        }else {
            processAnnotations(roundEnvironment);
        }
        return true;
    }

    private void processAnnotations(RoundEnvironment env) {

    }

    private void generateInitClass() {

    }
}
