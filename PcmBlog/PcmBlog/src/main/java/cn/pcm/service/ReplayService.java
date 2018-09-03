package cn.pcm.service;

import java.util.List;

import cn.pcm.domain.Replay;

public interface ReplayService {
	public void addReplay(Replay replay);

	public List<Replay> getReplay(int replayid);
	
	public Boolean delReplay(int id);
	
}
