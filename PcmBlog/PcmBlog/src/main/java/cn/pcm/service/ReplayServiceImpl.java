package cn.pcm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.pcm.dao.ReplayDao;
import cn.pcm.domain.Replay;

@Service("replayService")
public class ReplayServiceImpl implements ReplayService{

	
	@Autowired
	@Qualifier("replayDao")
	private ReplayDao replayDao;
	
	@Override
	public void addReplay(Replay replay) {
		 replayDao.addReplay(replay);
	}

	@Override
	public List<Replay> getReplay(int replayid) {
		// TODO Auto-generated method stub
		return replayDao.getReplay(replayid);
	}

	@Override
	public Boolean delReplay(int id) {
		// TODO Auto-generated method stub
		return replayDao.delReplay(id);
	}

}
