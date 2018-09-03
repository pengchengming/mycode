package cn.pcm.dao;

import java.util.List;

import cn.pcm.domain.Replay;

public interface ReplayDao {
	public void addReplay(Replay replay);

	public List<Replay> getReplay(int replayid);
	
	public Boolean delReplay(int id);
	
}
