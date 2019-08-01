package com.zf.plugins.JiaGuLeGu

import groovy.text.SimpleTemplateEngine
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class JiaGuLeGuTask extends JiaGuLeGuBaseTask {


    @Input
    JiaGuLeGuInfo jiaGuLeGuInfo;

    void initGlobalJiGuConfig(JiaGuLeGuConfig config) {
        if (config.secretId == null) {
            throw new GradleException("secretId cannot be empty")
        }

        if (config.secretKey == null) {
            throw new GradleException("secretKey cannot be empty")
        }
    }


    void initJiaGuConfig(JiaGuLeGuConfig config) {

        if (jiaGuLeGuInfo.secretId == null) {
            jiaGuLeGuInfo.secretId = config.secretId
        }

        if (jiaGuLeGuInfo.secretKey == null) {
            jiaGuLeGuInfo.secretKey = config.secretKey
        }

        if (jiaGuLeGuInfo.proxy == null) {
            jiaGuLeGuInfo.proxy = config.proxy
        }

        if (jiaGuLeGuInfo.uploadType == null) {
            throw new GradleException("uploadType cannot be empty")
        }

        if (jiaGuLeGuInfo.isOpenOutputDir == null) {
            jiaGuLeGuInfo.isOpenOutputDir = config.isOpenOutputDir;
        }


        def uploadType = jiaGuLeGuInfo.uploadType.trim()
        if (uploadType != JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE && uploadType != JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_URL) {
            throw new GradleException("uploadType must be a file or url character")
        }


        if (!jiaGuLeGuInfo.uploadPath) {
            throw new GradleException("Path to be augmented apk file, mandatory parameter")
        }


        if (uploadType == JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE) {
            File inputApkFile = new File(jiaGuLeGuInfo.uploadPath)
            if (!inputApkFile.exists()) {
                throw new GradleException("When uploadType is url, uploadPath must be the file path.\n ${jiaGuLeGuInfo.uploadPath}")
            }

            if (!inputApkFile.isFile()) {
                throw new GradleException("Pending reinforcement apk does not exist.\n ${jiaGuLeGuInfo.uploadPath}")
            }
        } else {
            if (!(jiaGuLeGuInfo.uploadPath.startsWith("http") || jiaGuLeGuInfo.uploadPath.startsWith("http"))) {
                throw new GradleException("When uploadType is file, uploadPath must be url.\n ${jiaGuLeGuInfo.uploadPath}")
            }
        }

        def downloadType = jiaGuLeGuInfo.downloadType.trim()
        if (downloadType != JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE && downloadType != JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_URL) {
            throw new GradleException("downloadType must be a file or url character")
        }

        if (jiaGuLeGuInfo.downloadPath == null) {
            throw new GradleException("The path of APK after reinforcement, the necessary parameters")
        } else {
            File outputApkDirFile = new File(jiaGuLeGuInfo.downloadPath)
            if (!outputApkDirFile.exists()) {
                throw new GradleException("The downloadPath folder must exist.downloadPath=${outputApkDirFile.absolutePath}")
            }
        }
    }


    @TaskAction
    public void run() throws Exception {
        JiaGuLeGuConfig config = JiaGuLeGuConfig.getConfig(project);
        initJiaGuJarConfig(config)
        initGlobalJiGuConfig(config)
        initJiaGuConfig(config)
        initJiaGuConfig(config)

        JiaGuLeGu jiaGuLeGu = new JiaGuLeGu(new File(config.leGuJarFilePath), jiaGuLeGuInfo, project.logger)
        jiaGuLeGu.jiaGu()

    }
}
