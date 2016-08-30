package com.fansfunding.internal.social;

import java.util.List;

/**
 * Created by 13616 on 2016/8/25.
 */
public class SendMomentPicture {

    /**
     * result : true
     * errCode : 200
     * data : ["user/moment/10000023/3CC75FF45EE1FEEBBFB0EF4198DE814B1468949398201.jpeg","user/moment/10000023/A50189CF1C4405CEEC637639CFA856A71468949405267.jpeg","user/moment/10000023/34A32FF898B51AD2D38136EC690807401468949410941.jpeg"]
     */

    private boolean result;
    private int errCode;
    private List<String> data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
