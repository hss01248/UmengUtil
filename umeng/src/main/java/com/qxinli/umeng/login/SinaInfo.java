package com.qxinli.umeng.login;

/**
 * Created by Administrator on 2017/2/15 0015.
 * 用户id：uid

 accesstoken: accessToken （6.2以前用access_token）

 refreshtoken: （6.2以前用refresh_token）

 过期时间：expiration （6.2以前用expires_in）

 用户名：name（6.2以前用screen_name）

 位置：location

 头像：iconurl（6.2以前用profile_image_url）

 性别：gender

 关注数：followers_count

 好友数：friends_count
 */

public class SinaInfo extends BaseInfo{
    public String location;
    public String followers_count;
    public String friends_count;




}
