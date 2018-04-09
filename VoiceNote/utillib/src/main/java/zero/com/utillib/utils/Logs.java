package zero.com.utillib.utils;

import android.util.Log;

import zero.com.utillib.BuildConfig;

public class Logs {
	/**
	 * debug log （公用）
	 * @param msg 消息
	 */
	public final static void debug(String msg) {
		Log.d("debug", msg);
	}

	public final static void JLlog(String msg) {
		customlog("LJL",msg);
	}

	public final static void wslog(String msg) {
		customlog("ws",msg);
	}

	/**
	 * 自定义log （公用）
	 * @param logName log名称
	 * @param msg 消息
	 */
	public final static void customlog(String logName,String msg) {
		if (BuildConfig.DEBUG){
			Log.d(logName,msg);
		}
	}

	/**
	 *
	 * @param msg
	 * @param e
     */
	public final static void errlog(String msg,Throwable e) {
		//#ifdef DEBUG
		Log.e("WsTest",msg,e);
		//#endif
	}
}
