package com.lfcx.driver.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.driver.R;
import com.lfcx.driver.maphelper.PoiSearchTask;
import com.lfcx.driver.maphelper.PositionEntity;
import com.lfcx.driver.maphelper.RecomandAdapter;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.util.EdtUtil;


/**
 * author: drawthink
 * date  : 2017/7/28
 * des   :  输入目标地
 */
public class DestinationActivity extends DriverBaseActivity implements View.OnClickListener,TextWatcher,AdapterView.OnItemClickListener {

    private ListView mRecommendList;

    private ImageView mBack_Image;

    private TextView mSearchText;

    private EditText mDestinaionText;

    private RecomandAdapter mRecomandAdapter;

    private RouteTask mRouteTask;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_order_select_fragment);
        mRecommendList=(ListView) findViewById(R.id.recommend_list);
        mBack_Image=(ImageView) findViewById(R.id.destination_back);
        mBack_Image.setOnClickListener(this);

        mSearchText=(TextView) findViewById(R.id.destination_search);
        mSearchText.setOnClickListener(this);

        mDestinaionText=(EditText) findViewById(R.id.destination_edittext);
        mDestinaionText.addTextChangedListener(this);
        mRecomandAdapter=new RecomandAdapter(getApplicationContext());
        mRecommendList.setAdapter(mRecomandAdapter);
        mRecommendList.setOnItemClickListener(this);

        mRouteTask= RouteTask.getInstance(getApplicationContext());
    }

    @Override
    public void afterTextChanged(Editable arg0) {

        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if(RouteTask.getInstance(getApplicationContext()).getStartPoint()==null){
//            Toast.makeText(getApplicationContext(), "检查网络，Key等问题", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        InputTipTask.getInstance(mRecomandAdapter).searchTips(getApplicationContext(),s.toString(),
//                RouteTask.getInstance(getApplicationContext()).getStartPoint().city);
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.destination_back) {
            finish();

        } else if (i == R.id.destination_search) {
            if (EdtUtil.isEdtEmpty(mDestinaionText)) {
                Toast.makeText(this, "请输入目的地", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                PoiSearchTask poiSearchTask = new PoiSearchTask(getApplicationContext(), mRecomandAdapter);
//                if(RouteTask.getInstance(getApplicationContext()).getStartPoint().city==null){
//
//                    return;
//                }
//                poiSearchTask.search(mDestinaionText.getText().toString(), );
                Log.v("system----distance---->",mDestinaionText.getText().toString());
                Log.v("system----city---->",RouteTask.getInstance(getApplicationContext()).getStartPoint().city+"");
                poiSearchTask.search(mDestinaionText.getText().toString(), mDestinaionText.getText().toString());
            }catch (Exception e){
                Toast.makeText(this, "获取位置失败,请稍后再试", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long id) {

        PositionEntity entity = (PositionEntity) mRecomandAdapter.getItem(position);
        if (entity.latitue == 0 && entity.longitude == 0) {
            PoiSearchTask poiSearchTask=new PoiSearchTask(getApplicationContext(), mRecomandAdapter);
            poiSearchTask.search(entity.address, RouteTask.getInstance(getApplicationContext()).getStartPoint().city);
        } else {
            entity.setAddress(mDestinaionText.getText().toString());
            mRouteTask.setEndPoint(entity);
            mRouteTask.search();
//            Intent intent = new Intent(DestinationActivity.this, CustomerOrderActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
            finish();
        }
    }

}

