package com.example.administrator.newsdaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.SharedPreferencesUtils;
import com.example.administrator.newsdaily.model.biz.UserManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserUser;
import com.example.administrator.newsdaily.model.entity.BaseEntity;
import com.example.administrator.newsdaily.model.entity.Register;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

/**
 * 注册界面
 **/
public class FragmentRegister extends Fragment {
    private View     view;
    private EditText editTextEmail, editTextName, editTextPwd;
    private Button      but_register;
    private CheckBox    checkBox;
    private UserManager userManager;
    private String      email, name, pwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        editTextEmail = (EditText) view.findViewById(R.id.editText_email);
        editTextName = (EditText) view.findViewById(R.id.editText_name);
        editTextPwd = (EditText) view.findViewById(R.id.editText_pwd);
        but_register = (Button) view.findViewById(R.id.button_register);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox1);

        but_register.setOnClickListener(clickListener);

        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!checkBox.isChecked()) {
                Toast.makeText(getActivity(), "没有同意协议条款！", Toast.LENGTH_SHORT).show();
                return;
            }
            String email = editTextEmail.getText().toString();
            String name = editTextName.getText().toString().trim();
            String pwd = editTextPwd.getText().toString().trim();
            if (!CommonUtil.verifyEmail(email)) {
                Toast.makeText(getActivity(), "请求输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getActivity(), "请输入用户昵称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pwd.length() < 6 || pwd.length() > 16) {
                Toast.makeText(getActivity(), "密码长度错误", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!CommonUtil.verifyPassword(pwd)) {
                Toast.makeText(getActivity(), "请输入6-22位数字和字母组合的密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userManager == null)
                userManager = UserManager.getInstance(getActivity());
            // ver : 版本 uid : 用户昵称 pwd : 密码 email : 邮箱 device : 手机IMEI号
            // userManager.register(new MyResponseHandlerInterface(),
            // CommonUtil.VERSION_CODE+"",name,pwd ,email);
            userManager.register(getActivity(), listener, errorListener,
                    CommonUtil.VERSION_CODE + "", name, pwd, email);
        }
    };
    ErrorListener    errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub
            Toast.makeText(getActivity(), "错误信息", Toast.LENGTH_SHORT).show();
        }
    };
    Listener<String> listener      = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            // TODO Auto-generated method stub

            BaseEntity<Register> register = ParserUser
                    .parserRegister(response);
            String result = null;
            String explain = null;
            Register data = register.getData();
            int status = Integer.parseInt(register.getStatus());
            if (status == 0) {
                result = data.getResult().trim();
                explain = data.getExplain();
                result = "注册成功";
                if (result.equals("0")) {
                    //保存用户信息
                    SharedPreferencesUtils.saveRegister(getActivity(), register);
                    startActivity(new Intent(getActivity(), ActivityUser.class));
                    //增加动画=======
                    getActivity().overridePendingTransition(R.anim.anim_activity_right_in, R.anim.anim_activity_bottom_out);
                    //更新界面
                    ((ActivityMain) getActivity()).changeFragmentUser();
                }
                Toast.makeText(getActivity(), "欢迎登录", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    };

}
