package com.perf.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AsmPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "==============AsmPlugin==============="
        def android = project.extensions.getByType(AppExtension)

        AsmTransform lt = new AsmTransform()
        android.registerTransform(lt)
    }
}