package com.guoliang.framekt.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:52
 */
object FileUtil {
    private const val TAG = "FileUtil"

    /**
     * 文件删除
     * @param filePath 文件地址
     * @return 是否删除成功
     */
    fun fileDelete(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    /**
     * 对文件重命名
     * @param filePath 文件地址
     * @param reName 新命名
     * @return 是否成功
     */
    fun fileRename(filePath: String, reName: String): Boolean {
        val file = File(filePath)
        val path =
            filePath.substring(0, filePath.lastIndexOf("/") + 1) + reName + filePath.substring(
                filePath.lastIndexOf("."),
                filePath.length
            )
        val newFile = File(path)
        return file.renameTo(newFile)
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean 是否成功
     */
    fun copyFile(oldPath: String, newPath: String, isRetain:Boolean=true): Boolean {
        return try {
            var byTeam = 0
            var bantered = 0
            val oldFile = File(oldPath)
            //文件存在时
            if (oldFile.exists()) {
                //读入原文件
                val inStream: InputStream = FileInputStream(oldPath)
                val fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                while (inStream.read(buffer).also { bantered = it } != -1) {
                    byTeam += bantered //字节数 文件大小
                    println(byTeam)
                    fs.write(buffer, 0, bantered)
                }
                inStream.close()
                if (!isRetain) {
                    fileDelete(oldPath)
                }
            }
            true
        } catch (e: Exception) {
            LogUtil.e(TAG, e.message)
            false
        }
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean 是否成功
     */
    fun copyFolder(oldPath: String, newPath: String): Boolean {
        return try {
            val newFile = File(newPath)
            //如果文件夹不存在 则建立新文件夹
            if (!newFile.mkdirs()) {
                val mkdirs = newFile.mkdirs()
            }
            val a = File(oldPath)
            val file = a.list()
            var temp: File? = null
            for (i in 0 until (file?.size ?: 0)) {
                temp = if (oldPath.endsWith(File.separator)) {
                    File(oldPath + file!![i])
                } else {
                    File(oldPath + File.separator + file!![i])
                }
                if (temp.isFile) {
                    val input = FileInputStream(temp)
                    val output = FileOutputStream(
                        newPath + "/" +
                                temp.name.toString()
                    )
                    val b = ByteArray(1024 * 5)
                    var len: Int
                    while (input.read(b).also { len = it } != -1) {
                        output.write(b, 0, len)
                    }
                    output.flush()
                    output.close()
                    input.close()
                }
                if (temp.isDirectory) { //如果是子文件夹
                    copyFolder(
                        oldPath + "/" + file[i],
                        newPath + "/" + file[i]
                    )
                }
            }
            true
        } catch (e: Exception) {
            LogUtil.e(TAG, e.message)
            false
        }
    }


}