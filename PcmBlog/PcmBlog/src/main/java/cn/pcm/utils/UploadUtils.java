package cn.pcm.utils;

import java.util.UUID;


public class UploadUtils {
	// 生成随机目录
	public static String generateRandomDir(String uuidname) {
		int hashcode = uuidname.hashCode();
		// 一级目录
		int d1 = hashcode & 0xf;
		// 二级目录
		int d2 = (hashcode >> 4) & 0xf;

		return "/" + d1 + "/" + d2;
	}

	// 根据源文件名 ，生成唯一文件名
	public static String generateUUIDName(String filename) {
		// 获得文件扩展名
		String ext = filename.substring(filename.lastIndexOf("."));
		return UUID.randomUUID().toString() + ext;
	}

	// 传入参数filename 可以含有路径，该方法将路径切掉
	public static String subFileName(String filename) {
		if (filename.contains("\\")) {
			return filename.substring(filename.lastIndexOf("\\") + 1);
		}
		return filename;
	}
}
