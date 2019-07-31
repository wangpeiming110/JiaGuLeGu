package com.zf.plugins.JiaGuLeGu

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class JiaGuLeGuConfig {

    public final static String UPLOAD_DOWNLOAD_TYPE_FILE = 'file'
    public final static String UPLOAD_DOWNLOAD_TYPE_URL = 'url'

    String leGuJarFilePath

    String secretId
    String secretKey
    String proxy
    boolean isOpenOutputDir

    NamedDomainObjectContainer<JiaGuLeGuInfo> items

    public JiaGuLeGuConfig(Project project) {
        NamedDomainObjectContainer<JiaGuLeGuInfo> itemsContainer = project.container(JiaGuLeGuInfo)
        items = itemsContainer
    }

    void secretId(String secretId) {
        this.secretId = secretId
    }

    void secretKey(String secretKey) {
        this.secretKey = secretKey
    }

    void proxy(String proxy) {
        this.proxy = proxy
    }

    void isOpenOutputDir(String isOpenOutputDir) {
        this.isOpenOutputDir = isOpenOutputDir
    }

    void leGuJarFilePath(String leGuJarFilePath) {
        this.leGuJarFilePath = leGuJarFilePath
    }

    void items(Action<NamedDomainObjectContainer<JiaGuLeGuInfo>> action) {
        action.execute(items)
    }


    public static JiaGuLeGuConfig getConfig(Project project) {
        JiaGuLeGuConfig config =
                project.getExtensions().findByType(JiaGuLeGuConfig.class);
        if (config == null) {
            config = new JiaGuLeGuConfig();
        }
        return config;
    }


}
