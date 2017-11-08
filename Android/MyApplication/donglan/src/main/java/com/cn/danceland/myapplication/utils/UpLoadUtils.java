package com.cn.danceland.myapplication.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by feng on 2017/10/25.
 */

public class UpLoadUtils {
    static DataOutputStream outStream;
    public static String postUpLoadFile(String url, Map<String, String> params, Map<String, File> files)
            throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary=" + BOUNDARY);
        conn.setRequestProperty("Authorization","Bearer+eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzQzNjkwNzUzNSIsImNyZWF0ZWQiOjE1MTAwMzU1MzMyMDAsImV4cCI6MTUxMDY0MDMzM30.Ex6PoP3NqZfiDzMRFiiNgY5J55N9S8GFoveHkZug0J_9qS92Si3KYk4xbb3_rvEqLzgSsM5HjYvH3fLt-4jPqw\n");
        // 首先组拼文本类型的参数
//        if(params!=null){
//            StringBuilder sb = new StringBuilder();
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINEND);
//                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
//                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
//                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//                sb.append(LINEND);
//                sb.append(entry.getValue());
//                sb.append(LINEND);
//            }
//
//            outStream = new DataOutputStream(conn.getOutputStream());
//            outStream.write(sb.toString().getBytes());
//
//        }

        outStream = new DataOutputStream(conn.getOutputStream());

        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
//                sb1.append(PREFIX);
//                sb1.append(BOUNDARY);
//                sb1.append(LINEND);
//                sb1.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
//                        + file.getValue().getName() + "\"" + LINEND);
//                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
//                sb1.append(LINEND);
                //outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                //outStream.write(LINEND.getBytes());
            }
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        //outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        LogUtil.e("zzf",res+"");

        StringBuffer sb = new StringBuffer();
        // 打开输入流 , 读取服务器返回的数据
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        String line;
        // 一行一行的读取服务器返回的数据
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        // 关闭输入流
        reader.close();




//        InputStream in = conn.getInputStream();
//        StringBuilder sb2 = new StringBuilder();
//        if (res == 200) {
//            int ch;
//            while ((ch = in.read()) != -1) {
//                sb2.append((char) ch);
//            }
//        }
//        outStream.close();
//        conn.disconnect();
        String str = sb.toString();
        return str;
    }
}