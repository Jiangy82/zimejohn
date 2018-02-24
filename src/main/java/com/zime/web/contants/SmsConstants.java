package com.zime.web.contants;


/**
 * 短信常量
 */
public class SmsConstants {
  /**
   * 验证码类型
   * 
   * @code 类型
   * @templet 短信模板
   */
  public static enum SmsCaptchaType {
    /**
     * 注册
     * 
     * @code 1
     * @templet 注册，验证码：%s，%d分钟内有效。
     */
    REGIST((byte) 1, "您的验证码是%s ，请您在%d分钟内填写。如非本人操作，请忽略本短信。"),
    /**
     * 修改密码
     * 
     * @code 2
     * @templet 修改密码，验证码：%s，%d分钟内有效。
     */
    CHANGE_PWD((byte) 2, "您的验证码是%s ，请您在%d分钟内填写。如非本人操作，请忽略本短信。"),
    /**
     * 重置密码
     * 
     * @code 3
     * @templet 修改密码，验证码：%s，%d分钟内有效。
     */
    RESET_PWD((byte) 3, "您的验证码是%s ，请您在%d分钟内填写。如非本人操作，请忽略本短信。"),
    
    /**
     * 登陆验证码
     */
    LOGIN((byte) 4, "您的验证码是%s ，请您在%d分钟内填写。如非本人操作，请忽略本短信。"),
    
    /**
     * 更换手机号
     */
    CHANGE_MOBILE((byte) 5, "您的验证码是%s ，请您在%d分钟内填写。如非本人操作，请忽略本短信。"),
	    
	    /**
	     * 绑定实体卡
	     */
		BindPlCard_MOBILE((byte) 6, "您的验证码是%s ，请您在%d分钟内填写。如非本人操作，请忽略本短信。");

	  
	  
    public byte code;
    public String templet;

    private SmsCaptchaType(byte code, String templet) {
      this.code = code;
      this.templet = templet;
    }

    /**
     * 根据code获取模板
     * @param code
     * @return
     */
    public static String getTemplet(byte code) {
      SmsCaptchaType[] elements = SmsCaptchaType.values();

      for (int i = 0; i < elements.length; i++) {
        if (elements[i].code == code) {
          return elements[i].templet;
        }
      }
      return null;
    }

    /**
     * 根据code获取模板SmsCaptchaType
     * @param code
     * @return
     */
    public static SmsCaptchaType getSmsCaptchaType(byte code) {
      SmsCaptchaType[] elements = SmsCaptchaType.values();
      for (int i = 0; i < elements.length; i++) {
        if (elements[i].code == code) {
          return elements[i];
        }
      }
      return null;
    }
  }

  /**
   * 短信验证码缓存 SMS_CAPTCHA_type_mobile
   * SMS_CAPTCHA_%s_%s
   */
  public static final String CAPTCHA_KEY = "SMS_CAPTCHA_%s_%s";

  /**
   * 短信验证码有效时间(秒)， 5分钟
   */
  public static final int SMS_VALID_TIME = 300 ; 
  /**
   * 短信验证码发送冷却时间(秒) 1分钟
   */
  public static final int SMS_COOL_TIME = 60 ; 

  /**
   * 批量短信发送每次最多手机个数
   */
  public static final int MAX_COUNT = 10;

  /**
   * 手机正则表达式 ^1[34578]\\d{9}$
   */
  public static final String MOBILE_REGEX = "^1[34578]\\d{9}$";

}
