package com.zf.plugins.JiaGuLeGu


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class JiaGuLeGuPlugin implements Plugin<Project> {

    public static final String sPluginExtensionName = "jiaGuLeGuConfig";

    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", null);
        }

        project.extensions.create(sPluginExtensionName, JiaGuLeGuConfig, project);

        createJiaGuTask(project)
        createJiaGuShowVersionTask(project)
        createJiaGuUpdateTask(project)
    }


    def createJiaGuTask(Project project) {

        project[sPluginExtensionName].items.all { _item ->
            project.tasks.create("legu${_item.name.capitalize()}", JiaGuLeGuTask) {
                description "乐固加固"
                group "JiaGu_LeGu"
                jiaGuLeGuInfo _item
            }
        }
    }


    def createJiaGuShowVersionTask(Project project) {
        String _leGuJarFilePath = project[sPluginExtensionName].leGuJarFilePath
        project.tasks.create('leguShowVersion', JiaGuLeGuShowVersionTask) {
            description "查看当前乐固版本"
            group "JiaGu_LeGu"
        }
    }

    def createJiaGuUpdateTask(Project project) {
        String _leGuJarFilePath = project[sPluginExtensionName].leGuJarFilePath
        project.tasks.create('leguUpdate', JiaGuLeGuUpdateTask) {
            description "升级乐固"
            group "JiaGu_LeGu"
        }
    }

}

