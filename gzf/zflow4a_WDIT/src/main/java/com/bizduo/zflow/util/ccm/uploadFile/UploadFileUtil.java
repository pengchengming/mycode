package com.bizduo.zflow.util.ccm.uploadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bizduo.zflow.util.FileUtil;

public class UploadFileUtil {
	/**
     * 写文件到本地 
     * @param in  输入的文件流
     * @param fileName   上传的文件名
     * @throws IOException
     */
    public static void copyFile(InputStream in, String filePath) throws IOException {
		File uploadRecord = new File(filePath); 
   	 	FileUtil.createFile(uploadRecord,filePath);
        FileOutputStream fs = new FileOutputStream(filePath); 
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = in.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        in.close();
    }
}
