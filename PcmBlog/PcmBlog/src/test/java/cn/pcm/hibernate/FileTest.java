package cn.pcm.hibernate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.pcm.domain.Resource;
import cn.pcm.service.FileService;

@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class FileTest {

	@Autowired
	@Qualifier("fileService")
	private FileService fileService;
	
	@Test
	public void addFile(){
		Resource resource=fileService.getResource(103);
		for(int i=0;i<50;i++)
		fileService.addResource(resource);
		 resource=fileService.getResource(104);
		for(int i=0;i<50;i++)
		fileService.addResource(resource);
	}
	
}
