package com.spoon.dev.bottomselector.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
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
public class DBMove {

    public static String DBNAME = "area.db";

    public static SQLiteDatabase move(Context context, String dbName) {
        if (!TextUtils.isEmpty(dbName)) {
            DBNAME = dbName;
        }
        File f = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/" + DBNAME);
        if (!f.exists()) {//不存在,复制数据库
            moveDBFile(context, "assets:" + DBNAME, DBNAME);
        }
        if (f.exists())//存在返回SQLiteDatabase对象
            return SQLiteDatabase.openDatabase(f.getPath(), null, SQLiteDatabase.OPEN_READONLY);
        return null;
    }

    private static void moveDBFile(Context context, String filePath, String name) {
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
