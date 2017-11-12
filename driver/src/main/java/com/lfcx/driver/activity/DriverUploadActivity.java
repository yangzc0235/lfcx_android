package com.lfcx.driver.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverUploadActivity extends DriverBaseActivity {

    public static final String TAG = DriverUploadActivity.class.getSimpleName();

    private Unbinder unbinder;
    @BindView(R2.id.title_bar)
    TextView title_bar;
    /**
     * 身份证正面
     */
    @BindView(R2.id.iv_idcard_front)
    ImageView ivCardFron;

    /**
     * 身份证反面
     */
    @BindView(R2.id.iv_card_after)
    ImageView ivCardAfter;

    /**
     * 手持身份证
     */
    @BindView(R2.id.iv_card_with_hand)
    ImageView ivCardHand;

    /**
     * 行驶证
     */
    @BindView(R2.id.iv_xingshi_main)
    ImageView ivXingshi;
    /**
     * 行驶证副页
     */
    @BindView(R2.id.iv_xingshi_sub)
    ImageView ivXingshiSub;

    /**
     * 驾驶证
     */
    @BindView(R2.id.iv_drive)
    ImageView ivDrive;

    /**
     * 人车合影
     */
    @BindView(R2.id.iv_peo_car)
    ImageView ivPeoCar;

    /**
     * 车辆45度
     */
    @BindView(R2.id.iv_car_tan)
    ImageView ivCarTan;

    /**
     * 车正面照
     */
    @BindView(R2.id.iv_car_front)
    ImageView ivCarFront;

    @BindView(R2.id.iv_baoxian)
    ImageView ivBaoxian;

    private Map<String,File> param = new HashMap<>();
    private DriverCarAPI driverCarAPI;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_upload);
        unbinder = ButterKnife.bind(this);
        init();
    }
    /**
     * 身份证正面
     */
    @OnClick(R2.id.iv_idcard_front)
    public void onClickCardFront(View v){
        pickPic(1);
    }
    /**
     * 身份证饭面
     */
    @OnClick(R2.id.iv_card_after)
    public void onClickCardAfter(View v){
        pickPic(2);
    }
    /**
     * 手持身份证
     */
    @OnClick(R2.id.iv_card_with_hand)
    public void onClickCardHande(View v){
        pickPic(3);
    }
    /**
     * 行驶证
     */
    @OnClick(R2.id.iv_xingshi_main)
    public void onClickXingshi(View v){
        pickPic(4);
    }
    /**
     * 行驶证sub
     */
    @OnClick(R2.id.iv_xingshi_sub)
    public void onClickXingshiSub(View v){
        pickPic(5);
    }

    /**
     * 驾驶证
     */
    @OnClick(R2.id.iv_drive)
    public void onClickDrive(View v){
        pickPic(6);
    }
    /**
     * 人车合影
     */
    @OnClick(R2.id.iv_peo_car)
    public void onClickPeo(View v){
        pickPic(7);
    }

    /**
     * 45度
     */
    @OnClick(R2.id.iv_car_tan)
    public void onClickTan(View v){
        pickPic(8);
    }

    /**
     * 车正面
     */
    @OnClick(R2.id.iv_car_front)
    public void onClickCarFront(View v){
        pickPic(9);
    }
    /**
     * 保险
     */
    @OnClick(R2.id.iv_baoxian)
    public void onClickBaoxian(View v){
        pickPic(10);
    }

    @OnClick(R2.id.d_btn_confirm)
    public void onClickConfrim(){
        uploadPhoto();
    }

    private void init(){
        driverCarAPI = APIFactory.create(DriverCarAPI.class);
        title_bar.setText("司机认证");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverUploadActivity.this.finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void uploadPhoto(){
        if(param.size()<10){
            showToast("请确认全部照片已选择");
            return;
        }
        Map<String,RequestBody> requestParam = new HashMap<>();
        int i=0;
        for(Map.Entry<String,File> entry : param.entrySet()){
            requestParam.put("file"+i+"\"; filename=\""+entry.getKey(),RequestBody.create(MediaType.parse("image/jpeg"),entry.getValue()));
            i++;
        }
        showLoading();
        driverCarAPI.uploadPhoto(requestParam).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                String str = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
                LogUtils.e(TAG,t.getMessage());
            }
        });
    }

    private void pickPic(int requestCode){
        Album.image(this) // 选择图片。
                .singleChoice()
                .requestCode(requestCode)
                .columnCount(3)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        AlbumFile file;
                        Bitmap bitmap;
                        File imageFile;
                        if(null != result && !result.isEmpty()){
                            file = result.get(0);
                            imageFile = new File(file.getThumbPath());
                            bitmap = wrapperImage(file.getThumbPath());
                        }else {return;}
                        switch (requestCode){
                            case 1://身份证正面
                                ivCardFron.setImageBitmap(bitmap);
                                param.put("cardfrontphoto",imageFile);
                                break;
                            case 2://身份证反面
                                ivCardAfter.setImageBitmap(bitmap);
                                param.put("cardbackphoto",imageFile);
                                break;
                            case 3://手持身份证照片
                                ivCardHand.setImageBitmap(bitmap);
                                param.put("cardwithhandphoto",imageFile);
                                break;
                            case 4://行驶证
                                ivXingshi.setImageBitmap(bitmap);
                                param.put("vehiregitcertphoto1",imageFile);
                                break;
                            case 5://行驶证sub
                                ivXingshiSub.setImageBitmap(bitmap);
                                param.put("vehiregitcertphoto2",imageFile);
                                break;
                            case 6://驾驶证
                                ivDrive.setImageBitmap(bitmap);
                                param.put("drivelicephoto",imageFile);
                                break;
                            case 7://人车合影
                                ivPeoCar.setImageBitmap(bitmap);
                                param.put("driverandcarphoto",imageFile);
                                break;
                            case 8://45
                                ivCarTan.setImageBitmap(bitmap);
                                param.put("car45photo",imageFile);
                                break;
                            case 9://车正面
                                ivCarFront.setImageBitmap(bitmap);
                                param.put("carfacephoto",imageFile);
                                break;
                            case 10://保险
                                ivBaoxian.setImageBitmap(bitmap);
                                param.put("Safedocphoto",imageFile);
                                break;
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                    }
                })
                .start();
    }

    private Bitmap wrapperImage(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap dw =  BitmapFactory.decodeFile(filePath);
        return dw;
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
