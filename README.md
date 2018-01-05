### 流程
1. 通过ADB截屏；
2. 通过ADB将截屏保存到电脑；
3. 识别玩家位置；
4. 识别目标方块位置；
5. 识别目标方块中心圆点的位置；
6. 如果第5步成功，则取第5步的中心点为下一步的位置；否则，取第4步的中心点为下一步的位置；
7. 计算玩家位置与下一步的位置，乘以一定的系数，得到长按的时间；
8. 通过ADB，触发长按；
	
## 三. 运行教程

1. 准备Java运行与编译环境，使用Java8以上，IDE推荐使用eclipse或Intellij；
2. 安装Android SDK；
3. 使用 git工具clone项目，地址为https://github.com/szhjw/weChat.git；
4. 使用IDE（笔者使用eclipse）import该项目；
5. 准备好一部已经打开开发者模式的Android手机；
6. 请确认是否adb已经联接上你的手机；如果adb连接失败，则会导致截图与拉取截图失败，提示“find myPos, fail”；如果连着多个Android设备的话，最好关到只有一个；
7. 打开开发者选项，找到“USB调试（安全设置）允许通过USB调试修改权限或者模拟点击”（在mui上是这样的，在其他手机上，应该也是差不多这样）这个开关，打开它；如果这个权限没有授予，则不能正常触发弹跳；
8. 修改com.wxkj.tyt.demo.ProgramStart中COMMAND，将其改为你自己的ADB位置；
9. 打开微信，打开跳一跳游戏，并点击开始；
10. 运行程序（ProgramStart.java中的main方法）吧，骚年，观察它自动跳动；


工具介绍

1.安装java环境，jdk1.8
2.安装Adb 驱动
3.开发工具：eclipse或Intellij
4.一部Android手机
5.下载源码：https://github.com/szhjw/weChat.git(源码基于chenliang进行修改)

