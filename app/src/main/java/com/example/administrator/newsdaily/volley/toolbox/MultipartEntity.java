package com.example.administrator.newsdaily.volley.toolbox;

import android.text.TextUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class MultipartEntity implements HttpEntity {
	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();
	/**
	 * ���з�
	 */
	private final String NEW_LINE_STR = "\r\n";
	private final String CONTENT_TYPE = "Content-Type: ";
	private final String CONTENT_DISPOSITION = "Content-Disposition: ";
	/**
	 * �ı��������ַ���
	 */
	private final String TYPE_TEXT_CHARSET = "text/plain; charset=UTF-8";

	/**
	 * �ֽ�������
	 */
	private final String TYPE_OCTET_STREAM = "application/octet-stream";
	/**
	 * �����Ʋ���
	 */
	private final byte[] BINARY_ENCODING = "Content-Transfer-Encoding: binary\r\n\r\n"
			.getBytes();
	/**
	 * �ı�����
	 */
	private final byte[] BIT_ENCODING = "Content-Transfer-Encoding: 8bit\r\n\r\n"
			.getBytes();

	/**
	 * �ָ���
	 */
	private String mBoundary = null;
	/**
	 * �����
	 */
	ByteArrayOutputStream mOutputStream = new ByteArrayOutputStream();

	public MultipartEntity() {
		this.mBoundary = generateBoundary();
	}

	 /**
     * ���ɷָ���
     * 
     * @return
     */
    private final String generateBoundary() {
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buf.toString();
    }
 
    /**
     * ������ͷ�ķָ���
     * 
     * @throws IOException
     */
    private void writeFirstBoundary() throws IOException {
        mOutputStream.write(("--" + mBoundary + "\r\n").getBytes());
    }
 
    /**
     * ����ı�����
     * 
     * @param key
     * @param value
     */
    public void addStringPart(final String paramName, final String value) {
        writeToOutputStream(paramName, value.getBytes(), TYPE_TEXT_CHARSET, BIT_ENCODING, "");
    }
 
    /**
     * ������д�뵽�������
     * 
     * @param key
     * @param rawData
     * @param type
     * @param encodingBytes
     * @param fileName
     */
    private void writeToOutputStream(String paramName, byte[] rawData, String type,
            byte[] encodingBytes,
            String fileName) {
        try {
            writeFirstBoundary();
            mOutputStream.write((CONTENT_TYPE + type + NEW_LINE_STR).getBytes());
            mOutputStream
                    .write(getContentDispositionBytes(paramName, fileName));
            mOutputStream.write(encodingBytes);
            mOutputStream.write(rawData);
            mOutputStream.write(NEW_LINE_STR.getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * ��Ӷ����Ʋ���, ����Bitmap���ֽ�������
     * 
     * @param key
     * @param rawData
     */
    public void addBinaryPart(String paramName, final byte[] rawData) {
        writeToOutputStream(paramName, rawData, TYPE_OCTET_STREAM, BINARY_ENCODING, "no-file");
    }
 
    /**
     * ����ļ�����,����ʵ���ļ��ϴ�����
     * 
     * @param key
     * @param file
     */
    public void addFilePart(final String key, final File file) {
        InputStream fin = null;
        try {
            fin = new FileInputStream(file);
            writeFirstBoundary();
            final String type = CONTENT_TYPE + TYPE_OCTET_STREAM + NEW_LINE_STR;
            mOutputStream.write(getContentDispositionBytes(key, file.getName()));
            mOutputStream.write(type.getBytes());
            mOutputStream.write(BINARY_ENCODING);
 
            final byte[] tmp = new byte[4096];
            int len = 0;
            while ((len = fin.read(tmp)) != -1) {
                mOutputStream.write(tmp, 0, len);
            }
            mOutputStream.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(fin);
        }
    }
 
    private void closeSilently(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
 
    private byte[] getContentDispositionBytes(String paramName, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CONTENT_DISPOSITION + "form-data; name=\"" + paramName + "\"");
        // �ı�����û��filename����,����Ϊ�ռ���
        if (!TextUtils.isEmpty(fileName)) {
            stringBuilder.append("; filename=\""
                    + fileName + "\"");
        }
 
        return stringBuilder.append(NEW_LINE_STR).toString().getBytes();
    }
 
    @Override
    public long getContentLength() {
        return mOutputStream.toByteArray().length;
    }
 
    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + mBoundary);
    }
 
    @Override
    public boolean isChunked() {
        return false;
    }
 
    @Override
    public boolean isRepeatable() {
        return false;
    }
 
    @Override
    public boolean isStreaming() {
        return false;
    }
 
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        // ������ĩβ�Ľ�����
        final String endString = "--" + mBoundary + "--\r\n";
        // д�������
        mOutputStream.write(endString.getBytes());
        //
        outstream.write(mOutputStream.toByteArray());
    }
 
    @Override
    public Header getContentEncoding() {
        return null;
    }
 
    @Override
    public void consumeContent() throws IOException,
            UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }
 
    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(mOutputStream.toByteArray());
    }

}
