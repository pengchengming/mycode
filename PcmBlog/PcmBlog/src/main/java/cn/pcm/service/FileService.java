package cn.pcm.service;

import java.util.List;

import cn.pcm.domain.Resource;

public interface FileService {

	public void addResource(Resource resource);
	  
	public int getResourceCount();
	
	public List<Resource> getResource(int start, int count);
	
	public Boolean delResource(int id);
	
	public Resource getResource(int id);
}
