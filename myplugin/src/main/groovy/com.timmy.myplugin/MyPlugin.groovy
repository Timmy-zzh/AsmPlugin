package com.timmy.myplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        System.out.println("=========MyPlugin1=========")
        System.out.println("===========2===========")
        System.out.println("=========MyPlugin3=========")
    }
}
