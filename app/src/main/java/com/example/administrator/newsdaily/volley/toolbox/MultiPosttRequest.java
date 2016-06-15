package com.example.administrator.newsdaily.volley.toolbox;

import com.example.administrator.newsdaily.volley.AuthFailureError;
import com.example.administrator.newsdaily.volley.NetworkResponse;
import com.example.administrator.newsdaily.volley.Request;
import com.example.administrator.newsdaily.volley.Response;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MultiPosttRequest extends Request<String> {

	private MultipartEntity entity = new MultipartEntity();
	private final Listener<String> mListener;

	public MultiPosttRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		// TODO Auto-generated constructor stub
		mListener = listener;
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param key
	 *            :����
	 * @param mFilePart
	 *            :�ļ�
	 */
	public void buildMultipartEntity(String key, File mFilePart) {
		entity.addFilePart(key, mFilePart);
	}
	
	/**
	 * �ϴ�String
	 * 
	 * @param key
	 *            :����
	 * @param mFilePart
	 *            :�ļ�
	 */
	public void buildMultipartEntity(String key, String value) {
		entity.addStringPart(key, value);
	}

	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			e.getStackTrace();
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		return Response.success("Uploaded", getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

}
