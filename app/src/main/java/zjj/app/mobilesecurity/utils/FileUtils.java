package zjj.app.mobilesecurity.utils;

import android.util.Log;

import java.io.File;

import zjj.app.mobilesecurity.BuildConfig;

public class FileUtils {


    /**
     * 遍历目录下文件
     * @param directory
     */
    public static void displayDirAndFiles(String directory) {
        File dir = new File(directory);
        if (BuildConfig.DEBUG) Log.d("FileUtils", directory);
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files) {
                displayDirAndFiles(file.getAbsolutePath());
            }
        }else{
            if (BuildConfig.DEBUG) Log.d("FileUtils", dir.getAbsolutePath());
        }

    }
}
