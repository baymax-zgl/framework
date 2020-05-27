package com.guoliang.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:52
 */
public class FileUtil {
    private FileUtil() { }

    private static final String TAG = "FileUtil";
    /**
     * FileUtil实例
     */
    private static FileUtil INSTANCE;

    public static FileUtil getInstance(){
        if (INSTANCE==null){
            synchronized (FileUtil.class){
                INSTANCE = new FileUtil();
            }
        }
        return INSTANCE;
    }

    /**
     * 文件删除
     * @param filePath 文件地址
     * @return 是否删除成功
     */
    public boolean fileDelete(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            return file.delete();
        }else {
            return false;
        }
    }

    /**
     * 对文件重命名
     * @param filePath 文件地址
     * @param reName 新命名
     * @return 是否成功
     */
    public boolean fileRename(String filePath,String reName){
        File file = new File(filePath);
        String path=filePath.substring(0, filePath.lastIndexOf("/") + 1) + reName + filePath.substring(filePath.lastIndexOf("."), filePath.length());
        File newFile = new File(path);
        return file.renameTo(newFile);
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean 是否成功
     */
    public boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldFile = new File(oldPath);
            //文件存在时
            if (oldFile.exists()) {
                //读入原文件
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            return true;
        }
        catch (Exception e) {
            LogUtil.getInstance().e(TAG,e.getMessage());
            return false;
        }

    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean 是否成功
     */
    public boolean copyFolder(String oldPath, String newPath) {

        try {
            File newFile = new File(newPath);
            //如果文件夹不存在 则建立新文件夹
            if (!newFile.mkdirs()){
                boolean mkdirs = newFile.mkdirs();
            }
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < (file != null ? file.length : 0); i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else{
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
            return true;
        }
        catch (Exception e) {
            LogUtil.getInstance().e(TAG,e.getMessage());
            return false;
        }
    }

}
