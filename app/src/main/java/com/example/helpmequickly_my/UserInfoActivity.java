package com.example.helpmequickly_my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.UserInfo;
import com.example.popup.PhotoPopupWindow;
import com.example.utils.InfoPrefs;
import com.example.utils.PictureUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserInfoActivity";
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_CHANGE_USER_NICK_NAME = 10;
    private static final String IMAGE_FILE_NAME = "user_head_icon.jpg";

    PhotoPopupWindow mPhotoPopupWindow;
    TextView textView_user_nick_name;
    CircleImageView circleImageView_user_head;

    // SyncStateContract.Constants.UserInfo.HEAD_IMAGE ???
    // TODO 需要将uri和path进行改变
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information);

        textView_user_nick_name = findViewById(R.id.user_nick_name);
        circleImageView_user_head = findViewById(R.id.user_head_image);
        //InfoPrefs自己封装的一个 SharedPreferences 工具类
        //init()指定文件名，getData(String key)获取key对应的字符串，getIntData(int key)获取key对应的int
        InfoPrefs.init("user_info");
        refresh();

        LinearLayout LinearLayout_user_nick_name = findViewById(R.id.user_nick_nm);
        LinearLayout_user_nick_name.setOnClickListener(this);

        LinearLayout LinearLayout_user_head = findViewById(R.id.user_head_img);
        LinearLayout_user_head.setOnClickListener(this);
    }

    public void refresh(){
        //textView_user_nick_name.setText(InfoPrefs.getData(SyncStateContract.Constants.UserInfo.NAME));
        showHeadImage();
        //circleImageView_user_head.setImageURI();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.user_head_img:
                //创建存放头像的文件夹
                PictureUtil.mkdirMyPetRootDirectory();
                mPhotoPopupWindow = new PhotoPopupWindow(UserInfoActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 文件权限申请
                        if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限还没有授予，进行申请
                            ActivityCompat.requestPermissions(UserInfoActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
                        } else {
                            // 如果权限已经申请过，直接进行图片选择
                            mPhotoPopupWindow.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            // 判断系统中是否有处理该 Intent 的 Activity
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, REQUEST_IMAGE_GET);
                            } else {
                                Toast.makeText(UserInfoActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v){
                        // 拍照及文件权限申请
                        if (ContextCompat.checkSelfPermission(UserInfoActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(UserInfoActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限还没有授予，进行申请
                            ActivityCompat.requestPermissions(UserInfoActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300); // 申请的 requestCode 为 300
                        } else {
                            // 权限已经申请，直接拍照
                            mPhotoPopupWindow.dismiss();
                            imageCapture();
                        }
                    }
                });
                View rootView = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.personal_information, null);
                mPhotoPopupWindow.showAtLocation(rootView,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

//            case R.id.user_nick_nm:
//                ChangeInfoBean bean = new ChangeInfoBean();
//                bean.setTitle("修改昵称");
//                bean.setInfo(InfoPrefs.getData(SyncStateContract.Constants.UserInfo.NAME));
//                Intent intent = new Intent(UserInfoActivity.this,ChangeInfoActivity.class);
//                intent.putExtra("data", bean);
//                startActivityForResult(intent,REQUEST_CHANGE_USER_NICK_NAME);
//                break;
            default:
        }
    }

    //以下为头像相关方法
    private void showHeadImage() {
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
        if (isSdCardExist) {

            String localIconNormal = "user_image.png";
            FileInputStream localStream = null;
            try {
                localStream = openFileInput(localIconNormal);
                Bitmap bitmap = BitmapFactory.decodeStream(localStream);
                circleImageView_user_head.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                circleImageView_user_head.setImageResource(R.mipmap.user_image);
                Log.e(TAG, "no file");
            }



//            String path = InfoPrefs.getData(UserInfo.USER_HEAD_IMAGE);// 获取图片路径
//            Log.e(TAG, "path: "+ path);
//            //SyncStateContract.Constants._COUNT
//            File file = new File(path);
//
//            Uri uri =Uri.parse(path);
//            if (file.exists()) {
//                Bitmap bm = BitmapFactory.decodeFile(path);
//                // 将图片显示到ImageView中
//                circleImageView_user_head.setImageBitmap(bm);
//
//
//            }else{
//                Log.e(TAG,"no file");
//                circleImageView_user_head.setImageResource(R.mipmap.user_image);
//            }
        } else {
            Log.e(TAG,"no SD card");
            circleImageView_user_head.setImageResource(R.mipmap.user_image);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                // 切割
                case REQUEST_SMALL_IMAGE_CUTTING:
                    Log.e(TAG, "before show");
                    File cropFile = new File(PictureUtil.getMyPetRootDirectory(), "crop.jpg");
                    Uri cropUri = Uri.fromFile(cropFile);
                    setPicToView(cropUri);
                    break;

                // 相册选取
                case REQUEST_IMAGE_GET:
                    Log.d(TAG, "onActivityResult: " + "获取照片了！");
                    Uri uri = PictureUtil.getImageUri(this, data);
                    Log.e(TAG, "uri: "+uri.toString());
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        circleImageView_user_head.setImageBitmap(bitmap);
                         String imgPath = uri.getPath();
                        Log.e(TAG, "IMGPath: "+imgPath );

                        String str1 = "user_image.png";
                        FileOutputStream localFileOutputStream1 = openFileOutput(str1, 0);
                        Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.PNG;
                        bitmap.compress(localCompressFormat, 100, localFileOutputStream1);
                        localFileOutputStream1.close();

                        InfoPrefs.setData(UserInfo.USER_HEAD_IMAGE, uri.toString());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //startPhotoZoom(uri);
                    break;

                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File pictureFile = new File(PictureUtil.getMyPetRootDirectory(), IMAGE_FILE_NAME);
                    Uri pictureUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictureUri = FileProvider.getUriForFile(this,
                                "com.example.mypet.fileprovider", pictureFile);
                        Log.e(TAG, "picURI=" + pictureUri.toString());
                    } else {
                        pictureUri = Uri.fromFile(pictureFile);
                    }
                    startPhotoZoom(pictureUri);
                    break;
                // 获取changeinfo销毁 后 回传的数据
                case REQUEST_CHANGE_USER_NICK_NAME:
                    String returnData = data.getStringExtra("data_return");
                    InfoPrefs.setData(UserInfo.USER_NAME, returnData);
                    textView_user_nick_name.setText(InfoPrefs.getData(UserInfo.USER_NAME));
                    break;
                default:
            }
        } else {
            Log.e(TAG, "result = " + resultCode + ",request = " + requestCode);
        }
    }

    private void startPhotoZoom(Uri uri) {
        Log.d(TAG,"Uri = "+uri.toString());
        //保存裁剪后的图片
        File cropFile=new File(PictureUtil.getMyPetRootDirectory(),"crop.jpg");
        try{
            if(cropFile.exists()){
                cropFile.delete();
                Log.e(TAG,"delete");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Uri cropUri;
        cropUri = Uri.fromFile(cropFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);

        // Log.e(TAG,"cropUri = "+cropUri.toString());
        InfoPrefs.setData(UserInfo.USER_HEAD_IMAGE, cropUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    private void imageCapture() {
        Intent intent;
        Uri pictureUri;
        //getMyPetRootDirectory()得到的是Environment.getExternalStorageDirectory() + File.separator+"MyPet"
        //也就是我之前创建的存放头像的文件夹（目录）
        File pictureFile = new File(PictureUtil.getMyPetRootDirectory(), IMAGE_FILE_NAME);
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //这一句非常重要
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
            pictureUri = FileProvider.getUriForFile(this,
                    "com.example.mypet.fileprovider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照,拍照的结果存到oictureUri对应的路径中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        Log.e(TAG,"before take photo"+pictureUri.toString());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void setPicToView(Uri uri)  {
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            // 创建 smallIcon 文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //String storage = Environment.getExternalStorageDirectory().getPath();
                File dirFile = new File(PictureUtil.getMyPetRootDirectory(),  "Icon");
                if (!dirFile.exists()) {
                    if (!dirFile.mkdirs()) {
                        Log.d(TAG, "in setPicToView->文件夹创建失败");
                    } else {
                        Log.d(TAG, "in setPicToView->文件夹创建成功");
                    }
                }
                File file = new File(dirFile, IMAGE_FILE_NAME);
                InfoPrefs.setData(UserInfo.USER_HEAD_IMAGE,file.getPath());
                //Log.d("result",file.getPath());
                // Log.d("result",file.getAbsolutePath());
                // 保存图片
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 在视图中显示图片
            showHeadImage();
            //circleImageView_user_head.setImageBitmap(InfoPrefs.getData(Constants.UserInfo.GEAD_IMAGE));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPopupWindow.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(UserInfoActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPhotoPopupWindow.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPopupWindow.dismiss();
                    imageCapture();
                } else {
                    mPhotoPopupWindow.dismiss();
                }
                break;
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                //Toast.makeText(UserInfoActivity.this,"you press back!",Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void save(String inputText){
        FileOutputStream outputStream = null;
        BufferedWriter writer =  null;

        try {
            outputStream = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public void storePic(String tabid, String key, Bitmap bitmap) {
//        //LogUtils.LOGD(TAG, "storePic begin tabid = " + tabid + "key = " + key);
//        if(tabid == null || key == null || tabid.isEmpty() || key.isEmpty() || bitmap == null) {
//            return;
//        }
//        FileOutputStream fos = null;
//        try {
//            fos = openFileOutput(tabid + "_" + key, Context.MODE_PRIVATE);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (FileNotFoundException e) {
//            //LogUtils.LOGE(TAG, "storePic FileNotFoundException e = " +e);
//        } finally {
//            if(fos != null) {
//                try {
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                    //LogUtils.LOGE(TAG, "storePic IOException e = " +e);
//                }
//            }
//        }
//    }
//
//    public Bitmap getStorePic(String tabid, String key) {
//        //LogUtils.LOGD(TAG, "getStorePic begin tabid = " + tabid + "key = " + key);
//        if(tabid == null || key == null || tabid.isEmpty() || key.isEmpty()) {
//            return null;
//        }
//        FileInputStream fin = null;
//        Bitmap bitmap = null;
//        try {
//            fin = openFileInput(tabid + "_" + key);
//            bitmap = BitmapFactory.decodeStream(fin);
//        } catch (FileNotFoundException e) {
//            //LogUtils.LOGE(TAG, "getStorePic FileNotFoundException e = " + e);
//        }
//        return bitmap;
//    }

}


