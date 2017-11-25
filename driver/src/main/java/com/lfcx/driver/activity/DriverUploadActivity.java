package com.lfcx.driver.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.net.result.LoginResult;
import com.squareup.okhttp.RequestBody;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    private Map<String, File> param = new HashMap<>();
    private DriverCarAPI driverCarAPI;
    private ImageView iv_back;
    private String mMobile = "110";
    private DUserAPI userAPI;
    private String mPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_upload);
        unbinder = ButterKnife.bind(this);
        userAPI = APIFactory.create(DUserAPI.class);
        init();
    }

    /**
     * 身份证正面
     */
    @OnClick(R2.id.iv_idcard_front)
    public void onClickCardFront(View v) {
        pickPic(1);
    }

    /**
     * 身份证饭面
     */
    @OnClick(R2.id.iv_card_after)
    public void onClickCardAfter(View v) {
        pickPic(2);
    }

    /**
     * 手持身份证
     */
    @OnClick(R2.id.iv_card_with_hand)
    public void onClickCardHande(View v) {
        pickPic(3);
    }

    /**
     * 行驶证
     */
    @OnClick(R2.id.iv_xingshi_main)
    public void onClickXingshi(View v) {
        pickPic(4);
    }

    /**
     * 行驶证sub
     */
    @OnClick(R2.id.iv_xingshi_sub)
    public void onClickXingshiSub(View v) {
        pickPic(5);
    }

    /**
     * 驾驶证
     */
    @OnClick(R2.id.iv_drive)
    public void onClickDrive(View v) {
        pickPic(6);
    }

    /**
     * 人车合影
     */
    @OnClick(R2.id.iv_peo_car)
    public void onClickPeo(View v) {
        pickPic(7);
    }

    /**
     * 45度
     */
    @OnClick(R2.id.iv_car_tan)
    public void onClickTan(View v) {
        pickPic(8);
    }

    /**
     * 车正面
     */
    @OnClick(R2.id.iv_car_front)
    public void onClickCarFront(View v) {
        pickPic(9);
    }

    /**
     * 保险
     */
    @OnClick(R2.id.iv_baoxian)
    public void onClickBaoxian(View v) {
        pickPic(10);
    }

    @OnClick(R2.id.d_btn_confirm)
    public void onClickConfrim() {
        uploadPhoto();
    }

    private void init() {
        driverCarAPI = APIFactory.create(DriverCarAPI.class);
        title_bar.setText("司机认证");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverUploadActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(BUNDLE_KEY);
        if (bundleExtra != null) {
            mMobile = bundleExtra.getString("mobile");
            mPwd = bundleExtra.getString("pwd");
            Log.v("system-mobile----->", mMobile);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void uploadPhoto() {
        if (param.size() < 10) {
            showToast("请确认全部照片已选择");
            return;
        }
        Map<String, RequestBody> requestParam = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, File> entry : param.entrySet()) {
            requestUploadHeadimg("file" + i, entry.getValue(), entry.getKey());
        }
    }

    /**
     * 上传头像到服务器
     *
     * @param file 上传的文件
     */
    private void requestUploadHeadimg(String name, File file, String fileName) {
        showLoading();
        OkHttpUtils.post()//
                .addFile(name, fileName, file)//
                .url(NetConfig.UPLOAD_PHOTO_OK)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Toast.makeText(DriverUploadActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideLoading();
                        BaseResultBean baseResultBean = new Gson().fromJson(response, BaseResultBean.class);
                        if (baseResultBean.getCode().equals("0")) {
                            Log.v("system---认证信息------>", response);
                            try {
                                goLogin(mMobile, mPwd);
                            } catch (Exception e) {
                                Toast.makeText(DriverUploadActivity.this, "获取注册信息失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DriverUploadActivity.this, baseResultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    /**
     * 登录
     *
     * @param moible
     * @param pwd
     */
    private void goLogin(final String moible, final String pwd) {
        Map<String, String> param = new HashMap<>();
        param.put("user", moible);
        param.put("pwd", pwd);
        param.put("isdriver", "1");
        showLoading();
        userAPI.customerLogin(param).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                hideLoading();
                LoginResult result = null;
                try {
                    result = response.body();
                    Log.v("system------->", response.body().toString());
                    if ("0".equals(result.getCode())) {
                        SPUtils.setParam(DriverUploadActivity.this, SPConstants.KEY_DRIVER_PK_USER, result.getPk_user());
                        SPUtils.setParam(DriverUploadActivity.this, SPConstants.DRIVER_MOBILE, moible);
                        SPUtils.setParam(DriverUploadActivity.this, SPConstants.KEY_DRIVER_PWD, pwd);
                        goToActivity(DriverMainActivity.class);
                        finish();
                    } else {
                        showToast(result.getMsg());
                    }

                } catch (Exception e) {
                    LogUtils.e(DriverUploadActivity.this.getClass().getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                hideLoading();
                LogUtils.e(DriverUploadActivity.this.getClass().getSimpleName(), t.getMessage());
                showToast("网络错误，请稍后再试!");
            }
        });
    }


    /**
     * 选择图片
     *
     * @param requestCode
     */
    private void pickPic(int requestCode) {
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
                        if (null != result && !result.isEmpty()) {
                            file = result.get(0);
                            imageFile = new File(file.getThumbPath());
                            bitmap = wrapperImage(file.getThumbPath());
                        } else {
                            return;
                        }
                        switch (requestCode) {
                            //cardfrontphoto，cardbackphoto，cardwithhandphoto，vehiregitcertphoto1，vehiregitcertphoto2，drivelicephoto，driverandcarphoto，car45photo，carfacephoto，safedocphoto  名称是 手机号_
                            case 1://身份证正面
                                ivCardFron.setImageBitmap(bitmap);

                                param.put(mMobile + "_cardfrontphoto.jpg", imageFile);
                                break;
                            case 2://身份证反面
                                ivCardAfter.setImageBitmap(bitmap);
                                param.put(mMobile + "_cardbackphoto.jpg", imageFile);
                                break;
                            case 3://手持身份证照片
                                ivCardHand.setImageBitmap(bitmap);
                                param.put(mMobile + "_cardwithhandphoto.jpg", imageFile);
                                break;
                            case 4://行驶证
                                ivXingshi.setImageBitmap(bitmap);
                                param.put(mMobile + "_vehiregitcertphoto1.jpg", imageFile);
                                break;
                            case 5://行驶证sub
                                ivXingshiSub.setImageBitmap(bitmap);
                                param.put(mMobile + "_vehiregitcertphoto2.jpg", imageFile);
                                break;
                            case 6://驾驶证
                                ivDrive.setImageBitmap(bitmap);
                                param.put(mMobile + "_drivelicephoto.jpg", imageFile);
                                break;
                            case 7://人车合影
                                ivPeoCar.setImageBitmap(bitmap);
                                param.put(mMobile + "_driverandcarphoto.jpg", imageFile);
                                break;
                            case 8://45
                                ivCarTan.setImageBitmap(bitmap);
                                param.put(mMobile + "_car45photo.jpg", imageFile);
                                break;
                            case 9://车正面
                                ivCarFront.setImageBitmap(bitmap);
                                param.put(mMobile + "_carfacephoto.jpg", imageFile);
                                break;
                            case 10://保险
                                ivBaoxian.setImageBitmap(bitmap);
                                param.put(mMobile + "_safedocphoto.jpg", imageFile);
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

    private Bitmap wrapperImage(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap dw = BitmapFactory.decodeFile(filePath);
        return dw;
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


}
