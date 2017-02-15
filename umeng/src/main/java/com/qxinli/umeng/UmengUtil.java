package com.qxinli.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.qxinli.umeng.login.AuthCallback;
import com.qxinli.umeng.login.BaseInfo;
import com.qxinli.umeng.login.QQInfo;
import com.qxinli.umeng.login.SinaInfo;
import com.qxinli.umeng.login.WeixinInfo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class UmengUtil {
    private static Context context;
    private static UMShareAPI umShareAPI;
    private static SHARE_MEDIA[] shareMedias ;//SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE
    public static void init(Context context1,String sinaCallbackUrl,boolean degbug,SHARE_MEDIA... shareMediaList){
        context = context1;
       // UMShareAPI.get(context1);
        Config.REDIRECT_URL = sinaCallbackUrl;//http://sns.whalecloud.com/sina2/callback
        shareMedias = shareMediaList;
        Config.isJumptoAppStore = true;
        Config.DEBUG = degbug;
        umShareAPI =  UMShareAPI.get(context1);
        //对应平台没有安装的时候跳转转到应用商店下载,其中qq 微信会跳转到下载界面进行下载，其他应用会跳到应用商店进行下载
        //友盟统计
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType. E_UM_NORMAL);
    }

    public static void setKeySecretWeixin(String key,String secret){
        PlatformConfig.setWeixin(key,secret);
    }
    public static void setKeySecretQQ(String key,String secret){
        PlatformConfig.setQQZone(key,secret);
    }
    public static void setKeySecretSina(String key,String secret){
        PlatformConfig.setSinaWeibo(key,secret);
    }





    public static void onActivityResult(Activity activity,int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
    public static void onDestroy(Activity activity) {
        UMShareAPI.get(activity).release();
    }

    public static void shareTxt(Activity activity, final String uid,
                                String title, String desc, String thumbUrl,
                                String targetUrl, final ShareCallback callback){
        share(0,activity,uid,title,desc,"",thumbUrl,targetUrl,callback);
    }

    /**
     * 自带分享的统计,其中uid是app本身的账户系统的id
     * @param type
     * @param activity
     * @param uid
     * @param title
     * @param desc
     * @param thumbUrl
     * @param mediaUrl
     * @param targetUrl
     * @param callback
     */
    private static void share(int type,Activity activity, final String uid,
                              String title, String desc, String thumbUrl, String mediaUrl,
                              String targetUrl, final ShareCallback callback){

        ShareAction action = new ShareAction(activity);

        if(type==0){
           action.withMedia(new UMImage(activity, mediaUrl)) ;
        }else if(type == 1){
            UMusic music = new UMusic(mediaUrl);
            music.setTitle(title);//音乐的标题
            music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
            music.setDescription(desc);//音乐的描述
           action.withMedia(music);
        }else if(type==2){
            UMVideo music = new UMVideo (mediaUrl);
            music.setTitle(title);//音乐的标题
            music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
            music.setDescription(desc);//音乐的描述
            action.withMedia(music);
        }
        action.withTitle(title)
                .withText(desc)
                .withTargetUrl(targetUrl)
                .setDisplayList(shareMedias)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        UMPlatformData.UMedia media = UMPlatformData.UMedia.SINA_WEIBO;
                        if(share_media ==SHARE_MEDIA.QQ){
                            media = UMPlatformData.UMedia.TENCENT_QQ;
                        }else if(share_media ==SHARE_MEDIA.SINA){
                            media = UMPlatformData.UMedia.SINA_WEIBO;
                        }else if(share_media ==SHARE_MEDIA.WEIXIN){
                            media = UMPlatformData.UMedia.WEIXIN_FRIENDS;
                        }else if(share_media ==SHARE_MEDIA.WEIXIN_CIRCLE){
                            media = UMPlatformData.UMedia.WEIXIN_CIRCLE;
                        } else if(share_media ==SHARE_MEDIA.QZONE){
                            media = UMPlatformData.UMedia.TENCENT_QZONE;
                        }


                        UMPlatformData platform = new UMPlatformData(media, uid);

                        MobclickAgent.onSocialEvent(context, platform);
                        callback.onResult(share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        callback.onError(share_media,throwable);

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        callback.onCancel(share_media);

                    }
                })
                .open();
    }

    public static void shareMusic(Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback){
        share(1,activity,uid,title,desc,thumbUrl,musicUrl,targetUrl,callback);
    }
    public static void shareVideo(Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback){
        share(2,activity,uid,title,desc,thumbUrl,musicUrl,targetUrl,callback);
    }

    public static void loginBySina(Activity activity,  AuthCallback callback){
        login(activity,SHARE_MEDIA.SINA,callback);
    }
    public static void loginByWeixin(Activity activity,  AuthCallback callback){
        login(activity,SHARE_MEDIA.WEIXIN,callback);
    }
    public static void loginByQQ(Activity activity,  AuthCallback callback){
        login(activity,SHARE_MEDIA.QQ,callback);
    }
    private static  void login(Activity activity, final SHARE_MEDIA platform, final AuthCallback callback){
        umShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                BaseInfo info = null;
                if(share_media == SHARE_MEDIA.SINA){
                    SinaInfo weixinInfo = new SinaInfo();
                    weixinInfo.followers_count = map.get("followers_count");
                    weixinInfo.friends_count = map.get("friends_count");
                    info = weixinInfo;

                }else if(share_media == SHARE_MEDIA.QQ){
                    QQInfo weixinInfo = new QQInfo();
                    weixinInfo.is_yellow_year_vip = map.get("is_yellow_year_vip");
                    info = weixinInfo;
                }else if(share_media == SHARE_MEDIA.WEIXIN){
                   WeixinInfo weixinInfo = new WeixinInfo();
                    weixinInfo.openid = map.get("openid");
                    weixinInfo.country = map.get("country");
                    info = weixinInfo;

                }
                info.accessToken = map.get("accessToken");
                info.city = map.get("city");
                info.expiration = map.get("expiration");
                info.gender = map.get("gender");
                info.iconurl = map.get("iconurl");
                info.name = map.get("name");
                info.province = map.get("province");
                info.refreshtoken = map.get("refreshtoken");
                info.uid = map.get("uid");



                callback.onComplete(i,info);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                callback.onError(i,throwable);

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                callback.onCancel(i);

            }
        });
    }



    //以下的是统计的api

    public static void analysisOnResume(Activity activity){
        MobclickAgent.onResume(activity);
    }
    public static void analysisOnPause(Activity activity){
        MobclickAgent.onPause(activity);
    }
    public static void onKillProcess(){
        MobclickAgent.onKillProcess(context);
    }

    public static void analysisOnPageStart(String  pageName){
        MobclickAgent.onPageStart(pageName);
    }
    public static void analysisOnPageEnd(String  pageName){
        MobclickAgent.onPageEnd(pageName);
    }

    public static void onProfileSignIn(String ID) {
        MobclickAgent.onProfileSignIn(ID);
    }
    public static void onProfileSignIn(String provider, String ID) {
        MobclickAgent.onProfileSignIn(provider,ID);
    }
    public static void onProfileSignOff(){
        MobclickAgent.onProfileSignOff();
    }




}
