package zero.com.utillib.utils;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import zero.com.utillib.Activity.MApplication;


public class ExceptionUtil {
	
	public static void handlingException(String msg,Throwable tr){

		try{
//			BaseActivity.dialogDismiss();
		}catch (Exception e){
			Toast.makeText(MApplication.currentActivity(), "Dialog关闭异常！", Toast.LENGTH_LONG).show();
		}
		
		msg +='\n' +Log.getStackTraceString(tr);
		alertDialogOneBtn(msg);
		Logs.errlog(msg,tr);
	}
	private static Builder oneBtnDg=null;
	private static void alertDialogOneBtn(String msg){
		if(oneBtnDg==null){
			oneBtnDg = new Builder(MApplication.currentActivity());
		}
		oneBtnDg.setCancelable(false)
		.setTitle("")
		.setMessage(msg)
		.setPositiveButton("确定",//设置确定按钮
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						
					}
				}
		);
		oneBtnDg.show();
	}
}
