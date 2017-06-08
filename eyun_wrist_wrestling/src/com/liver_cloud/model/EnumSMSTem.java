package com.liver_cloud.model;


/**
 * 短信模板枚举
 */
public enum EnumSMSTem {
	// 后去模板请自行补充
	/**
	 * 验证码模板
	 */
	IDENTIFYINGCODE(22096),
	/**
	 * 邀请码模板
	 */
	InviteCODE(22392);

	public Integer value;

	EnumSMSTem(Integer value) {
		this.value = value;
	}

	/**
	 * 根据值取枚举
	 *
	 * @param value
	 * @return
	 */
	public static EnumSMSTem valueOf(Integer value) {
		if (value == null) {
			return null;
		}
		for (EnumSMSTem enumValue : values()) {
			if (value.equals(enumValue.value)) {
				return enumValue;
			}
		}
		return null;
	}
}
