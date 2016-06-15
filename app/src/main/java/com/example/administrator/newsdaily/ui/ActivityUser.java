package com.example.administrator.newsdaily.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LoadImage;
import com.example.administrator.newsdaily.common.LoadImage.ImageLoadListener;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.common.SharedPreferencesUtils;
import com.example.administrator.newsdaily.common.SystemUtils;
import com.example.administrator.newsdaily.model.biz.UserManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserUser;
import com.example.administrator.newsdaily.model.entity.BaseEntity;
import com.example.administrator.newsdaily.model.entity.Register;
import com.example.administrator.newsdaily.model.entity.User;
import com.example.administrator.newsdaily.model.httpclient.ResponseHandlerInterface;
import com.example.administrator.newsdaily.model.httpclient.TextHttpResponseHandler;
import com.example.administrator.newsdaily.ui.adapter.LoginLogAdapter;
import com.example.administrator.newsdaily.ui.base.MyBaseActivity;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * �û�����
 **/
public class ActivityUser extends MyBaseActivity implements ImageLoadListener {
    private LinearLayout layout;
    private ImageView    imageView, imageView_back;
    private TextView textView, integralTextView, commentTextView;
    private ListView          logListview;
    private SharedPreferences sharedPreferences;
    private PopupWindow       popupWindow;
    private Bitmap            bitmap, alterBitmap;
    private File            file;
    private LoadImage       loadImage;
    private LoginLogAdapter adapter;
    private Button          btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        layout = (LinearLayout) findViewById(R.id.layout);
        imageView = (ImageView) findViewById(R.id.icon);
        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        textView = (TextView) findViewById(R.id.name);
        integralTextView = (TextView) findViewById(R.id.integral);
        commentTextView = (TextView) findViewById(R.id.comment_count);
        logListview = (ListView) findViewById(R.id.list);
        btn_exit = (Button) findViewById(R.id.btn_exit);
//        Ϊ��¼��־���������
        adapter = new LoginLogAdapter(this, new ArrayList());
        logListview.setAdapter(adapter);
        //��һ�������������ģ��ڶ���������ImageLoadListener�Ľӿ�ʵ�ֶ���
        //����ͼƬ����LoadImage����
        loadImage = new LoadImage(this, this);
        initData();
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        textView.setText(sharedPreferences.getString("uname", "Bin:"));
        String localpath = sharedPreferences.getString("localpic", null);
        if (localpath != null) {
            bitmap = BitmapFactory.decodeFile(localpath);
            imageView.setImageBitmap(bitmap);
        }
        imageView.setOnClickListener(onClickListener);
        btn_exit.setOnClickListener(onClickListener);
        imageView_back.setOnClickListener(onClickListener);
        initpopupwindow();
    }

    private void initpopupwindow() {
        View contentView = getLayoutInflater().inflate(
                R.layout.item_pop_selectpic, null);
        //����popupwindow��ͼ
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        ���þ۽�
        popupWindow.setFocusable(true);
        LinearLayout photo_take = (LinearLayout) contentView
                .findViewById(R.id.photo_take);
        LinearLayout photo_sel = (LinearLayout) contentView
                .findViewById(R.id.photo_sel);
        photo_take.setOnClickListener(onClickListener);
        photo_sel.setOnClickListener(onClickListener);
    }

    /**
     * �����û���������
     */
    private void initData() {
        String token = SharedPreferencesUtils.getToken(this);
        UserManager.getInstance(this).getUserInfo(this, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                LogUtil.d("�����û����ķ����ַ���", response);
                BaseEntity<User> user = ParserUser.parserUser(response);
                if (Integer.parseInt(user.getStatus()) != 0) {
                    Toast.makeText(ActivityUser.this, "�����û�����ʧ��", Toast.LENGTH_SHORT).show();
                    return;
                }

                //�����û����ݵ����� �� �û��ǳ� ,�û�ͷ���ַ?
                SharedPreferencesUtils.saveUser(ActivityUser.this, user);
                //��ʾ���ݸ���UI
                User userCore = user.getData();
                textView.setText(userCore.getUid());
                //���»��֡�������
                integralTextView.setText("����:" + userCore.getIntegration());
                commentTextView.setText(userCore.getComnum() + "");
                //���µ�¼��¼����
                adapter.appendData(userCore.getLoginlog(), true);
                adapter.update();
                //��ȡ�û�ͷ���ַ?
                String portrait = userCore.getPortrait();
                if (!TextUtils.isEmpty(portrait)) {
                    //�˷����ڲ������жϻ������Ƿ���ͼƬ�������򷵻ء�
                    //�����жϱ����ļ��Ƿ���ͼƬ���ڣ������򷵻أ�
                    //��֮���������������ݣ�����ص������imageLoadOk()
                    loadImage.geBitmap(portrait, imageView);

                }

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        }, CommonUtil.VERSION_CODE + "", token, SystemUtils.getIMEI(this));
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon: //����û�ͷ�񣬵ײ�����popupwindow
                    popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.imageView_back:
                    startActivity(new Intent(ActivityUser.this, ActivityMain.class));
                    finish();
                    break;
                case R.id.photo_take: //����
                    popupWindow.dismiss();
                    takePhoto();
                    break;
                case R.id.photo_sel: //�����ѡ��
                    popupWindow.dismiss();
                    selectPhoto();
                    break;
                case R.id.btn_exit://�˳���¼
                    SharedPreferencesUtils.clearUser(ActivityUser.this);
                    startActivity(new Intent(ActivityUser.this, ActivityMain.class));
                    finish();
                    break;
            }
        }
    };

    /**
     * ��ת��ϵͳ�����չ���
     */
    protected void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    /***
     * ��ת��ϵͳ��Ƭ��ѡ����Ƭ
     */
    protected void selectPhoto() {
        final Intent intent = getPhotoPickIntent();
        startActivityForResult(intent, 200);
    }

    /***
     * ��װ����Gallery��intent
     */
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");//���òü�����
        intent.putExtra("aspectX", 1); //��߱���
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80); //���ֵ
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true); //���زü����
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                save(bitmap); // �����û�ѡ���ͼƬ
            }
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                bitmap = data.getParcelableExtra("data");
                save(bitmap); // �����û�ѡ���ͼƬ
            }

        }
    }

    /**
     * �����û��ϴ���ͼƬ
     **/
    private void save(Bitmap bitmap) {
        if (bitmap == null)
            return;
        roundPic();
        File dir = new File(Environment.getExternalStorageDirectory(),
                "azynews");
        dir.mkdirs();
        file = new File(dir, "userpic.jpg");
        try {
            OutputStream stream = new FileOutputStream(file);
            if (alterBitmap.compress(CompressFormat.PNG, 100, stream)) {
                //�ϴ�ͼƬ
                UserManager.getInstance(this).changePhoto(this, SharedPreferencesUtils.getToken(this), file,
                        listener, errorListener);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ü�ͼƬ
     */
    private void roundPic() {
        Bitmap backBp = BitmapFactory.decodeResource(getResources(),
                R.drawable.userbg);
        alterBitmap = Bitmap.createBitmap(backBp.getWidth(),
                backBp.getHeight(), backBp.getConfig());
        Canvas canvas = new Canvas(alterBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(backBp, new Matrix(), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        bitmap = Bitmap.createScaledBitmap(bitmap, backBp.getWidth(),
                backBp.getHeight(), true);
        canvas.drawBitmap(bitmap, new Matrix(), paint);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            startActivity(new Intent(this, ActivityMain.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Listener<String> listener = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            // TODO Auto-generated method stub
            System.out.println("�ϴ�ͷ�񷵻���Ϣ--->" + response);
            BaseEntity<Register> entity = ParserUser.parserUploadImage(response);
            if (entity.getData().getResult().equals("0")) {
                //�����û�ͷ�񱾵ص�·��
                SharedPreferencesUtils.saveUserLocalIcon(ActivityUser.this, file.getAbsolutePath());
                imageView.setImageBitmap(alterBitmap);
            }
        }
    };

    private ErrorListener errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub
            System.out.println("�ϴ�ͷ�񷵻���Ϣ--->" + error.getMessage());
        }
    };

    private ResponseHandlerInterface picResponseHandler = new TextHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              String responseString) {

            LogUtil.d(LogUtil.TAG, "�ϴ�ͷ�񷵻���Ϣ--->" + responseString);
            BaseEntity<Register> entity = ParserUser.parserUploadImage(responseString);
            if (entity.getData().getResult().equals("0")) {
                //�����û�ͷ���·��
                SharedPreferencesUtils.saveUserLocalIcon(ActivityUser.this, file.getAbsolutePath());
                imageView.setImageBitmap(alterBitmap);
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              String responseString, Throwable throwable) {
            LogUtil.d(LogUtil.TAG, "�ϴ��û�ͷ��ʧ��---" + responseString);
        }
    };

    //�˷�����ImageLoadListener�ӿڵ�ʵ�ַ������������������ص�����bitmap����
    @Override
    public void imageLoadOk(Bitmap bitmap, String url) {
        LogUtil.d(LogUtil.TAG, "onlistener...load iamge..." + bitmap + "---url=" + url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
