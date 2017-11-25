package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lfcx.driver.R;
import com.lfcx.driver.adapter.MenuDialogAdapter;
import com.lfcx.driver.adapter.MenuDialogTypeAdapter;
import com.lfcx.driver.adapter.MyPagerAdapter;
import com.lfcx.driver.adapter.MyViewPager;
import com.lfcx.driver.net.result.CarnameEntity;
import com.lfcx.driver.net.result.NameEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarNameSelectActivity extends DriverBaseActivity {
    private List<CarnameEntity> mCarnameEntities = new ArrayList<>();
    private MyViewPager mViewPager;
    private View view1, view2, view3;
    private ListView mListView1, mListView2, mListView3;
    private List<View> views = new ArrayList<View>();
    private MenuDialogAdapter mListView1Adapter;
    private MenuDialogTypeAdapter mListView2Adapter;
    private CarnameEntity carnameEntity;
    private NameEntity mNameEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_name_select);
        initViews();
        getJsonData();

    }


    //操作控件
    private void initViews() {
        //一级
        mViewPager = (MyViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.pager_number, null);
        view2 = inflater.inflate(R.layout.pager_number, null);
        view3 = inflater.inflate(R.layout.pager_number, null);
        mListView1 = (ListView) view1.findViewById(R.id.listview);
        mListView2 = (ListView) view2.findViewById(R.id.listview);
        views.add(view1);
        views.add(view2);//加载了一二级菜单
        mViewPager.setAdapter(new MyPagerAdapter(views));




    }

    /**
     * 获取json的数据
     */
    private void getJsonData() {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(this.getAssets().open(
                    "totalCar.json"), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            List<String> cars = null;
            JSONArray jsonArray = null;
            JSONObject jsonObject = null;
            try {
                jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    String carmodels = (String) jsonObject.get("carmodels");
                    JSONArray jsonArray1 = (JSONArray) jsonObject.get("cartypes");
                    cars = new ArrayList<>();
                    for (int j = 0; j < jsonArray1.length(); j++){
                        cars.add(jsonArray1.get(j).toString());
                    }
                    Log.v("system--code-->", carmodels);

                    carnameEntity = new CarnameEntity();
                    carnameEntity.setCarmodels(carmodels);
                    carnameEntity.setCartypes(cars);
                    mCarnameEntities.add(carnameEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mListView1Adapter = new MenuDialogAdapter(this, mCarnameEntities);
            mListView1Adapter.setSelectedBackgroundResource(R.color.blue);//选中时
            mListView1Adapter.setHasDivider(false);
            mListView1Adapter.setNormalBackgroundResource(R.color.white);//未选中
            mListView1.setAdapter(mListView1Adapter);
            mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListView1Adapter != null)
                        mListView1Adapter.setSelectedPos(position);
                    if (mListView2Adapter != null)
                        mListView2Adapter.setSelectedPos(-1);
                    CarnameEntity carnameEntity = (CarnameEntity) parent.getItemAtPosition(position);
                    if (mListView2Adapter == null) {
                        mListView2Adapter = new MenuDialogTypeAdapter(CarNameSelectActivity.this,carnameEntity.getCartypes());
                        mListView2Adapter.setNormalBackgroundResource(R.color.white);
                        mListView2.setAdapter(mListView2Adapter);
                    } else {
                        mListView2Adapter.setData(carnameEntity.getCartypes());
                        mListView2Adapter.notifyDataSetChanged();
                    }
                    mNameEntity=new NameEntity();
                    String carmodels = carnameEntity.getCarmodels();
                    mNameEntity.setCarmodels(carmodels);

                }
            });

            mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String type = (String) parent.getItemAtPosition(position);
                    Log.v("system--type------->",type);
                    mNameEntity.setType(type);
                    setResultDate(mNameEntity);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //传递值
    private void setResultDate(NameEntity nameEntity){
        Intent intent=new Intent();
        intent.putExtra("menu",(Serializable)nameEntity);
        Log.v("system-------->",nameEntity.getCarmodels());
        Log.v("system---type----->",nameEntity.getType());
        setResult(RESULT_OK, intent);
        finish();
    }
}
