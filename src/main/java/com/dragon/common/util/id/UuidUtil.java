package com.dragon.common.util.id;

import java.util.UUID;

public class UuidUtil {
	
	public static String get32UUID() {
		String uuid = getUUID().trim().replaceAll("-", "");
		return uuid;
	}
	
	/**
	 * 生成UUID
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
}

