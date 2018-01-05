package com.wxkj.tyt.demo;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 启动类
 * @author hjw
 *
 */
public class ProgramStart {
	//adb
   private static final String COMMAND = "adb";
   //弹跳系数
   private static final double PRESSURE = 1.385f;
   
   private static final double PIXEL=1080;
	
	public static void main(String[] args) {
		
        String root = ProgramStart.class.getResource("/").getPath();
        System.out.println("root: " + root);
        File srcDir = new File(root, "imgs/input");
        srcDir.mkdirs();
        System.out.println("srcDir: " + srcDir.getAbsolutePath());
        MyPosFinder myPosFinder = new MyPosFinder();
        NextCenterFinder nextCenterFinder = new NextCenterFinder();
        WhitePointFinder whitePointFinder = new WhitePointFinder();
        int total = 0;
        int centerHit = 0;
        double jumpRatio = 0;
        for (int i = 0; i < 5000; i++) {
            try {
                total++;
                File file = new File(srcDir, i + ".png");
                if (file.exists()) {
                    file.deleteOnExit();
                }
                Runtime.getRuntime().exec(COMMAND + " shell /system/bin/screencap -p /sdcard/screenshot.png");
                Thread.sleep(1_000);
                Runtime.getRuntime().exec(COMMAND + " pull /sdcard/screenshot.png " + file.getAbsolutePath());
                Thread.sleep(1_000);

                System.out.println("screenshot, file: " + file.getAbsolutePath());
                BufferedImage image = ImgLoader.load(file.getAbsolutePath());
                if (jumpRatio == 0) {
                    jumpRatio = PRESSURE * PIXEL / image.getWidth();
                }
                int[] myPos = myPosFinder.find(image);
                if (myPos != null) {
                    System.out.println("find myPos, succ, (" + myPos[0] + ", " + myPos[1] + ")");
                    int[] nextCenter = nextCenterFinder.find(image, myPos);
                    if (nextCenter == null || nextCenter[0] == 0) {
                        System.err.println("find nextCenter, fail");
                        break;
                    } else {
                        int centerX, centerY;
                        int[] whitePoint = whitePointFinder.find(image, nextCenter[0] - 120, nextCenter[1], nextCenter[0] + 120, nextCenter[1] + 180);
                        if (whitePoint != null) {
                            centerX = whitePoint[0];
                            centerY = whitePoint[1];
                            centerHit++;
                            System.out.println("find whitePoint, succ, (" + centerX + ", " + centerY + "), centerHit: " + centerHit+ ", total: " + total);
                        } else {
                            if (nextCenter[2] != Integer.MAX_VALUE && nextCenter[4] != Integer.MIN_VALUE) {
                                centerX = (nextCenter[2] + nextCenter[4]) / 2;
                                centerY = (nextCenter[3] + nextCenter[5]) / 2;
                            } else {
                                centerX = nextCenter[0];
                                centerY = nextCenter[1] + 48;
                            }
                        }
                        System.out.println("find nextCenter, succ, (" + centerX + ", " + centerY + ")");
                        int distance = (int) (Math.sqrt((centerX - myPos[0]) * (centerX - myPos[0]) + (centerY - myPos[1]) * (centerY - myPos[1])) * jumpRatio);
                        System.out.println("distance: " + distance);
                        System.out.println(COMMAND + " shell input swipe 400 400 400 400 " + distance);
                        Runtime.getRuntime().exec(COMMAND + " shell input swipe 300 300 400 400 " + distance);
                    }
                } else {
                    System.err.println("find myPos, fail");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            try {
                Thread.sleep(4_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("centerHit: " + centerHit + ", total: " + total);
	}

}
