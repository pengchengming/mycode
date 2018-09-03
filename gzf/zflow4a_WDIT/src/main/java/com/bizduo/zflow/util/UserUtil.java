package com.bizduo.zflow.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bizduo.zflow.domain.sys.User;

public class UserUtil {

	public static User getUser() {
		User user = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx == null) {
            return null;
        }
        if (ctx.getAuthentication() == null) {
            return null;
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
            return null;
        }
        if (ctx.getAuthentication().getPrincipal() instanceof User) {
            user = (User) ctx.getAuthentication().getPrincipal();
        }
        return user;
	}
	
	public static boolean checkAgentIsMobile(String ua) {
		final   String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" };
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
	
	public static int checkAgentIsMobileInt(String ua) {
		final   String[] agent = { "Android", "iPhone", "iPod", "Windows Phone", "MQQBrowser" };
		final   String[] pad = { "iPad" };
		int flag = 0;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
					if (ua.contains(item)) {
						flag = 1; break;
					}
				}
				for (String item : pad) {
					if (ua.contains(item)) {
						flag = 2; break;
					}
				}
			}
		}
		return flag;
	}
	
}
