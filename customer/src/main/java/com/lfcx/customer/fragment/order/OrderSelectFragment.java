package com.lfcx.customer.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.CustomerOrderActivity;
import com.lfcx.customer.maphelper.InputTipTask;
import com.lfcx.customer.maphelper.PoiSearchTask;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RecomandAdapter;
import com.lfcx.customer.maphelper.RouteTask;

/**
 *
 */

public class OrderSelectFragment extends Fragment implements View.OnClickListener,TextWatcher,AdapterView.OnItemClickListener {

    private ListView mRecommendList;

    private ImageView mBack_Image;

    private TextView mSearchText;

    private EditText mDestinaionText;

    private RecomandAdapter mRecomandAdapter;

    private RouteTask mRouteTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_order_select_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mRecommendList=(ListView) getActivity().findViewById(R.id.recommend_list);
        mBack_Image=(ImageView) getActivity().findViewById(R.id.destination_back);
        mBack_Image.setOnClickListener(this);

        mSearchText=(TextView) getActivity().findViewById(R.id.destination_search);
        mSearchText.setOnClickListener(this);

        mDestinaionText=(EditText) getActivity().findViewById(R.id.destination_edittext);
        mDestinaionText.addTextChangedListener(this);
        mRecomandAdapter=new RecomandAdapter(getActivity());
        mRecommendList.setAdapter(mRecomandAdapter);
        mRecommendList.setOnItemClickListener(this);

        mRouteTask= RouteTask.getInstance(getActivity());
        //初始值
        mDestinaionText.setText("北京");
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
        if(RouteTask.getInstance(getActivity()).getStartPoint()==null){
            Toast.makeText(getActivity(), "检查网络，Key等问题", Toast.LENGTH_SHORT).show();
            return;
        }
        InputTipTask.getInstance(mRecomandAdapter).searchTips(getActivity(),s.toString(),
                RouteTask.getInstance(getActivity()).getStartPoint().city);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R2.id.destination_back:
                ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.BUILD);
                break;
            case R2.id.destination_search:
                PoiSearchTask poiSearchTask=new PoiSearchTask(getActivity(), mRecomandAdapter);
                poiSearchTask.search(mDestinaionText.getText().toString(), RouteTask.getInstance(getActivity()).getStartPoint().city);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long id) {

        PositionEntity entity = (PositionEntity) mRecomandAdapter.getItem(position);
        if (entity.latitue == 0 && entity.longitude == 0) {
            PoiSearchTask poiSearchTask=new PoiSearchTask(getActivity(), mRecomandAdapter);
            poiSearchTask.search(entity.address, RouteTask.getInstance(getActivity()).getStartPoint().city);

        } else {
            mRouteTask.setEndPoint(entity);
            mRouteTask.search();
            ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.BUILD);
        }
    }
}
