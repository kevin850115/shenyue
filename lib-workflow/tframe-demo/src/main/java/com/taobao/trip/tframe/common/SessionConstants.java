package com.taobao.trip.tframe.common;

public class SessionConstants {
    public static final String JIPIAO_TAOMI = "jipiaoTaomi";
    public static final String ATTRIBUTE_USER_ID = "userID"; // 淘宝字符ID
    public static final String ATTRIBUTE_ISLOGIN = "_l_g_";
    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ATTRIBUTE_NICK = "_nk_";
    public static final String ATTRIBUTE_USER_ID_NUM = "userIDNum";
    public static final String FORM_NO = "formNO";//行政购票验证formNO
    public static final String BUYER_IS_BUSINESS = "by";//买家是否是集团买家
    public static final String CORPORATION_ID = "corporationId";//行政购票验证corporationId
    
    public static final String AGENT_LOGIN_FLAG = "agent_login_flag"; // 卖家登陆管理权限标记
    public static final String BUYER_LOGIN_FLAG = "buyer_login_flag"; // 买家登陆管理权限标记
    public static final String ADMIN_LOGIN_FLAG = "admin_login_flag"; // 管理员登陆管理权限标记
    
    public static final String AGENT_USER_NAME = "n"; // keep short
    public static final String AGENT_USER_FULL_NAME = "fn"; // keep short
    public static final String AGENT_USER_NICK = "ln"; // login username

    public static final String BUYER_TAOBAO_NICK = "un"; //买家登录使用的淘宝nick
    public static final String BUYER_C2C_MENU_NICK = "_nk_"; //昵称
    public static final String BUYER_C2C_MENU_USERID = "et_userID";//淘宝32位id
    public static final String BUYER_C2C_MENU_HASSHOP = "et_existShop";//买家是否有商铺
    public static final String BUYER_C2C_MENU_SERVICE_TYPE = "et_service_type";//是否有Alisoft网店
    
    public static final String AGENT_TYPE = "agt";
    public static final Integer AGENT_TYPE_ET = new Integer(0);
    
    // 以string形式存贮于session中的id
    public static final String AGENT_USER_ID = "i"; // keep short
    
    // 最后访问时间
    public static final String LAST_ACCESS = "lv"; // keep short

    /**
     * 用户vip级别
     */
    public static final String VIP_LEVEL = "VIPLevel";
    
    //added by zhangting    c2c页头header.vm需要
    public static final String ATTRIBUTE_PUBLISH_ITEM = "et_publishItemObj";

}
