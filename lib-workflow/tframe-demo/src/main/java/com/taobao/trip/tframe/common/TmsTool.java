package com.taobao.trip.tframe.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TmsTool {
    private static final String CMS_HOME = "/home/admin/tms";

    public static String importRgn(String url, long siteId) {
        if (url.indexOf("..") != -1) {
            return "";
        }
        String filePath = CMS_HOME;
        if (siteId != 0) {
            filePath = filePath + "/" + siteId + url;
        } else {
            filePath = filePath + url;
        }

        File f = new File(filePath);
        StringBuffer resultBuffer = new StringBuffer("");
        FileInputStream fi = null;
        InputStreamReader ireader = null;
        BufferedReader reader = null;
        try {
            fi = new FileInputStream(f);
            if (fi != null) {
                ireader = new InputStreamReader(fi,"GBK");
                if (ireader != null) {
                    String line;
                    reader = new BufferedReader(ireader);
                    while ((line = reader.readLine()) != null) {
                        resultBuffer.append(line);

                        resultBuffer.append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (ireader != null) {
                    ireader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (fi != null) {
                    fi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultBuffer.toString();
    }

}
