package com.zf.plugins.JiaGuLeGu;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;

import java.io.File;

public class JiaGuLeGuBaseTask extends DefaultTask {


    void initJiaGuJarConfig(JiaGuLeGuConfig config) {

        if (!config.leGuJarFilePath) {
            throw new GradleException("The hardened jar file path cannot be empty.")
        }

        File leGuJarFile = new File(config.leGuJarFilePath)
        if (!leGuJarFile.exists()) {
            throw new GradleException("The hardened jar file does not exist.\n${jiaGuLeGuInfo.absolutePath}")
        }
    }

}
