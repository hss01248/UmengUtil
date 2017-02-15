package com.qxinli.umeng.login;

/**
 * Created by Administrator on 2017/2/15 0015.
 * openid:openid

 unionid:（6.2以前用unionid）uid

 accesstoken: accessToken （6.2以前用access_token）

 refreshtoken: refreshtoken: （6.2以前用refresh_token）

 过期时间：expiration （6.2以前用expires_in）

 name：name（6.2以前用screen_name）

 城市：city

 省份：prvinice

 国家：country

 性别：gender

 头像：iconurl（6.2以前用profile_image_url）
 */

public class WeixinInfo extends BaseInfo{
    public String openid;
    public String country;
}
