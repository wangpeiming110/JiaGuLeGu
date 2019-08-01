package com.zf.plugins.JiaGuLeGu


import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

import java.text.DecimalFormat

public class JiaGuLeGu {

    Logger logger
    public File jiaGuJarDir
    public String jiaGuJarName
    JiaGuLeGuInfo jiaGuLeGuInfo

    DecimalFormat df = new DecimalFormat("0.00");

    JiaGuLeGu(File jiaGuJarFile, Logger logger) {
        this.jiaGuJarDir = jiaGuJarFile.getParentFile()
        this.jiaGuJarName = jiaGuJarFile.getName()
        this.logger = logger
    }


    JiaGuLeGu(File jiaGuJarFile, JiaGuLeGuInfo jiaGuLeGuInfo, Logger logger) {
        this.jiaGuJarDir = jiaGuJarFile.getParentFile()
        this.jiaGuJarName = jiaGuJarFile.getName()
        this.jiaGuLeGuInfo = jiaGuLeGuInfo;
        this.logger = logger;
    }

    void jiaGu() {

        String md5 = null;

        if (jiaGuLeGuInfo.uploadType == JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_URL) {

            File apkFile = Utils.downLoad(jiaGuLeGuInfo.proxy, jiaGuLeGuInfo.uploadPath, new File(jiaGuLeGuInfo.downloadPath), { def size ->
                logger.quiet("output > 已下下载大小 ${df.format(size / 1024 / 8)}MB")
            })

            if (!apkFile.exists()) {
                throw new RuntimeException("apk 文件下载失败")
            }

            md5 = Utils.getMD5(apkFile);
            apkFile.delete()
        }

        jiaGuInternal(jiaGuLeGuInfo.secretId,
                jiaGuLeGuInfo.secretKey,
                jiaGuLeGuInfo.proxy,
                jiaGuLeGuInfo.uploadPath,
                jiaGuLeGuInfo.downloadPath,
                jiaGuLeGuInfo.uploadType,
                jiaGuLeGuInfo.downloadType,
                md5);

        openDir(jiaGuLeGuInfo.downloadType)

    }


    private void jiaGuInternal(String secretId, String secretKey, String proxy, String inputApkFile, String outputApkDirPath, String uploadType, String downloadType, String md5) {

        StringBuilder cmdBuilder = new StringBuilder("java -Dfile.encoding=utf-8 -jar ${jiaGuJarName} -sid ${secretId} -skey ${secretKey}  -uploadPath ${inputApkFile} -downloadPath ${outputApkDirPath}")

        if (uploadType != JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE) {
            cmdBuilder.append(" -uploadType ${uploadType}")
        }

        if (downloadType != JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE) {
            cmdBuilder.append(" -downloadType ${downloadType}")
        }

        if (md5) {
            cmdBuilder.append(" -uploadMd5 ${md5}")
        }

        if (proxy) {
            cmdBuilder.append(" -proxy ${proxy}")
        }
        exec(cmdBuilder.toString(), "utf-8", { String commandLine ->
            logger.quiet(commandLine.replaceAll(jiaGuLeGuInfo.secretId, "XXXXXX").replaceAll(jiaGuLeGuInfo.secretKey, "XXXXXX"))
        })
    }

    void showVersion() {
        String cmd = "java -Dfile.encoding=utf-8 -jar ${jiaGuJarName} -v";
        exec(cmd)
    }

    void update() {
        String cmd = "java -Dfile.encoding=utf-8 -jar ${jiaGuJarName} -update";
        exec(cmd)
    }

    void openDir(String path) {

        if (!jiaGuLeGuInfo.isOpenOutputDir) {
            logger.info("Do not open the output folder.")
            return
        }

        if (!isWindowSystem()) {
            logger.warn("Opening folder operation only supports windows.")
            return
        }

        if (jiaGuLeGuInfo.downloadType == JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_URL) {
            logger.warn("The downloadType value URL does not support opening the output folder.")
            return
        }

        String cmd = "explorer.exe ${path}"
        exec(cmd)
    }

    String[] getCmd(String cmd) {
        String[] cmdArr = ["/bin/sh", "-c", cmd]
        if (isWindowSystem()) {
            cmdArr[0] = "cmd"
            cmdArr[1] = "/C"
        }

        return cmdArr
    }

    void exec(cmd) {
        exec(cmd, "utf-8", null)
    }

    void exec(cmd, String charset, Closure cmdClosure) {

        String[] cmdArr = getCmd(cmd);

        if (cmdClosure == null) {
            this.logger.quiet(cmdArr[2].toString())
        } else {
            cmdClosure(cmdArr[2])
        }

        Process process = Runtime.runtime.exec(cmdArr, null, this.jiaGuJarDir)
        StreamConsumer infoStream = new StreamConsumer(process.inputStream, 'output', charset, { type, text ->
            if (!((text ==~ /\s?/) || (text ==~ /^#.*#$/))) {
                this.logger.quiet("${type} > ${text}")
            }

        })
        infoStream.start()
        process.waitFor()

        def errText = process.err.getText(charset);

        if (errText != null && errText.trim().length() > 0) {
            throw new GradleException(errText)
        }
    }


    static boolean isWindowSystem() {
        return System.getProperty("file.separator") == "\\";
    }

}
