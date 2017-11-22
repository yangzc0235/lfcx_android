package com.lfcx.driver.activity;

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
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String mMobile="15901126195";

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
//        if(param.size()<10){
//            showToast("请确认全部照片已选择");
//            return;
//        }
        Map<String,RequestBody> requestParam = new HashMap<>();
        int i=0;
        for(Map.Entry<String,File> entry : param.entrySet()){
            requestParam.put("file"+i+"\"; filename=\""+entry.getKey(),RequestBody.create(MediaType.parse("application/octet-stream"),entry.getValue()));
            Log.v("system---图片集合----->",requestParam.toString());
            Log.v("system-getKey()----->",entry.getKey());
            File value = entry.getValue();
            try {
                long fileSize = getFileSize(value);
                Log.v("system---文件大小------->",fileSize+"");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Log.v("system-value----->",entry.getValue().toString());
            Log.v("system-value----->",RequestBody.create(MediaType.parse("multipart/form-data"),entry.getValue()).toString());
            i++;
        }
        driverCarAPI.uploadPhoto(requestParam).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String str = response.body();
                BaseResultBean baseResultBean=new Gson().fromJson(response.body(),BaseResultBean.class);
                if(baseResultBean.getCode().equals("0")){
                    Log.v("system---认证信息------>",str);
                    Toast.makeText(DriverUploadActivity.this, baseResultBean.getMsg(), Toast.LENGTH_SHORT).show();
                    goToActivity(DriverLoginActivity.class);
                    finish();
                }else {
                    Toast.makeText(DriverUploadActivity.this, baseResultBean.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
                LogUtils.e(TAG,t.getMessage());
            }
        });
    }

    /**
     * 获取指定文件大小(单位：字节)
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
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
                            //cardfrontphoto，cardbackphoto，cardwithhandphoto，vehiregitcertphoto1，vehiregitcertphoto2，drivelicephoto，driverandcarphoto，car45photo，carfacephoto，safedocphoto  名称是 手机号_
                            case 1://身份证正面
                                ivCardFron.setImageBitmap(bitmap);
                                param.put(mMobile+"_cardfrontphoto"+".jpg",imageFile);
                                break;
                            case 2://身份证反面
                                ivCardAfter.setImageBitmap(bitmap);
                                param.put(mMobile+"_cardbackphoto"+".jpg",imageFile);
                                break;
                            case 3://手持身份证照片
                                ivCardHand.setImageBitmap(bitmap);
                                param.put(mMobile+"_cardwithhandphoto"+".jpg",imageFile);
                                break;
                            case 4://行驶证
                                ivXingshi.setImageBitmap(bitmap);
                                param.put(mMobile+"_vehiregitcertphoto1"+".jpg",imageFile);
                                break;
                            case 5://行驶证sub
                                ivXingshiSub.setImageBitmap(bitmap);
                                param.put(mMobile+"_vehiregitcertphoto2"+".jpg",imageFile);
                                break;
                            case 6://驾驶证
                                ivDrive.setImageBitmap(bitmap);
                                param.put(mMobile+"_drivelicephoto"+".jpg",imageFile);
                                break;
                            case 7://人车合影
                                ivPeoCar.setImageBitmap(bitmap);
                                param.put(mMobile+"_driverandcarphoto"+".jpg",imageFile);
                                break;
                            case 8://45
                                ivCarTan.setImageBitmap(bitmap);
                                param.put(mMobile+"_car45photo"+".jpg",imageFile);
                                break;
                            case 9://车正面
                                ivCarFront.setImageBitmap(bitmap);
                                param.put(mMobile+"_carfacephoto"+".jpg",imageFile);
                                break;
                            case 10://保险
                                ivBaoxian.setImageBitmap(bitmap);
                                param.put(mMobile+"_safedocphoto"+".jpg",imageFile);
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

    private String[] uploadFileByHttp(String srcPath) {
        String uploadUrl = "upload/uploadPhoto";
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";

        String[] ret = null;
        while(ret == null)
        {
            try {
                URL url = new URL(uploadUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url
                        .openConnection();
                // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
                // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
                httpURLConnection.setChunkedStreamingMode(2048 * 1024);// 2M
                // 允许输入输出流
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                // 使用POST方法
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);

                httpURLConnection.connect();
                DataOutputStream dos = new DataOutputStream(
                        httpURLConnection.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; name=\"doc\"; filename=\""
                        + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                        + "\""
                        + end);
                dos.writeBytes(end);

                FileInputStream fis = new FileInputStream(srcPath);
                byte[] buffer = new byte[8192]; // 8k
                int count = 0;
                // 读取文件
                int sum = 0;
                while ((count = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, count);
                    sum += count;
                }
                System.out.println(sum);
                fis.close();

                dos.writeBytes(end);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                dos.flush();

                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String result = br.readLine();

                System.out.println(result);

                dos.close();
                is.close();
                //httpURLConnection.disconnect();

                return result.split(";");

            } catch (Exception e) {
                e.printStackTrace();
                setTitle(e.getMessage());
            }
        }
        return null;
    }
}
