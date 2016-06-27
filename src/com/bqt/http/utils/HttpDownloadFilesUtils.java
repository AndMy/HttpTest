package com.bqt.http.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.bqt.http.HttpDownloadActivity;

/** 下传文件工具类*/
public class HttpDownloadFilesUtils {

	/**直接使用URLConnection.openStream()打开网络输入流,然后将流写入到文件中*/
	public static void simpleDownLoad(String fileUrl, String filePath, boolean isUseUrlName, Handler mHandler) {
		String fileName;
		if (isUseUrlName) fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);//截取文件名及后缀名
		else fileName = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss", Locale.CHINA).format(new Date()) + fileUrl.substring(fileUrl.lastIndexOf("."));
		fileName = fileName == null ? "bqt" : fileName;
		try {
			InputStream inputStream = new URL(fileUrl).openStream();
			OutputStream outputStream = new FileOutputStream(new File(filePath + fileName));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
			inputStream.close();
			outputStream.close();
			mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_OK, filePath + fileName));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//*****************************************************************************************************************
	//																							多线程下载
	//*****************************************************************************************************************
	public static final int THREAD_COUNT = 3;// 线程的数量
	public static long blocksize;// 每个下载区块的大小
	public static int runningTHREAD_COUNT;// 正在运行的线程的数量

	/**
	 * 多线程下载
	 *  方式1:使用多线程分别下载文件的不同部分，最后把【合并】成一个文件(效率高)。方式2:使用java提供的【RandomAccessFile】类实现多线程的下载(简单)。
	 * @param fileUrl		服务器路径
	 * @param filePath	保存本地路径
	 * @param isUseUrlName	是否使用服务器路径中的文件名，设为false则使用当前时间作为文件名
	 * @param mHandler	通过mHandler和UI线程通讯
	 * @param pbs		在子线程直接更新各线程下载进度，设为null则不考虑
	 */
	public static void mutileThreadDownload(String fileUrl, String filePath, boolean isUseUrlName, Handler mHandler, ArrayList<ProgressBar> pbs) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();//获取连接
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if (conn.getResponseCode() == 200) {
				// 1、在本地创建一个大小跟服务器一模一样的空白文件
				long fileSize = conn.getContentLength();// 得到服务端文件的大小
				String info = "\n服务端文件的大小:" + fileSize + " ( " + fileSize / 1024 / 1024 + "M )";
				mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));
				String fileName;
				if (isUseUrlName) fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);//截取文件名及后缀名
				else fileName = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss", Locale.CHINA).format(new Date()) + fileUrl.substring(fileUrl.lastIndexOf("."));
				fileName = fileName == null ? "bqt" : fileName;
				RandomAccessFile raf = new RandomAccessFile(filePath + fileName, "rw");//可以从指定位置开始读、写文件；模式：r、rw、rws、rwd
				raf.setLength(fileSize);//设定大小
				raf.close();

				// 2、开启若干个子线程分别去下载对应的资源
				blocksize = fileSize / THREAD_COUNT; // 每个下载区块的大小
				runningTHREAD_COUNT = THREAD_COUNT; //运行的线程数量

				for (int i = 1; i <= THREAD_COUNT; i++) {
					long startIndex = (i - 1) * blocksize;//开始位置，从0开始
					long endIndex = i * blocksize - 1;//结束位置
					if (i == THREAD_COUNT) endIndex = fileSize - 1;// 最后一个线程的结束位置为 size - 1，若值比它大，也不会有异常，实际下载大小也是  size - 1
					info = "\n开启线程 " + i + " ,下载范围:" + startIndex + "~" + endIndex;
					mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));
					//ProgressBar、SeekBar、ProgressDialog 这些都是可以在子线程直接更新进度的
					if (pbs != null && pbs.get(i - 1) != null) pbs.get(i - 1).setMax((int) (endIndex - startIndex));//设置各个线程的进度条的最大值
					//3、调用下面的逻辑完成多线程下载
					new DownloadThread(fileUrl, filePath + fileName, i, startIndex, endIndex, mHandler, pbs).start();
				}
			}
			conn.disconnect();//取消连接
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class DownloadThread extends Thread {//线程Thread的子类
		private String fileUrl;
		private String filePath;
		private int threadId;
		private long startIndex;
		private long endIndex;
		private Handler mHandler;
		private ArrayList<ProgressBar> pbs;
		/**定义一个记录当前线程已下载文件的大小的临时文件，若文件不存在则从头下载，否则从记录的位置继续下载；写入时则将其封装为RandomAccessFile*/
		private File positionFile;

		/***
		* @param fileUrl		服务器路径
		* @param filePath	缓存文件保存路径
		* @param threadId		线程id，请使用0、1、2、3……形式，并请按顺序命名
		* @param startIndex	当前线程开始下载的位置
		* @param endIndex	当前线程结束下载的位置
		* @param mHandler	通过mHandler和UI线程通讯
		* @param pbs		在子线程直接更新各线程下载进度，设为null则不考虑
		*/
		public DownloadThread(String fileUrl, String filePath, int threadId, long startIndex, long endIndex, Handler mHandler, ArrayList<ProgressBar> pbs) {
			this.fileUrl = fileUrl;
			this.filePath = filePath;
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.mHandler = mHandler;
			this.pbs = pbs;
		}

		public void run() {
			String info;
			try {
				// 1、记录当前线程已下载的总大小
				int total = 0;// 初始值设为0，若已下载部分文件，则获取已下载部分文件的大小并重新赋值
				positionFile = new File(filePath + "-" + threadId);
				if (positionFile.exists() && positionFile.length() > 0) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(positionFile)));
					String totalstr = bufferedReader.readLine();// 获取当前线程上次下载的总【大小】是多少
					total = Integer.valueOf(totalstr);
					info = "\n上次线程" + threadId + "下载的总大小：" + total;
					mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));
					startIndex += total;//每个线程继续下载时的开始【位置】注意startIndex的值为开始下载的【位置】，是一个索引；total的值为实际下载的文件的【大小】
					bufferedReader.close();
				}
				//2、获取连接，设置连接的参数信息
				HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("RANGE", "bytes=" + startIndex + "-" + endIndex);//指定每条线程从文件的什么位置开始下载，下载到什么位置为止
				// 注意，下载服务器中某一部分内容返回码是206，可以用【code/100==2】来判断
				InputStream inputStream = conn.getInputStream();//返回服务端返回的流

				//3、将流中的数据写入文件中
				RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
				randomAccessFile.seek(startIndex);// 指定文件开始写的位置（指针偏移量）。
				info = "\n第 " + threadId + " 个线程：写文件的开始位置：" + String.valueOf(startIndex);
				mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));

				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = inputStream.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, len);//向randomAccessFile中写入读取到的流中的内容
					//将此文件封装成为一个RandomAccessFile，并采用采用rwd模式，即使断电也不会丢失信息！
					RandomAccessFile rf = new RandomAccessFile(positionFile, "rwd");
					total += len;//记录下载的总大小
					rf.write(String.valueOf(total).getBytes());
					rf.close();
					if (pbs != null && pbs.get(threadId - 1) != null) pbs.get(threadId - 1).setProgress(total);//设置当前进度条的进程值
				}
				inputStream.close();
				randomAccessFile.close();
			} catch (Exception e) {
			} finally {
				//4、 所有的线程都下载完毕后删除记录文件
				synchronized (HttpDownloadFilesUtils.class) {//线程安全问题
					info = "\n线程 " + threadId + " 下载完毕了";
					mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));
					runningTHREAD_COUNT--;
					if (runningTHREAD_COUNT < 1) {
						info = "\n所有线程已下载完毕，删除临时文件";
						mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));
						for (int i = 1; i <= THREAD_COUNT; i++) {
							File temFile = new File(filePath + "-" + i);
							info = "\n删除临时文件 " + i + " ,状态: " + temFile.delete();//删除记录文件
							mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_INFO, info));
						}
						mHandler.sendMessage(Message.obtain(mHandler, HttpDownloadActivity.MSG_WHAT_DOWNLOAD_OK, filePath));
					}
				}
			}
		}
	}
}