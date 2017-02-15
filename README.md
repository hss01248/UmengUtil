# UmengUtil
umeng分享,第三方登录以及统计的api的封装,避免再出现api大幅改动而到处改源码.

[![](https://jitpack.io/v/hss01248/UmengUtil.svg)](https://jitpack.io/#hss01248/UmengUtil)

# 初始化

```
init(Context context1,String sinaCallbackUrl,boolean degbug,SHARE_MEDIA... shareMediaList)

setKeySecretWeixin(String key,String secret)
setKeySecretQQ(String key,String secret)
setKeySecretSina(String key,String secret)
```

# 分享:

平台有:新浪微博,qq,qq空间,微信好友,微信朋友圈

分享成功后自动统计到友盟后台

内容有:文本,音频,视频

```
shareTxt(Activity activity, final String uid,
                            String title, String desc, String thumbUrl,
                            String targetUrl, final ShareCallback callback)
                            
shareMusic(Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback)
                                  
shareVideo(Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback)
```

注意需要在activity的生命周期方法中调用本util对应的方法:

```
UmengUtil.onActivityResult(this,requestCode,resultCode,data);
UmengUtil.onDestroy(this);
```

## 第三方登录

拿到第三方登录需要的信息.

新浪,qq,微信

注:第三方平台token容易过期,如果要做第三方登录的一次授权后永久登录功能,需要和自己的后端开发者商量接口,比如后续只需要传第三方平台uid之类的.

```
UmengUtil.loginBySina(this, new AuthCallback<SinaInfo>() {
                    @Override
                    public void onComplete(int var2, SinaInfo info) {
                        Log.e("dd",info.toString());

                    }

                    @Override
                    public void onError( int var2, Throwable var3) {

                    }

                    @Override
                    public void onCancel( int var2) {

                    }
                });

UmengUtil.loginByWeixin(this, new AuthCallback<WeixinInfo>() {
    @Override
    public void onComplete(int var2, WeixinInfo info) {
        Log.e("dd",info.toString());

    }

    @Override
    public void onError( int var2, Throwable var3) {

    }

    @Override
    public void onCancel( int var2) {

    }
})


```

## debug模式下也让apk装正式签名文件-gradle配置

```
//android下
signingConfigs {
    release {
        storeFile file("xxx.jks")
        storePassword "xxx"
        keyAlias "xxx"
        keyPassword "xxx"
    }
}
buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
```

## menifest中配置activity:

```
<activity
    android:name="com.umeng.qq.tencent.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true" >
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
		//这里要用qq开发者平台注册时用的qq号
        <data android:scheme="tencent1104927660" />
    </intent-filter>
</activity>
<activity
    android:name="com.umeng.qq.tencent.AssistActivity"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.Translucent.NoTitleBar"
    android:configChanges="orientation|keyboardHidden|screenSize"/>

<activity
    android:name="com.umeng.socialize.editorpage.ShareActivity"
    android:theme="@style/Theme.UMDefault"
    android:excludeFromRecents="true"
    />
<activity
    android:name=".wxapi.WXEntryActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"
    android:exported="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
```

# 统计功能

```
//BaseActivity里调用:
analysisOnResume(Activity activity)
analysisOnPause(Activity activity)

//多tab或fragment中:
analysisOnPageStart(String  pageName)
analysisOnPageEnd(String  pageName)

//账号统计
onProfileSignIn(String ID)
onProfileSignIn(String provider, String ID)
onProfileSignOff()
```

### menifest中:

```
//application标签内
<meta-data
    android:name="UMENG_APPKEY"
    android:value="${UMENG_APPKEY_VALUE}" />
<meta-data
    android:name="UMENG_CHANNEL"
    android:value="${UMENG_CHANNEL_VALUE}" />
```

### gradle中:

```
//defaultConfig内:
manifestPlaceholders = [UMENG_CHANNEL_VALUE : "androidmarket",//默认的渠道
                        UMENG_APPKEY_VALUE : "你的友盟key"]
                  
//android内:
 productFlavors {
        androidmarket {}
        baidu {}
        xiaomi {}
        zhushou91 {}
        yingyongbao {}
        productFlavors.all { flavor ->
            flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name]
        }
    }
```

# 混淆

```
-keep public class [您的应用包名].R$*{
public static final int *;
}
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
 *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
public static final int *;
    }
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
```

## gradle

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```

**Step 2.** Add the dependency

```
    dependencies {
            compile 'com.github.hss01248:UmengUtil:1.0.0'
    }
```

