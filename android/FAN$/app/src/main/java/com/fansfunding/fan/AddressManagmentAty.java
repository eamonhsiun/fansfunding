package com.fansfunding.fan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.fansfunding.internal.Address;

import com.fansfunding.pick.AssetsUtils;



import java.io.IOException;
import java.util.ArrayList;

import cn.qqtheme.framework.picker.AddressPicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by RJzz on 2016/7/19.
 */
public class AddressManagmentAty extends AppCompatActivity {

    private boolean district_change = false;

    //新增对象
    Address adsfuck  = new Address();

    Address.DataDetial dataDetialFuck = adsfuck.dataDetial();

    //姓名
    private TextInputEditText nameT;

    //电话
    private TextInputEditText phoneT;

    //地区弹出
    private TextView district;

    //详细地址
    private TextInputEditText address;

    //邮编
    private TextInputEditText postcode;

    //取消按钮
    private ImageButton btn_cancel;

    //确认按钮
    private ImageButton btn_confirm;

    //标题栏
    private TextView title;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);
        AddressActivity.add_success = false;

        nameT = (TextInputEditText) findViewById(R.id.tite_address_name);
        phoneT = (TextInputEditText) findViewById(R.id.tite_address_phone);
        address = (TextInputEditText) findViewById(R.id.tite_address);
        postcode = (TextInputEditText) findViewById(R.id.tite_postcode);
        btn_cancel = (ImageButton) findViewById(R.id.btn_cancel);
        btn_confirm = (ImageButton) findViewById(R.id.btn_confirm);
        district = (TextView) findViewById(R.id.tv_address_district);
        title = (TextView) findViewById(R.id.tv_address_info);

        //地址选择器
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_area(v);
            }
        });

        switch (AddressActivity.MSG_TYPE) {
            case AddressActivity.MSG_ADD:
                Log.i("AddressManagment", "增加地址");
                title.setText("编辑地址");
                break;
            case AddressActivity.MSG_EDIT:
                Log.i("AddressManagment", "编辑地址");
                title.setText("修改地址");
                nameT.setText(AddressActivity.mDataDetial.getName());
                phoneT.setText(AddressActivity.mDataDetial.getPhone());
                district.setText(AddressActivity.mDataDetial.getProvince()
                        + AddressActivity.mDataDetial.getCity()
                        + AddressActivity.mDataDetial.getDistrict());
                address.setText(AddressActivity.mDataDetial.getAddress());
                postcode.setText(AddressActivity.mDataDetial.getPost_code() + "");
                break;
            default:
                Log.i("AddressManagment", "default");

                break;
        }

        //提交
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (AddressActivity.MSG_TYPE) {
                    case AddressActivity.MSG_ADD:
                        Log.i("AddressManagment", "添加成功返回");
                        break;
                    case AddressActivity.MSG_EDIT:
                        Log.i("AddressManagment", "编辑完成返回");
                        break;
                }
                commit();
            }
        });


        //取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void pick_area(View view) {

        try {
            final ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
            String json = AssetsUtils.readText(this, "city.json");
            data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
            AddressPicker picker = new AddressPicker(this, data);
        switch (AddressActivity.MSG_TYPE) {
            case AddressActivity.MSG_ADD:
                picker.setSelectedItem("北京市", "北京市", "东城区");
                break;
            case AddressActivity.MSG_EDIT:
                picker.setSelectedItem(AddressActivity.mDataDetial.getProvince(),
                        AddressActivity.mDataDetial.getCity(),
                        AddressActivity.mDataDetial.getDistrict());

                    break;
            default:
                break;

        }

            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(AddressPicker.Province province, AddressPicker.City city, AddressPicker.County county) {
                    district.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    dataDetialFuck.setProvince(province.getAreaName());
                    dataDetialFuck.setCity(city.getAreaName());
                    dataDetialFuck.setDistrict(county.getAreaName());
                    district_change = true;
                }

            });
            picker.show();
        } catch (Exception e) {
            Log.i("AddressManagment", e.getMessage().toString());
        }
        //
    }

    //增加地址
    public void add() {



    }


    //修改地址
    public void edit() {
        Intent intent = getIntent();
        Address.DataDetial dataDetial_edit = (Address.DataDetial) intent.getSerializableExtra("addressEdit");

        nameT.setText(dataDetialFuck.getName());
        phoneT.setText(dataDetial_edit.getPhone());
        district.setText(dataDetial_edit.getProvince() + dataDetial_edit.getCity() + dataDetial_edit.getDistrict());
        address.setText(dataDetial_edit.getAddress());
        postcode.setText(dataDetial_edit.getPost_code());


    }
    private void commit() {

        if(!nameT.getText().toString().equals("") &&
                !phoneT.getText().toString().equals("") &&
                !district.getText().toString().equals("") &&
                !address.getText().toString().equals("") &&
                !postcode.getText().toString().equals("")
                )  {

            String str_data = postcode.getText().toString();
            if("".equals(str_data)){
                str_data = "0";
            }
            int postcode = Integer.parseInt(str_data);
            dataDetialFuck.setPost_code(postcode);
            dataDetialFuck.setName(nameT.getText().toString());
            dataDetialFuck.setAddress(address.getText().toString());
            dataDetialFuck.setPhone(phoneT.getText().toString());

//            Intent intent = new Intent();
//            intent.setClass(AddressManagmentAty.this, AddressActivity.class);
//            Bundle extras = new Bundle();
            switch (AddressActivity.MSG_TYPE) {
                case AddressActivity.MSG_ADD:
//                    extras.putSerializable("addressAdd", dataDetial);
                    //将需要添加的对象改变值
                    dataDetialFuck.setAddress(address.getText().toString());
                    AddressActivity.addDataDetial = dataDetialFuck;
                    AddressActivity.add_success = true;
//                    ost();p
                    break;
                case AddressActivity.MSG_EDIT:
                    //将当前的对象改变
//                    extras.putSerializable("addressEdit", dataDetial);
//                    dataDetial.setProvince(AddressActivity.mDataDetial.getProvince());
//                    dataDetial.setCity(AddressActivity.mDataDetial.getCity());
//                    dataDetial.setDistrict(AddressActivity.mDataDetial.getDistrict());


                    if(!district.getText().toString().isEmpty()) {
                        //如果地址改变了之后，重新赋值，不然不改变
                        if(district_change) {
                            AddressActivity.mDataDetial.setProvince(dataDetialFuck.getProvince());
                            AddressActivity.mDataDetial.setCity(dataDetialFuck.getCity());
                            AddressActivity.mDataDetial.setDistrict(dataDetialFuck.getDistrict());
                        }

                    }
                    AddressActivity.mDataDetial.setName(dataDetialFuck.getName());
                    AddressActivity.mDataDetial.setPhone(dataDetialFuck.getPhone());
                    AddressActivity.mDataDetial.setPost_code(dataDetialFuck.getPost_code());
                    AddressActivity.mDataDetial.setAddress(dataDetialFuck.getAddress());
                    break;
                default:
                    break;
            }
            //Intent intent = new Intent(AddressManagmentAty.this, AddressActivity.class);
            //startActivity(intent);
            this.finish();

        }else {
            Toast.makeText(AddressManagmentAty.this, "请输入完整的收获信息", Toast.LENGTH_LONG).show();
        }


    }



}
