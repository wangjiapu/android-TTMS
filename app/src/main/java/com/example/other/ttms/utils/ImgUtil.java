package com.example.other.ttms.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import com.example.other.ttms.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImgUtil {
    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CHOOSE_PHOTO = 2;//选择相册
    public static final int REQUEST_CODE_CAMERA = 3;//相机权限请求
    public static final int REQUEST_CODE_ALBUM = 4;//相册权限请求
    public static Uri imageUri;//相机拍照图片保存地址

    /**
     * 选择图片，从图库、相机
     *
     * @param activity 上下文
     */
    public static void choicePhoto(final Activity activity) {
        //采用的是系统Dialog作为选择弹框
        new AlertDialog.Builder(activity).setTitle("上传头像")//设置对话框标题
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= 23) {//检查相机权限
                            ArrayList<String> permissions = new ArrayList<>();
                            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                permissions.add(Manifest.permission.CAMERA);
                            }

                            if (permissions.size() == 0) {//有权限,跳转
                                //打开相机-兼容7.0
                                openCamera(activity);
                            } else {
                                activity.requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_CAMERA);
                            }
                        } else {
                            //打开相机-兼容7.0
                            openCamera(activity);
                        }
                    }
                }).setNegativeButton("系统相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //如果有权限申请，请在Activity中onRequestPermissionsResult权限返回里面重新调用openAlbum()
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ALBUM);
                } else {
                    openAlbum(activity);
                }
            }
        }).show();//在按键响应事件中显示此对话框
    }

    /**
     * 打开相机
     * 兼容7.0
     *
     * @param activity
     */
    public static void openCamera(Activity activity) {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(activity.getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
            //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
            imageUri = FileProvider.getUriForFile(activity, "briefer.pet.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    public static void openAlbum(Activity activity) {
        //调用系统图库的意图
        Intent choosePicIntent = new Intent(Intent.ACTION_PICK, null);
        choosePicIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(choosePicIntent, CHOOSE_PHOTO);

        //打开系统默认的软件
        //Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //intent.setType("image/*");
        //activity.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    /**
     * 启动裁剪
     *
     * @param activity     上下文
     * @param sourceUir    需要裁剪图片的Uri
     * @param aspectRatioX 裁剪图片宽高比
     * @param aspectRatioY 裁剪图片宽高比
     * @return
     */
    public static void startUCrop(Activity activity, Uri sourceUir, float aspectRatioX, float aspectRatioY) {
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(NetUtil.getDir(), System.currentTimeMillis() + ".jpg"));
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUir, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        //是否能调整裁剪框
        //options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9（设置后就不能选择其他比例了、选择面板就不会出现了）
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        uCrop.start(activity);
    }


    /**
     * 得到byte[]
     * 这里对传入的图片Uri压缩到1M以内，并转换为byte[]后返回
     *
     * @param activity 上下文
     * @param uri      传入图片的Uri
     * @return byte[]
     */
    public static byte[] getImgByteFromUri(Activity activity, Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);//100表示不压缩，直接放到out里面
        int options = 90;//压缩比例
        while (out.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            out.reset(); // 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        byte[] bs = out.toByteArray();

        return bs;
    }
}
