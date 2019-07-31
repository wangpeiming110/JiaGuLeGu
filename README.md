# ApkSign
[ ![Download](https://api.bintray.com/packages/zf/maven/JiaGuLeGu/images/download.svg) ](https://github.com/903600017/JiaGuLeGu/release)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://raw.githubusercontent.com/903600017/JiaGuLeGu/master/LICENSE)




JiaGuLeGu：根据腾讯乐固命令行实现的自动化插件。

JiaGuLeGu 为apk提供快速加固，解放双手，实现自动化流程。解决“腾讯乐固”用命令行加固繁琐，用UI界面操作不能实现自动化的应用场景

### 下载腾讯乐固 jar

[腾讯乐固文档](https://cloud.tencent.com/developer/article/1193406)


### Gradle插件使用方式

#### 配置build.gradle

在位于项目的根目录 `build.gradle` 文件中添加 JiaGuLeGu 插件的依赖， 如下：

```groovy
buildscript {
    dependencies {
        classpath 'com.zf.plugins:JiaGuLeGu:1.0.1'
    }
}
```

并在当前App的 `build.gradle` 文件中apply这个插件

```groovy
apply plugin: 'legu'
```

#### 配置插件(最简易配置)

```groovy
jiaGuLeGuConfig {
    //乐固 jar 包位置
    leGuJarFilePath new File("E:\\XXX\\ms-shield.jar").absolutePath
    //对应用 “腾讯乐固文档” 里的 sid 
    secretId "XXXXXX"
    //对应用 “腾讯乐固文档” 里的 skey  
    secretKey "XXXXXX"
    items {
        hauyi {
            //需要加固的apk
            uploadPath "https://raw.githubusercontent.com/Rakatak/TestingLiquidRakatak/3bef9538000c1e04f7d4c52878413304ad116d15/node_modules/appium/build/settings_apk/settings_apk-debug.apk"
            //加固后的apk输出文件夹
            downloadPath new File("E:\\XXX\\out\\").absolutePath
        }
    }
}
```

#### 插件全部配置
```groovy
jiaGuLeGuConfig {
    //乐固 jar 包位置
    leGuJarFilePath new File("E:\\XXX\\ms-shield.jar").absolutePath
    //对应用 “腾讯乐固文档” 里的 sid 
    secretId 'XXXXXX'
    //对应用 “腾讯乐固文档” 里的 skey  
    secretKey 'XXXXXX'
    
 //统一配置优先级低于 自定义配置--------------start---------------------
 
    //代理地址，可选参数。设置形如https://dev.proxy.oa.com:8080
    proxy 'XXXXXX'
    
    //加固成功后打开文件夹
    isOpenOutputDir true
    
//统一配置--------------end---------------------
    
    items {
        abcRelease {
            //待加固apk文件的路径，必选参数。-uploadType为file时候制定本地待加固apk文件的路径；-uploadType为url时候指定远程apk文件url
            uploadPath new File("E:\\XXX\\out\\debug.apk").absolutePath
            //加固后apk的路径，必选参数。请务必保证路径可写权限
            downloadPath new File("E:\\XXX\\out\\").absolutePath
            
            //待加固文件类型，可选参数。默认是file，其他可选url
            uploadType 'file'
            
            //下载类型，可选参数。默认是file，将自动下载文件，url打印加固后的url
            downloadType 'file'
         
          //自定义配置，优先统一配置--------------start---------------------   
         
            //代理地址，可选参数。设置形如https://dev.proxy.oa.com:8080
            proxy 'XXXXXX'
            
            //加固成功后打开文件夹
            isOpenOutputDir true
            
           //自定义配置，优先统一配置--------------start---------------------   
        }
    }
}
```

**配置参数详细说明**

[腾讯乐固文档](https://cloud.tencent.com/developer/article/1193406)
	
**生成apk签名包：**

`./gradlew apkSign${配置名称(首页字母大小)}  `
 
 如上面的配置，生成签名包需要执行如下命令：
 
 `./gradlew leguAbcRelease`


## Q&A
- [输出乱码](https://github.com/903600017/JiaGuLeGu/wiki/Terminal-%E8%BE%93%E5%87%BA%E4%B9%B1%E7%A0%81)？

## 技术支持

* Read The Fucking Source Code
* 通过提交issue来寻求帮助
* 联系我们寻求帮助.(QQ群：366399995)

## 贡献代码
* 欢迎提交issue
* 欢迎提交PR


## License

    Copyright 2017 903600017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
