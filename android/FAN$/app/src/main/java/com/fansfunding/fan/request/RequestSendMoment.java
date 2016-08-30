package com.fansfunding.fan.request;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.utils.BitmapUtils;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.FeedbackCode;
import com.fansfunding.internal.social.SendMomentPicture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 13616 on 2016/8/25.
 */
public class RequestSendMoment {

    private SendMomentPicture sendMomentPicture;

    public SendMomentPicture getSendMomentPicture(){
        return sendMomentPicture;
    }

    public void requestSendMomentPicture(Activity context, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final String token, List<PhotoInfo> photoInfoList){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(PhotoInfo p:photoInfoList){
            Log.i("TAG",p.getPhotoPath());
            File file = new File(p.getPhotoPath());
            Bitmap bm = BitmapUtils.getimage(p.getPhotoPath());
            file=BitmapUtils.saveMyBitmap(bm,file.getName());

            RequestBody requestBody = FormBody.create(MediaType.parse("image/jpeg"), file);
            builder.addFormDataPart("files",file.getName(),requestBody);
        }
        /*builder.addFormDataPart("userId",String.valueOf(userId));
        builder.addFormDataPart("token",token);*/

        RequestBody requestBody = builder.build();
        Request request=new Request.Builder()
                .post(requestBody)
                .url(context.getString(R.string.url_userbasic)+userId+"/moment/images")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                Log.i("TAG","PICTURE"+str_response);
                sendMomentPicture=new SendMomentPicture();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((sendMomentPicture = gson.fromJson(str_response, sendMomentPicture.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE);
                        return;
                    }

                    //发送用户动态的图片失败
                    if(sendMomentPicture.isResult()==false){
                        if(handler.handlerFanErrorMessage(sendMomentPicture.getErrCode())==false){
                            handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE);
                        }
                        return;
                    }
                    //发送用户动态的图片成功
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_PICTURE_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }

    public void requestSendMoment(Activity context, final ErrorHandler handler, OkHttpClient httpClient, final int userId, final String token,final String content,final int origin,final int linkCategory,final int linkProject,final String images){

        FormBody.Builder builder=new FormBody.Builder()
                .add("token",token)
                .add("content",content);
        if(linkCategory>0&&linkProject>0){
            builder.add("linkCategory",String.valueOf(linkCategory))
                    .add("linkProject",String.valueOf(linkProject));
        }
        if(images!=null&&images.equals("")==false){
            builder.add("images",images);
        }
        if(origin!=-1){
            builder.add("origin",String.valueOf(origin));
        }
        FormBody formbody=builder.build();
        Request request=new Request.Builder()
                .post(formbody)
                .url(context.getString(R.string.url_user)+userId+"/moment")
                .build();
        Call call=httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null||response.isSuccessful()==false){
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_FAILURE);
                    return;
                }
                Gson gson=new GsonBuilder().create();
                String str_response=response.body().string();
                FeedbackCode feedbackCode=new FeedbackCode();
                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((feedbackCode = gson.fromJson(str_response, feedbackCode.getClass()))==null){
                        handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_FAILURE);
                        return;
                    }

                    //发送用户动态失败
                    if(feedbackCode.isResult()==false){
                        if(handler.handlerFanErrorMessage(feedbackCode.getErrCode())==false){
                            handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_FAILURE);
                        }
                        return;
                    }
                    //发送用户动态成功
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_SUCCESS);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(FANRequestCode.SEND_USER_MOMENT_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
