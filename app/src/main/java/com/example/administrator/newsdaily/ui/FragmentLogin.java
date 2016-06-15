package com.example.administrator.newsdaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.common.SharedPreferencesUtils;
import com.example.administrator.newsdaily.model.biz.UserManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserUser;
import com.example.administrator.newsdaily.model.entity.BaseEntity;
import com.example.administrator.newsdaily.model.entity.Register;
import com.example.administrator.newsdaily.model.httpclient.ResponseHandlerInterface;
import com.example.administrator.newsdaily.model.httpclient.TextHttpResponseHandler;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

import org.apache.http.Header;

/** 登陆界面 **/
public class FragmentLogin extends Fragment {
	private View view;
	private EditText editTextNickname, editTextPwd;
	private Button but_register, btn_login, btn_forgetPass;
	private UserManager userManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		初始化界面
		view = inflater.inflate(R.layout.fragment_login, container, false);
		editTextNickname = (EditText) view.findViewById(R.id.editText_nickname);
		editTextPwd = (EditText) view.findViewById(R.id.editText_pwd);
		but_register = (Button) view.findViewById(R.id.button_register);
		btn_forgetPass = (Button) view.findViewById(R.id.button_forgetPass);
		btn_login = (Button) view.findViewById(R.id.button_login);
//		设置点击事件
		but_register.setOnClickListener(clickListener);
		btn_forgetPass.setOnClickListener(clickListener);
		btn_login.setOnClickListener(clickListener);
		return view;
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_login:
				String name = editTextNickname.getText().toString().trim();
				String pwd = editTextPwd.getText().toString().trim();
//				判断用户名,密码信息是否正确
				if(TextUtils.isEmpty(name)){
					Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(pwd.length() < 6 || pwd.length()  > 16 ){
					Toast.makeText(getActivity(), "密码长度错误", Toast.LENGTH_SHORT).show();
					return ;
				}
//			将账号,密码信息添加到UserManager
				if (userManager == null)
					userManager = UserManager.getInstance(getActivity());
				userManager.login(getActivity(), listener, errorListener,
						CommonUtil.VERSION_CODE + "", name, pwd, "0");
				break;
			case R.id.button_register:
				((ActivityMain) getActivity()).showFragmentRegister();
				break;
			case R.id.button_forgetPass:
				((ActivityMain) getActivity()).showFragmentForgetPass();
				break;
			}

		}
	};

	public Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub

			LogUtil.d(LogUtil.TAG, "执行登陆操作，返回信息：" + response);
			BaseEntity<Register> register = ParserUser.parserRegister(response);
			int status = Integer.parseInt(register.getStatus());
			String result = "";
			if (status == 0) {
				result = "登陆成功";
				SharedPreferencesUtils.saveRegister(getActivity(), register);
				startActivity(new Intent(getActivity(), ActivityUser.class));
				// 增加动画=======
				getActivity().overridePendingTransition(
						R.anim.anim_activity_right_in,
						R.anim.anim_activity_bottom_out);
			} else if (status == -3) {
				result = "用户名或密码错误";
			} else {
				result = "登陆失败";
			}
			Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

		}
	};

	ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "登陆异常", Toast.LENGTH_SHORT).show();
		}
	};

	private ResponseHandlerInterface loginResponseHandler = new TextHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				String responseString) {

			if (statusCode == 200) {
				LogUtil.d(LogUtil.TAG, "?执行登陆操作，返回信息：" + responseString);
				BaseEntity<Register> register = ParserUser
						.parserRegister(responseString);
				int status = Integer.parseInt(register.getStatus());
				String result = "";
				if (status == 0) {
					result = "登陆成功";
//					保存注册信息
					SharedPreferencesUtils
							.saveRegister(getActivity(), register);
					startActivity(new Intent(getActivity(), ActivityUser.class));
					getActivity().overridePendingTransition(
							R.anim.anim_activity_right_in,
							R.anim.anim_activity_bottom_out);
				} else if (status == -3) {
					result = "用户名或密码错误";
				} else {
					result = "登陆失败";
				}
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
						.show();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			Toast.makeText(getActivity(), "登录异常！", Toast.LENGTH_SHORT).show();
		}
	};
}
