package com.qxinli.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.UmengTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class UmengUtil {
    private static Context context;
    private static UMShareAPI umShareAPI;
    private static SHARE_MEDIA[] shareMedias ;//SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE
    public static void init(Context context1,String sinaCallbackUrl,SHARE_MEDIA... shareMediaList){
        context = context1;
        UMShareAPI.get(context1);
        Config.REDIRECT_URL = sinaCallbackUrl;//http://sns.whalecloud.com/sina2/callback
        shareMedias = shareMediaList;
        Config.isJumptoAppStore = true;
        Config.DEBUG = true;
        //对应平台没有安装的时候跳转转到应用商店下载,其中qq 微信会跳转到下载界面进行下载，其他应用会跳到应用商店进行下载

        PlatformConfig.setWeixin("wx29e69e59e1acc297", "03aa889be7d1ca6c90144299b9f86c92");
        PlatformConfig.setSinaWeibo("3255824485", "303b08e2313912a4311fdcc6ab42156a");
        PlatformConfig.setQQZone("1104927660", "ZFNJSToh3SSBbtYh");


    }

    public static void onActivityResult(Activity activity,int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
    public static void onDestroy(Activity activity) {
        UMShareAPI.get(activity).release();
    }

    public static void shareTxt(Activity activity, String title,String text,String targetUrl,String imageUrl,UMShareListener umShareListener){
        new ShareAction(activity)
                .withText(text)
                .withTitle(title)
                .withTargetUrl(targetUrl)
                .withMedia(new UMImage(activity, imageUrl))
                .setDisplayList(shareMedias)
                .setCallback(umShareListener)
                .open();
        UmengTool.getSignature(activity);
    }

    public static void shareMusic(Activity activity,
                                                 String title,String desc,String thumbUrl,String musicUrl,
                                                 String text,String targetUrl,UMShareListener umShareListener){
        UMusic music = new UMusic(musicUrl);
        music.setTitle(title);//音乐的标题
        music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
        music.setDescription(desc);//音乐的描述

        new ShareAction(activity)
                .withText(text)
                .withTargetUrl(targetUrl)
                .withMedia(music)
                .setDisplayList(shareMedias)
                .setCallback(umShareListener)
                .open();
    }
    public static void shareVideo(Activity activity,
                                                 String title,String desc,String thumbUrl,String videoUrl,
                                                 String text,String targetUrl,UMShareListener umShareListener){
        UMVideo music = new UMVideo (videoUrl);
        music.setTitle(title);//音乐的标题
        music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
        music.setDescription(desc);//音乐的描述

        new ShareAction(activity)
                .withTitle(title)
                .withText(text)
                .withTargetUrl(targetUrl)
                .withMedia(music)
                .setDisplayList(shareMedias)
                .setCallback(umShareListener)
                .open();
    }





}
