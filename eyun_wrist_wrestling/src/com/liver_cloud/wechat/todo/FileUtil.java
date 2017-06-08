package com.liver_cloud.wechat.todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileUtil {
    /**
     * 读取文件内容（使用UTF-8编码）
     *
     * @param filePath
     *          输出文件路径
     * @return
     * @throws Exception
     */
    public static synchronized String readFileUTF8(String filePath) {
        String fileContent = "";
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

                String temp = "";
                while ((temp = br.readLine()) != null) {
                    fileContent = fileContent + temp;
                }
                br.close();
                fis.close();
            } else {
                System.out.println("文件" + filePath + "不存在");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
    
    /**
     * 将字符串写到文件内
     *
     * @param outputPath
     *          输出文件路径
     * @param msg
     *          字符串
     * @param isApend
     *          是否追加
     * @throws IOException
     */
    public static synchronized void writeContent(String msg, String outputPath, boolean isApend) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, isApend));
            bw.write(msg);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** 如果不存在就创建 */
    public static boolean createIfNoExists(String path) {
        File file = new File(path);
        boolean mk = false;
        if (!file.exists()) {
            mk = file.mkdirs();
        }
        return mk;
    }
}
