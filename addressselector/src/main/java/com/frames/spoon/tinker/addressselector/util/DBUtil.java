package com.frames.spoon.tinker.addressselector.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhanxiaolin-n22 on 2017/3/14.
 */

public class DBUtil {

    public static final String DBNAME = "area.db";

    public static void initDB(Context context) {
        File f = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/" + DBNAME);
        if (!f.exists()) {
            moveDBFile(context, "assets:" + DBNAME, DBNAME);
            Log.e("moveDBFile", "移动数据库成功");
        }
    }

    /**
     * 移动数据库文件
     */
    public static void moveDBFile(Context context, String filePath, String name) {
        File f = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/");
        if (!f.exists())
            f.mkdirs();
        InputStream input = null;
        OutputStream output = null;
        try {
            if (filePath.startsWith("assets:")) {
                input = context.getAssets().open(filePath.replaceFirst("assets:", "").trim());
            } else {
                input = new FileInputStream(new File(filePath));
            }
            output = new FileOutputStream(context.getDatabasePath(name));
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
        } catch (Exception e) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
