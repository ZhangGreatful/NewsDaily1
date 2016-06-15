package com.example.administrator.newsdaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;

/**左边侧拉界面**/
public class FragmentMenu extends Fragment{
	private View view;
	private RelativeLayout[] rls=new RelativeLayout[5];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_menu_left, container, false);
		rls[0]=(RelativeLayout) view.findViewById(R.id.rl_news);
		rls[1]=(RelativeLayout) view.findViewById(R.id.rl_reading);
		rls[2]=(RelativeLayout) view.findViewById(R.id.rl_local);
		rls[3]=(RelativeLayout) view.findViewById(R.id.rl_commnet);
		rls[4]=(RelativeLayout) view.findViewById(R.id.rl_photo);
		for(int i=0;i<rls.length;i++){
			rls[i].setOnClickListener(onClickListener);
		}
		return view;
	}
	
	private OnClickListener onClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			for (int i = 0; i < rls.length; i++) {
				rls[i].setBackgroundColor(0);
			}
			switch (v.getId()) {
			case R.id.rl_news:
				rls[0].setBackgroundColor(0x33c85555);
				((ActivityMain)getActivity()).showFragmentMain();
				break;
			case R.id.rl_reading:
				rls[1].setBackgroundColor(0x33c85555);
				((ActivityMain)getActivity()).showFragmentFavorite();
				break;
			case R.id.rl_local:
				rls[2].setBackgroundColor(0x33c85555);
				Toast.makeText(getActivity(), "Local", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rl_commnet:
				rls[3].setBackgroundColor(0x33c85555);
				Toast.makeText(getActivity(), "Commnet", Toast.LENGTH_SHORT).show();
				break;
			case R.id.rl_photo:
				rls[4].setBackgroundColor(0x33c85555);
				Toast.makeText(getActivity(), "Photo", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}
