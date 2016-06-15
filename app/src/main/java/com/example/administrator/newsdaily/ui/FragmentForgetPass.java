package com.example.administrator.newsdaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.model.biz.UserManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserUser;
import com.example.administrator.newsdaily.model.entity.BaseEntity;
import com.example.administrator.newsdaily.model.entity.Register;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

public class FragmentForgetPass extends Fragment {
	/** 邮箱编辑框 */
	private EditText editEmail;
	/** 确认按钮 */
	private Button btnCommit;
	/** 用户管理器 */
	private UserManager userManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forgetpass, container,
				false);
		editEmail = (EditText) view.findViewById(R.id.edit_email);
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		btnCommit.setOnClickListener(listener);
		return view;
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 点击确认按钮，向服务器发送忘记密码请求
			if (arg0.getId() == R.id.btn_commit) {
				String email = editEmail.getText().toString();
				if (!CommonUtil.verifyEmail(email)) {
					Toast.makeText(getActivity(), "请求输入正确的邮箱格式ʽ",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (userManager == null)
					userManager = UserManager.getInstance(getActivity());
				userManager.forgetPass(getActivity(),
						new VolleyResponseHandler(), new VolleyErrorHandler(),
						CommonUtil.VERSION_CODE + "", email);

			}
		}
	};

	class VolleyResponseHandler implements Listener<String> {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			LogUtil.d(LogUtil.TAG, "执行忘记密码操作，返回信息：" + response);
			BaseEntity<Register> register = ParserUser.parserRegister(response);
			int status = Integer.parseInt(register.getStatus());
			String result = "";
			if (status == 0) {
				Register entity = register.getData();
				result = entity.getExplain();
				if (entity.getResult().trim().equals("0")) {
					((ActivityMain) getActivity()).showFragmentLogin();
					//增加动画=======
					getActivity().overridePendingTransition(
							R.anim.anim_activity_right_in,
							R.anim.anim_activity_bottom_out);
				} else if (entity.getResult().trim().equals("-2")) {
					editEmail.requestFocus();
				}
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	class VolleyErrorHandler implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "请求失败！", Toast.LENGTH_SHORT).show();
		}

	}
}
