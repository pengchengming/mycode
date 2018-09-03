package cn.pcm.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.pcm.dao.FileDao;
import cn.pcm.domain.Resource;

@Service("fileService")
public class FileServiceImpl implements FileService{
	
	
	@Autowired
	@Qualifier("fileDao")
	private FileDao fileDao;

	@Override
	public void addResource(Resource resource) {
		fileDao.addResource(resource);
	}

	@Override
	public int getResourceCount() {
		return fileDao.getResourceCount();
	}

	@Override
	public List<Resource> getResource(int start, int count) {
		return fileDao.getResource(start, count);
	}

	@Override
	public Boolean delResource(int id) {
		Boolean flag = false;
		String path="D:\\develop\\newWorkspace2\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp25\\wtpwebapps\\PcmBlog\\attached\\upload";
		
		Resource resource=fileDao.getResource(id);		
		String filename=resource.getUuidName();
		File file = new File(path+"//"+filename);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		fileDao.delResource(id);
		return flag;
	}

	@Override
	public Resource getResource(int id) {
	return fileDao.getResource(id);
	}

}
