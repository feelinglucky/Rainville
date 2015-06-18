# 聆雨 for Android

![Screen Shots](http://files.gracecode.com/2014_05_29/1401368232.png)

有时候有没有觉得您想专注下，但是却被周边的噪音打扰？或者您想休息下，却在飞机或者其他嘈杂的环境中无法入睡？那么，或许您可以试试这款应用！

雨声是大自然最自然的声音，大脑听到雨声并不会认为这是噪音，甚至听雨声对于很多人来讲能够得到充分的放松。这款应用能够巧妙得合成各种雨的声音，利用声音中和的原理隔绝外界的噪音，使您的大脑能够专注、安静下来。

## 下载

- [小米市场](http://app.xiaomi.com/detail/63028)
- [魅族市场](http://app.meizu.com/phone/apps/e6adf49e7f484fe18c6510e79cbbc9a8)
- [Google Play](https://play.google.com/store/apps/details?id=com.gracecode.android.rain)


## 使用场景

* 隔绝，中和噪音对大脑的影响
* 飞机、火车等嘈杂的环境中
* 午休
* 哄孩子入睡
* 放松或者需要专注工作
* 其他您认为所有值得使用此应用的情况 :^)


## 特性

- 优雅简洁的界面，没有多余的元素干扰
- 独立音频解码器，省电省内存适合长期使用
- 支持多达十几种合成雨声（后续仍在增加）
- 针对小米以及魅族进行了针对性的优化
- 完全免费而且是开源的！


### 感谢

感谢许多帮助开发和测试朋友，没有他们或许我就没法坚持到这个项目上线（对于我而言除了技术以外，似乎我很难在其他领域擅长什么）。

- 感谢 [Stephane Pigeon](https://twitter.com/audiosampling) 提供音频文件，并允许将其打包进「聆雨」应用中
- 使用 'Musket2' 字体，我爱它 http://bybu.es/portfolio/musket/
- 天气图标使用 https://erikflowers.github.io/weather-icons/
- <del>使用 libvorbis 单独作为音频解码模块 https://github.com/vincentjames501/libvorbis-libogg-android</del> （ 1.7.0 以后使用系统原生的解码模块）。


## 更新记录

### Release 1.8.0(2015-06-18)

- 不再需要插入耳机才能播放
- 修复 Android 5.0 平台无法播放的问题
- 音效细节调整
- 其他小的 Bugfix

### Release 1.7.2(2015-01-06)

- 修复面板打开遮挡选择雨声类型面板的错误
- 增加图标改动的小细节，会根据当地天气切换图标

### Release 1.7.0(2015-01-04)

- 调整音效，完成无缝播放
- 调整类库播放模块，使用系统原生更加省电
- 部分文案和细节调整

### Release 1.6.0(2014-06-26)

- 调整音效
- 修复部分机型下定时调整弹出键盘的问题
- 已知的错误修复

### Release 1.5.9(2014-06-17)

- 修复通知无法取消的错误
- 调整去除部分无效的代码

### Release 1.5.7(2014-06-14)

- 调整音效，去除蝉鸣声
- 调整播放间隔，避免重复

### Release 1.5.1(2014-06-12)

- 调整音效，减少水流声附加蟋蟀以及鸟声
- 优化线程以及内存占用

### Release 1.5.1(2014-06-06)

- 新增新手引导提示，避免错过部分功能
- 修复播放按钮状态无法更新的问题

### Release 1.5.0(2014-06-03)

- 增加定时关闭功能
- 增加手机扬声器播放选项（连续按红色耳机提示打开）
- 修复部分可能存在的问题

@TODO MX2 用户反应播放时黑屏存在异常关闭的情况，如果您的是同类型手机请您不吝反馈。

### Release 1.4.9(2014-05-30)

- 修复支持蓝牙耳机以后造成的判断问题
- 增加个彩蛋：连续点击耳机图标解锁直接用手机喇叭播放
- 部分界面文案整理

### Release 1.4.8(2014-05-29)

- 蓝牙耳机支持
- 修复界面的部分文案错误

### Release 1.4.0(2014-05-20)

- 增加中文界面，应用定名为「聆雨」
- 修复若干已知的 Bug

### Release 1.3.0(2014-04-17)

- Optimized for Meizu's Smartbar

### Release 1.1.0(2013-10-27)

- Add App Widget, So you can start/stop play without open app;
- Minor modified some ui and bugs;

### Release 1.0.1(2013-10-22)

- Ready for Google Play Market.

### Release 1.0.0(2013-10-21)

- Initial release.


## 源代码和编译

Rainville 是开源应用，因此您可以很容易的获得它的源代码（注：音频是有版权的，具体
参见文章内文）。

首先，您可以从这里下载最新的源码：

    https://github.com/feelinglucky/Rainville

编译需要 Gradle 以及对应的类库，您需要联网获取。在此之前，先更新子项目以及对应的
代码：

    git submodule init && git submodule update

然后就可以编译了

    gradle clean && gradle build


最后，apk 文件包以及相关编译的信息留在 build 目录中。

祝使用愉快 :^)



## 点杯咖啡给我？

写应用其实是件「费力不讨好」的事情，需要花费更多看不见的精力和时间。虽然个人产出的应用可能毫无用处甚至脏乱不堪，但本人还是「恬不知耻」的希望的到大家的帮助和鼓励。

我的支付宝帐号是 ```amdk6@yeah.net``` ，不要求数额只要求心意到了即可。如您同意，您的名字将出现在应用的捐赠列表中，功德无量。

计划这些费将用于[维持服务器](https://mos.meituan.com/r/71f9f5e918)等实际的开支，以及可能的话购买相应的硬件。

```-- EOF --```

