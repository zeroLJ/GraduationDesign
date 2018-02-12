package zero.com.utillib.utils;

import android.util.Log;

public class Logs {
	/**
	 * debug log （公用）
	 * @param msg 消息
	 */
	public final static void debug(String msg) {
		//#ifdef DEBUG
		Log.d("debug", msg);
		//#endif
	}
	/**
	 * 上 专用log
	 * @param msg 消息
	 */
	public final static void wslog(String msg) {
		//#ifdef DEBUG
		customlog("WsTest",msg);
		//#endif
	}

	/**
	 * 上 专用log
	 * @param msg 消息
	 */
	public final static void JLlog(String msg) {
		//#ifdef DEBUG
		customlog("LJL",msg);
		//#endif
	}

	/**
	 * 自定义log （公用）
	 * @param logName log名称
	 * @param msg 消息
	 */
	public final static void customlog(String logName,String msg) {
		//#ifdef DEBUG
		Log.d(logName,msg);
		//#endif
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
