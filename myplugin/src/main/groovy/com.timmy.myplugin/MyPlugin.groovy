package com.timmy.myplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 在插件中使用自定义Transform
 */
public class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        System.out.println("=========MyPlugin 111=========")
        def android = project.extensions.getByType(AppExtension)
        LifeCycleTransform transform = new LifeCycleTransform()
        android.registerTransform(transform)
        System.out.println("=========MyPlugin end=========")
    }
}
