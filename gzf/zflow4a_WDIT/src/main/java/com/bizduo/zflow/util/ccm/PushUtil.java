package com.bizduo.zflow.util.ccm;

import com.bizduo.zflow.domain.dm.CCMPush;


public class PushUtil {
	/**
	 * 根据 type 获取url
	 * @param pushType
	 * @return
	 */
	public static String pushUri(int pushType) { 
		String url ="";
		switch (pushType) {
		case 1:
			url ="/push/taLeaderApprove";
			break;
		case 2: 
			url ="/push/accept";
			break;
		case 3: 
			url ="/push/comment";
			break;
		case 4: 
			url ="/push/reply";
			break;
		case 5: 
			url ="/push/acceptTimed";
			break;
		case 6: 
			url ="/push/replyTimed";
			break; 
		}
		return url;
	}
	/**
	 * 参数
	 * @param pushType
	 * @param params
	 * @return
	 */
	public static String pushParams(  CCMPush push ) { 
		StringBuffer params = new StringBuffer(); 
		switch (push.getPushType()) {
		case 1:
			params.append("&releatedJsonArray").append("=").append(push.getReleatedJsonArray())
            .append("&unReleatedJsonArray").append("=").append(push.getUnReleatedJsonArray());
			break;
		case 2: 
			params.append("&allPPJsonArray").append("=").append(push.getAllPPJsonArray())
             .append("&acceptJsonArray").append("=").append(push.getAcceptJsonArray());
			break;
		case 3: 
			params.append("&allPPJsonArray").append("=").append(push.getAllPPJsonArray());
			break;
		case 4:  
			String feedback=push.getFeedback();
			if(feedback==null)feedback="";
			params.append("&allPPJsonArray").append("=").append(push.getAllPPJsonArray())
				.append("&feedback").append("=").append(feedback);
			break;
		case 5: 
			params.append("&releatedJsonArray").append("=").append(push.getReleatedJsonArray());
			break;
		case 6:  
			params.append("&acceptedPPJsonArray").append("=").append(push.getAcceptJsonArray());
			break; 
		}
		return params.toString();
	}
}
