package com.qxinli.umeng;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public interface ShareCallback {
    void onResult(SHARE_MEDIA var1);

    void onError(SHARE_MEDIA var1, Throwable var2);

    void onCancel(SHARE_MEDIA var1);
}
