package com.me.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {
    /**
     * 生成图片验证码上的字符
     */
    public static String generateRandomText(int length){
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     *生成验证码图片
     */
    public static BufferedImage generateCaptchaImage(String text, int width, int height){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        // 背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));
        }

        // 绘制验证码文本
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < text.length(); i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(String.valueOf(text.charAt(i)), 20 * i + 10, 25);
        }
        g.dispose();
        return image;
    }

    /**
     *
     * @param length 数字验证码长度
     * @return
     */
    public static String generateRandomCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度不合法");
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
