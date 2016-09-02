package com.fansfunding.fan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fansfunding.adapter.AddressAdapter;
import com.fansfunding.internal.Address;
import com.fansfunding.internal.SingleAddress;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressActivity extends AppCompatActivity {



    //请求失败
    public static final int FAILURE_RESPONSE = 100;

    //添加地址失败
    public static final int FAILURE_ADD_ADDRESS = 101;

    //添加地址成功
    public static final int ADD_ADDRESS_SUCCESS = 102;

    //编辑地址成功
    public static final int EDIT_ADDRESS_SUCCESS = 103;

    //获取地址成功
    public static final int GET_ADDRESS_SUCCESS = 104;

    //编辑地址失败
    public static final int EDIT_ADDRESS_FAILURE = 105;

    //被编辑的index
    public static int index;

    //当前被操作的address对象
    public static SingleAddress mDataDetial;

    //将要被添加的Address对象
    public static SingleAddress addDataDetial;

    //添加成功
    public static boolean add_success = false;

    private FloatingActionButton add;

    private ListView lv_address;


    public static int MSG_TYPE;

    //编辑操作
    public static final int MSG_EDIT = 100;

    //增添操作
    public static final int MSG_ADD = 101;


    public static final int MSG_CANCEL = 102;


    public static AddressAdapter adapter;

    public static List<SingleAddress> dataDetialList = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FAILURE_RESPONSE:
                    Toast.makeText(AddressActivity.this, "请求失败,可能是网络连接问题哦~~~~", Toast.LENGTH_SHORT).show();
                    break;
                case ADD_ADDRESS_SUCCESS:
                    Toast.makeText(AddressActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    break;
                case FAILURE_ADD_ADDRESS:
                    Toast.makeText(AddressActivity.this, "添加地址失败", Toast.LENGTH_SHORT).show();
                    break;
                case GET_ADDRESS_SUCCESS:

                    adapter.notifyDataSetChanged();
                    break;
                case EDIT_ADDRESS_FAILURE:
                    Toast.makeText(AddressActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
                    break;
                case EDIT_ADDRESS_SUCCESS:
                    Toast.makeText(AddressActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        SharedPreferences share = getSharedPreferences("First getData",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean("isFirst", true);
        editor.commit();

        boolean is = share.getBoolean("isFirst", true);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_address);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setBackgroundColor(getApplicationContext().getResources(R.color.colorAccent));

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("管理收货地址");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);

        //第一次加载界面的时候去get地址数据
        if(is) {
            get();
        }

        //添加收货地址
        add = (FloatingActionButton) findViewById(R.id.fab_address_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent
                MSG_TYPE = MSG_ADD;
                Intent intent = new Intent("android.intent.action.FANS.ADDRESSMANAGEMENT");
                startActivity(intent);
            }
        });
        lv_address=(ListView)findViewById(R.id.lv_address);


        //测试用的FUCK数据~~~~~
//        Address address = new Address();
//        Address.DataDetial dataDetial = address.dataDetial();
//        dataDetial.setName("Fuck");
////        dataDetial.setPhone("Fuck");
//        dataDetial.setAddress("Fuck");
//        dataDetial.setIs_default(0);
////        dataDetial.setPost_code();
//        dataDetialList.add(dataDetial);


        adapter = new AddressAdapter(AddressActivity.this, R.layout.item_address, dataDetialList);

        /*lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG","INItemSelectListeber11");
                Address.DataDetial dataDetial = dataDetialList.get(position);
                mDataDetial = dataDetial;


            }
        });*/
        final Intent intent=getIntent();
        if(intent.getBooleanExtra("needSelect",false)==true) {
            lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("TAG","INItemSelectListeber222");
                    Intent data=new Intent();
                    data.putExtra("address",(SingleAddress)lv_address.getAdapter().getItem(position));
                    setResult(RESULT_OK,data);
                    Log.i("TAG","INItemSelectListeber000");
                    AddressActivity.this.finish();
                    Log.i("TAG","INItemSelectListeber111");
                }
            });
        }else{


        }
        lv_address.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {

        SharedPreferences share = getSharedPreferences("First getData",MODE_PRIVATE);
        boolean is = share.getBoolean("isFirst", true);
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean("isFirst", true);
        editor.commit();
        dataDetialList.clear();
        adapter.notifyDataSetChanged();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        switch (MSG_TYPE) {
            case MSG_ADD:
                if(add_success) {
                    dataDetialList.add(addDataDetial);
                    adapter.notifyDataSetChanged();
                    add_success = false;
                    post(1);
                }
                break;
            case MSG_EDIT:
                dataDetialList.remove(index);
                dataDetialList.add(index, mDataDetial);
                adapter.notifyDataSetChanged();
                edit();
                break;
            case MSG_CANCEL:
                break;
            default:
                break;
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_FOR_ADDRESS:
//                Address.DataDetial dataDetial = (Address.DataDetial) data.getSerializableExtra("addressAdd");
//                mDataDetial = dataDetial;
//                dataDetialList.add(mDataDetial);
//                adapter.notifyDataSetChanged();
//                break;
//            default:
//                break;
//        }
//    }

    private void post(int addressId) {

        //请求需要的id和token
        SharedPreferences share = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        int id = share.getInt("id", 0);
        String token = share.getString("token", "");
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("token",token);
//        requestParams.put("name", addDataDetial.getName());
//        requestParams.put("phone", addDataDetial.getPhone());
//        requestParams.put("province", addDataDetial.getProvince());
//        requestParams.put("city", addDataDetial.getCity());
//        requestParams.put("district",  addDataDetial.getDistrict());
//        requestParams.put("address", addDataDetial.getAddress());
//        requestParams.put("post_code", addDataDetial.getPost_code());
//        requestParams.put("isDefault", addDataDetial.getIs_default());
//
//        asyncHttpClient.post(getString(R.string.url_user) + id + "/shopping_address", requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                if(i == 200) {
//                    Log.i("AddressActivity", "请求成功");
//                    String reponse = null;
//                    try {
//                        reponse = new String(bytes, "gb2312");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("Yes", reponse);
//                    Log.i("AddressActivity", addDataDetial.getProvince());
//                    Log.i("AddressActivity", addDataDetial.getCity());
//                    Log.i("AddressActivity", addDataDetial.getDistrict());
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                if(i == 200) {
//                    String reponse = null;
//                    try {
//                        reponse = new String(bytes, "gb2312");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("failure", reponse);
//                }
//
//            }
//        });
//
//        String province = addDataDetial.getProvince();
//        String city = addDataDetial.getCity();
//        String district = addDataDetial.getDistrict();

        FormBody formBody = new FormBody.Builder()
                .add("token", token)
                .add("name", addDataDetial.getName())

                .add("phone", addDataDetial.getPhone())
                .add("province", addDataDetial.getProvince())
                .add("city", addDataDetial.getCity())
                .add("district", addDataDetial.getDistrict())
                .add("postCode", String.valueOf(addDataDetial.getPost_code()))
                .add("address", addDataDetial.getAddress())
                .build();
        String s = String.valueOf(addDataDetial.getPost_code());
        Log.i("AddressActivity", addDataDetial.getProvince());
        Log.i("AddressActivity", addDataDetial.getCity());
        Log.i("AddressActivity", addDataDetial.getDistrict());
        System.out.print(formBody.toString());

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getString(R.string.url_user) + id + "/shopping_address")
                .post(formBody)
                .build();
        Log.i("AddressActivity", request.headers().toString());
        Log.i("AddressActivity", formBody.toString());
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                handler.sendEmptyMessage(FAILURE_RESPONSE);

                //可能是网络原因导致的问题
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(FAILURE_ADD_ADDRESS);
                    return;
                }

                Gson gson = new GsonBuilder().create();
                Address ads = new Address();
                String str_response = response.body().string();
//

                try {
                    JSONObject jsonObject = new JSONObject(str_response);
                    boolean result = jsonObject.getBoolean("result");
                    int errCode = jsonObject.getInt("errCode");
                    int data = jsonObject.getInt("data");
                    addDataDetial.setAddressId(data);
                    handler.sendEmptyMessage(ADD_ADDRESS_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void get() {
        //请求需要的id和token
        SharedPreferences share = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        int id = share.getInt("id", 0);
        String token = share.getString("token","");
        OkHttpClient httpClient = new OkHttpClient();
        Request request=new Request.Builder()
                .url(getString(R.string.url_user) + id + "/shopping_address" + "?token="+token)
                .get()
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                handler.sendEmptyMessage(FAILURE_RESPONSE);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if(response==null || response.isSuccessful()==false) {
                    handler.sendEmptyMessage(FAILURE_RESPONSE);
                    return;
                }

                Gson gson = new GsonBuilder().create();

                Address ads = new Address();
                String str_response = response.body().string();
//
//                List<Address> addressList = gson.fromJson(str_response, new TypeToken<List<Address>>() {}.getType());
//                for(Address address : addressList) {
//                    Log.d("AddressActivity", address.getData().getProvince());
//                    System.out.print(address.getData().getProvince());
//                }

                try {

                    //用Gson进行解析，并判断结果是否为空
                    if((ads = gson.fromJson(str_response, ads.getClass()))==null){
                        handler.sendEmptyMessage(FAILURE_RESPONSE);
                        return;
                    }

                    if(ads.isResult() == false){
                        handler.sendEmptyMessage(FAILURE_RESPONSE);
                        return;
                    }

                    handler.sendEmptyMessage(GET_ADDRESS_SUCCESS);
                    Log.e("AddAddress", str_response);
                    System.out.print(str_response);
                    //将获取到的数据加载到listview列表中
                    for(SingleAddress detial : ads.getData()) {
                        dataDetialList.add(detial);
                    }


                    //第一次Get数据之后便不再去加载数据
                    SharedPreferences share = getSharedPreferences("First getData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putBoolean("isFirst", false);
                    editor.commit();



                }catch (IllegalStateException e){
                    Message msg=new Message();
                    msg.what = FAILURE_ADD_ADDRESS;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    Message msg=new Message();
                    msg.what = FAILURE_ADD_ADDRESS;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        });
    }

    //编辑
    private void edit() {
        //请求需要的id和token
        SharedPreferences share = getSharedPreferences(getString(R.string.sharepreference_login_by_phone), MODE_PRIVATE);
        int id = share.getInt("id", 0);
        String token = share.getString("token","");
        FormBody formBody = new FormBody.Builder()
                .add("token", token)
                .add("name", mDataDetial.getName())
                .add("phone", mDataDetial.getPhone())
                .add("province", mDataDetial.getProvince())
                .add("city", mDataDetial.getCity())
                .add("district", mDataDetial.getDistrict())
                .add("postCode", String.valueOf(mDataDetial.getPost_code()))
                .add("address", mDataDetial.getAddress())
                .build();
        String s = String.valueOf(mDataDetial.getPost_code());
        Log.i("AddressActivity", mDataDetial.getProvince());
        Log.i("AddressActivity", mDataDetial.getCity());
        Log.i("AddressActivity", mDataDetial.getDistrict());
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getString(R.string.url_user) + id + "/shopping_address/" + mDataDetial.getAddressId())
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                handler.sendEmptyMessage(FAILURE_RESPONSE);

                //可能是网络原因导致的问题
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //服务器响应失败
                if (response == null || response.isSuccessful() == false) {
                    handler.sendEmptyMessage(EDIT_ADDRESS_FAILURE);
                    return;
                }

                Gson gson = new GsonBuilder().create();
                Address ads = new Address();
                String str_response = response.body().string();
                System.out.print(str_response);

                try {
                    //用Gson进行解析，并判断结果是否为空
                    if((ads = gson.fromJson(str_response, ads.getClass()))==null){
                        handler.sendEmptyMessage(EDIT_ADDRESS_FAILURE);
                        return;
                    }
                    if(ads.isResult() == false){
                        handler.sendEmptyMessage(EDIT_ADDRESS_FAILURE);
                        return;
                    }
                    handler.sendEmptyMessage(EDIT_ADDRESS_SUCCESS);
                    Log.e("AddAddress", str_response);
                    System.out.print(str_response);
                }catch (IllegalStateException e){
                    handler.sendEmptyMessage(EDIT_ADDRESS_FAILURE);
                    e.printStackTrace();
                }catch (JsonSyntaxException e){
                    handler.sendEmptyMessage(EDIT_ADDRESS_FAILURE);
                    e.printStackTrace();
                }
            }
        });
    }
}
