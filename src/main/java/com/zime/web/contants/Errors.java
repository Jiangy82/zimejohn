package com.zime.web.contants;

/**
 * 错误码（400-599 禁止使用）
 *
 */
public enum Errors {
	// 0-200 系统级
	SUCCESS(0, "操作成功"),
	//
	SYSTEM_ERROR(1, "系统错误"),
	// 自定义错误，可以修改label
	SYSTEM_CUSTOM_ERROR(2, "自定义错误"),
	//
	SYSTEM_DATA_ERROR(3, "数据异常"),
	//
	SYSTEM_DATA_NOT_FOUND(4, "数据不存在"),
	//
	SYSTEM_NOT_LOGIN(5, "请登录"),
	//
	SYSTEM_UPDATE_ERROR(6, "数据更新失败"),
	//
	SYSTEM_NO_ACCESS(7, "无权限访问"),
	//
	SYSTEM_REQUEST_PARAM_ERROR(8, "请求参数错误"),
	//
	SYSTEM_MAX_FILE_ERROR(9, "只能上传小于50MB的文件"),

	// 101-200，用户模块
	USER_LOGIN_ERROR(101, "用户名或密码错误"),
	//
	USER_NOT_FOND(102, "该用户不存在"),
	//
	USER_STATUS_ERROR(103, "修改用户状态不符合规则"),

	// 短信发送模块
	USER_MOBILE_EXISTS(210, "该手机号已经注册"),
	//
	USER_MOBILE_NOT_REGISTER(211, "该手机号并未注册"),
	//
	USER_SMS_SEND_FAST(212, "请30秒后再试"),
	//
	USER_SMS_SEND_ERROR(213, "短信接口调用失败"),
	//
	CAPTCHA_IS_NULL(214, "验证码不存在"),
	//
	CAPTCHA_EXPIRED(215, "验证码已过期"),
	//
	CAPTCHA_ERROR(216, "验证码输入有误"),
	//
	WEIXIN_USER_NOT_LOGIN(217, "微信用户未登陆"),
	//
	BIND_CARD_CODE(218, "该手机号不存在"),
	//
	BIND_CARD_HAS(219, "此手机号已经被绑定");

	public int code;
	public String label;

	private Errors(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	/**
	 * 获取状态码描述
	 * 
	 * @param code
	 *            状态码
	 * @return 状态码描述，如果没有返回空串
	 */
	public static String getLabel(int code) {
		String result = "";
		for (Errors status : Errors.values()) {
			if (status.code == code) {
				result = status.label;
			}
		}
		return result;
	}

}
