package com.fansfunding.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fansfunding.fan.AddressActivity;
import com.fansfunding.fan.AddressManagmentAty;
import com.fansfunding.fan.R;
import com.fansfunding.internal.Address;
import com.fansfunding.internal.SingleAddress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by RJzz on 2016/7/18.
 */
public class AddressAdapter extends ArrayAdapter<SingleAddress> {
    //删除成功
    public static final int DELETE_SUCCESS = 100;

    //删除失败
    public static final int DELETE_FAILURE = 101;

    //修改默认地址成功
    public static final int DEFAULT_ADDRESS_SUCCESS = 102;

    //修改默认地址失败
    public static final int DEFAULT_ADDRESS_FAILURE = 103;

    //数据源
    List<SingleAddress> address;

    private int resourceId;
    Context mContext;
    public AddressAdapter(Context context, int resource, List<SingleAddress> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceId = resource;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELETE_FAILURE:
                    Toast.makeText(mContext, "删除失败了", Toast.LENGTH_SHORT).show();
                    break;
                case DELETE_SUCCESS:
                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case DEFAULT_ADDRESS_FAILURE:
                    Toast.makeText(mContext, "修改默认地址失败", Toast.LENGTH_SHORT).show();
                    break;
                case DEFAULT_ADDRESS_SUCCESS:
                    Toast.makeText(mContext, "修改默认地址成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;


            }
            super.handleMessage(msg);
        }
    };

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final SingleAddress dataDetial = getItem(position);

        final View view;
        final ViewHolder viewHolder;
        if(convertView == null) {
            view  = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.tv_address_name);
            viewHolder.phone = (TextView) view.findViewById(R.id.tv_address_phone);
            viewHolder.address = (TextView) view.findViewById(R.id.tv_address_address);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_address_default);
            viewHolder.edit = (Button) view.findViewById(R.id.btn_address_editor);
            viewHolder.delete = (Button) view.findViewById(R.id.btn_address_delete);
            view.setTag(viewHolder);

        }else  {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(dataDetial.getName());
        viewHolder.phone.setText(dataDetial.getPhone());
        viewHolder.address.setText(dataDetial.getProvince()
                + dataDetial.getCity()
                +dataDetial.getDistrict()
                +dataDetial.getAddress());
        if(dataDetial.getIs_default() == 1) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }

//        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////
////                for(Address.DataDetial data : AddressActivity.dataDetialList) {
////                    if(data != dataDetial) {
////                        data.setIs_default(false);
////
////                    }
////
////                }
//
//
//                if(isChecked) {
//
//                } else {
//                    viewHolder.checkBox.setChecked(false);
//                    dataDetial.setIs_default(false);
//                }
//            }
//        });
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(true);
                    dataDetial.setIs_default(1);
                    edit(AddressActivity.dataDetialList.get(position));
                    for(int i  = 0; i < AddressActivity.dataDetialList.size(); i++) {
                        if(i != position) {
                            AddressActivity.dataDetialList.get(i).setIs_default(0);
                        }
                    }
                } else {
                    viewHolder.checkBox.setChecked(false);
                    dataDetial.setIs_default(0);
                }
//                for(Address.DataDetial dataDetial1 : AddressActivity.dataDetialList) {
//                    edit(dataDetial);
//                }
                AddressActivity.adapter.notifyDataSetChanged();
            }
        });

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, AddressManagmentAty.class);
//                Bundle extras = new Bundle();
//                extras.putSerializable("addressEdit", AddressActivity.mDataDetial);
//                intent.putExtras(extras);
                AddressActivity.MSG_TYPE = AddressActivity.MSG_EDIT;
                AddressActivity.mDataDetial = dataDetial;
                mContext.startActivity(intent);
                AddressActivity.index = position;
                System.out.print(position);

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("是否删除");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(AddressActivity.dataDetialList.get(position), position);
                        AddressActivity.dataDetialList.remove(position);
                        AddressActivity.adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
        return view;
    }


    class ViewHolder {
        TextView name;
        TextView phone;
        TextView address;
        CheckBox checkBox;
        Button edit;
        Button delete;
    }

    private void delete(SingleAddress dataDetail, final int postion) {

        final SharedPreferences share = mContext.getSharedPreferences(mContext.getString(R.string.sharepreference_login_by_phone), mContext.MODE_PRIVATE);
        int id = share.getInt("id", 0);
        String token = share.getString("token", "");

        FormBody formBody = new FormBody.Builder()
                .add("token", token)
                .build();
//        String s = String.valueOf(addDataDetial.getPost_code());
//        Log.i("AddressActivity", addDataDetial.getProvince());
//        Log.i("AddressActivity", addDataDetial.getCity());
//        Log.i("AddressActivity", addDataDetial.getDistrict());

//        a.delete(mContext.getString(R.string.url_user) + id + "/shopping_address/" + dataDetial.getAddressId(), requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                try {
//                    String reponse = new String(bytes, "gb2312");
//                    System.out.print(reponse);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//        });
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mContext.getString(R.string.url_user) + id + "/shopping_address/" + dataDetail.getAddressId() + "/delete")
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(DELETE_FAILURE);


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(DELETE_FAILURE);
                    return;
                }

                String str_response  = response.body().string();
                System.out.print(str_response);
                System.out.print(response.body().contentType());
                try {
                    JSONObject jsonObject = new JSONObject(str_response);
                    boolean result = jsonObject.getBoolean("result");
                    int errCode = jsonObject.getInt("errCode");
                    String data = jsonObject.getString("data");

                    if(result && errCode == 200 && data.equals("删除成功")) {
                        handler.sendEmptyMessage(DELETE_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //修改默认地址
    private void edit(SingleAddress dataDetial) {
        //请求需要的id和token
        SharedPreferences share = mContext.getSharedPreferences(mContext.getString(R.string.sharepreference_login_by_phone), mContext.MODE_PRIVATE);
        int id = share.getInt("id", 0);
        String token = share.getString("token","");
        FormBody formBody = new FormBody.Builder()
                .add("token", token)
                .add("addressId", String.valueOf(dataDetial.getAddressId()))
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mContext.getString(R.string.url_user) + id + "/shopping_address/default")
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求失败
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(DEFAULT_ADDRESS_FAILURE);
                    return;
                }

                String s_response =  response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(s_response);
                    boolean result = jsonObject.getBoolean("result");
                    int errCode = jsonObject.getInt("errCode");
                    String data = jsonObject.getString("data");

                    if(result && errCode == 200 && data.equals("设置成功")) {
                        handler.sendEmptyMessage(DEFAULT_ADDRESS_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
