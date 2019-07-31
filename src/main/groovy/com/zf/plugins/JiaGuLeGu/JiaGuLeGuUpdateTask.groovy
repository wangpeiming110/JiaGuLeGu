package com.zf.plugins.JiaGuLeGu


import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class JiaGuLeGuUpdateTask extends JiaGuLeGuBaseTask {


    @TaskAction
    public void run() throws Exception {

        JiaGuLeGuConfig config = JiaGuLeGuConfig.getConfig(project)
        initJiaGuJarConfig(config)
        JiaGuLeGu jiaGuLeGu = new JiaGuLeGu(new File(config.leGuJarFilePath), project.logger)
        jiaGuLeGu.update()
    }
}
