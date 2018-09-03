package cn.pcm.fileOperate;

import java.io.File;

import org.junit.Test;

import cn.pcm.domain.Resource;

public class Demo1 {

	@Test
	public void test1() {
		Boolean flag = false;
		String path="D:\\develop\\newWorkspace2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp25\\wtpwebapps\\PcmBlog\\attached\\upload";

		String filename="2.jpg";
		File file = new File(path+"//"+filename);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		System.out.println(flag);
	}
}
