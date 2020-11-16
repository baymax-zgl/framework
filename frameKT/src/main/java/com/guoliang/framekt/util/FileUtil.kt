package com.guoliang.framekt.util

import java.io.*
import java.util.*
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

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

    fun copyFile(oldPath: String,block:((String)->Unit)?=null): Boolean {
        try {
            var byTeam = 0
            var bantered = 0
            val oldFile = File(oldPath)
            val newPath = getCopyNameFromOriginalTest(oldPath)
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
            }
            block?.invoke(newPath)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e(TAG, e.message)
            return false
        }
    }

    /**
     * @Description 得到文件副本名称，可供粘贴及多选重命名方法使用
     * 命名规则为：普通文件后加“ 1”，若文件末尾已有“ 数字”，则数字递增。
     * 比如，有个文件叫“我.jpg”，使用本方法后得到了“我 1.jpg”，再次使用本方法后得到“我 2.jpg”
     * @param originalName 原本的名字，XXX.xx 或者完整路径 xx/xx/XXX.xx ， 也可以没有后缀.xx
     * @return 副本名称
     */
    fun getCopyNameFromOriginal(originalName: String?): String? {
        //1.判断阈值
        if (originalName == null || originalName.isEmpty()) {
            return null
        }
        var copyName: String? = null
        //2.得到文件名和后缀名

        val nameAndExt: List<String> = originalName.split(".")
        val sb = StringBuilder()
        nameAndExt.forEachIndexed { index, s ->
            if (index != nameAndExt.size - 1) {
                sb.append(s)
            }
        }
        val fileName = sb.toString()
        val fileExt = "." + nameAndExt[nameAndExt.size - 1]
        //3.判断文件名是否包含我们定义副本规范的标记字符（空格）
        if (fileName.contains(" ")) { //如果文件名包涵空格，进行判断是否已经为副本名称
            //4-1.得到end
            var array = fileName.split(" ".toRegex()).toTypedArray()
            var end = array[array.size - 1] //得到标记字符后面的值
            //4-2.确保end得到的是最后面的值（防止出现类似路径中的目录也有标记字符的情况，如："mnt/sda/wo de/zhao pian/我的 照片 1.png"）
            while (end.contains(" ")) {
                array = fileName.split(" ".toRegex()).toTypedArray()
                end = array[array.size - 1]
            }
            //5.判断标记字符后的字符串是否复合规范（是否是数字）
            val isDigit = end.matches(Regex("[0-9]+")) //用正则表达式判断是否是正整数
            if (isDigit) {
                try {
                    val index = end.toInt() + 1 //递增副本记数
                    val position = fileName.lastIndexOf(" ") //得到最后的空格的位置，用于截取前面的字符串
                    if (position != -1) {
                        //6-1.构造新的副本名（数字递增）
                        copyName = fileName.substring(0, position + 1) + index.toString()
                    }
                } catch (e: java.lang.Exception) { //转化成整形错误
                    e.printStackTrace()
                    return null
                }
            } else { //如果空格后不是纯数字，即不为我们定义副本的规范
                //6-2.构造新的副本名（数字初始为1）
                copyName = "$fileName 1"
            }
        } else { //如果没有，则变为副本名称格式
            //6-3.构造新的副本名（数字初始为1）
            copyName = "$fileName 1"
        }
        LogUtil.e(TAG, "new copy name is $copyName$fileExt")
        //6.返回副本名+后缀名
        return copyName + fileExt
    }

    /**
     * 重命名或复制规则
     * originalName:源文件路径
     * xxx(1)
     * xxx(1)(1)
     * xxx(1)(2)
     * xxx(1)(2)(1)
     * xxx(1)(2)(2)
     * xxx(1)(2)(2)(1)
     */
    private fun getCopyNameFromOriginalTest(originalName: String): String {
        val oldFile = File(originalName)
        val oldFileName = oldFile.name
        val nameAndExt: List<String> = oldFileName.split(".")
        val sb = StringBuilder()
        //防止名称有.符合
        nameAndExt.forEachIndexed { index, s ->
            if (index != nameAndExt.size - 1) {
                sb.append(s)
            }
        }
        var fileName = sb.toString()
        val fileExt = "." + nameAndExt[nameAndExt.size - 1]
        var newTimeFileName = "$fileName(1)$fileExt"
        fileName=fileName.replace("(","\\(")
        fileName=fileName.replace(")","\\)")
        val oldRegex="$fileName[(][0-9]+[)]$fileExt"
        val pattern = Pattern.compile(oldRegex)
        val parentFile = oldFile.parentFile
        var number = 1
        parentFile!!.listFiles()?.forEach { it ->
            val matcher = pattern.matcher(it.name)
            if (matcher.find() && it.length() == oldFile.length()) {
                val newNumber = it.name.substring(it.name.lastIndexOf("(") + 1, it.name.lastIndexOf(")")).toInt()
                number = if (newNumber >= number) newNumber + 1 else number
                newTimeFileName = it.name.substring(0, it.name.lastIndexOf("(") + 1) + number + it.name.substring(it.name.lastIndexOf(")"), it.name.length)
            }
        }
        return parentFile.absolutePath + "/" + newTimeFileName
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean 是否成功
     */
    fun copyFile(oldPath: String, newPath: String, isRetain: Boolean = true): Boolean {
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

    //    public static String unzipFile(String zipPtath, String outputDirectory)throws IOException {
    //        /**
    //         * 解压assets的zip压缩文件到指定目录
    //         * @param context上下文对象
    //         * @param assetName压缩文件名
    //         * @param outputDirectory输出目录
    //         * @param isReWrite是否覆盖
    //         * @throws IOException
    //         */
    //
    //        // 创建解压目标目录
    //        File file = new File(outputDirectory);
    //        // 如果目标目录不存在，则创建
    //        if (!file.exists()) {
    //            file.mkdirs();
    //        }
    //        // 打开压缩文件
    //        InputStream inputStream = new FileInputStream(zipPtath); ;
    //        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
    //
    //        // 读取一个进入点
    //        ZipEntry zipEntry = zipInputStream.getNextEntry();
    //        // 使用1Mbuffer
    //        byte[] buffer = new byte[1024 * 1024];
    //        // 解压时字节计数
    //        int count = 0;
    //        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
    //        while (zipEntry != null) {
    //            if (!zipEntry.isDirectory()) {  //如果是一个文件
    //                // 如果是文件
    //                String fileName = zipEntry.getName();
    //                Log.i("qianjujun","解压文件 原来 文件的位置： " + fileName);
    //                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);  //截取文件的名字 去掉原文件夹名字
    //                Log.i("qianjujun","解压文件 的名字： " + fileName);
    //                file = new File(outputDirectory + File.separator + fileName);  //放到新的解压的文件路径
    //
    //                file.createNewFile();
    //                FileOutputStream fileOutputStream = new FileOutputStream(file);
    //                while ((count = zipInputStream.read(buffer)) > 0) {
    //                    fileOutputStream.write(buffer, 0, count);
    //                }
    //                fileOutputStream.close();
    //
    //            }
    //
    //            // 定位到下一个文件入口
    //            try {
    //                zipEntry = zipInputStream.getNextEntry();
    //            }catch (Exception e){
    //                e.printStackTrace();
    //                Log.e("qianjujun", "unzipFile: ", e);
    //            }
    //
    //            Log.i("qianjujun","解压文件 入口 2： " + zipEntry );
    //        }
    //        zipInputStream.close();
    //        Log.i("qianjujun","解压完成");
    //        return outputDirectory;
    //    }

    fun unZip(zipFile: File, outDir: String?): File {
        var name = zipFile.name
        name = name.replace(".zip".toRegex(), "")
        val outFileDir = File(outDir, name)
        if (!outFileDir.exists()) {
            outFileDir.mkdirs()
        }
        val zip = ZipFile(zipFile)
        val enumeration: Enumeration<*> = zip.entries()
        while (enumeration.hasMoreElements()) {
            val entry: ZipEntry = enumeration.nextElement() as ZipEntry
            val zipEntryName: String = entry.getName()
            val `in`: InputStream = zip.getInputStream(entry)
            if (entry.isDirectory()) {      //处理压缩文件包含文件夹的情况
                val result = File(outDir, zipEntryName)
                result.mkdir()
                continue
            }
            val file = File(outDir, zipEntryName)
            file.createNewFile()
            val out: OutputStream = FileOutputStream(file)
            val buff = ByteArray(1024)
            var len: Int
            while (`in`.read(buff).also { len = it } > 0) {
                out.write(buff, 0, len)
            }
            `in`.close()
            out.close()
        }
        return outFileDir
    }

}