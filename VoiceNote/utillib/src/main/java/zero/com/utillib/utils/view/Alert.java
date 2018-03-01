package zero.com.utillib.utils.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import zero.com.utillib.Activity.MApplication;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.StringUtils;

/**
 * Created by ws on 2017/7/27.
 */

public class Alert {

    public static void alertDialogOneBtnWarn(String mag) {
        MApplication.playWarn(-1);
        alertDialogOneBtn(mag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO 自动生成的方法存根
                MApplication.stopWarn();
            }
        });
    }


    public static void alertDialogOneBtn(String mag) {
        alertDialogOneBtn(mag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO 自动生成的方法存根
            }
        });
    }

    public static void alertDialogOneBtn(String mag, DialogInterface.OnClickListener confirmListener) {
         alertDialogTowBtn(mag,null, confirmListener, null);
    }

    public static void alertDialogOneBtn(String mag,String title, DialogInterface.OnClickListener confirmListener) {
        alertDialogTowBtn(mag,title, confirmListener, null);
    }

    private static  AlertDialog.Builder btnDg = null;

    public static void setBtnDg(AlertDialog.Builder btnDg) {
        Logs.wslog("-setBtnDg-");
        Alert.btnDg = btnDg;
    }



    public  static void alertDialogTowBtn(String mag, DialogInterface.OnClickListener confirmListener,DialogInterface.OnClickListener cancelListener) {
        //Logs.wslog("--"+btnDg);
        alertDialogTowBtn(mag,null,confirmListener,cancelListener);
    }


    public  static void alertDialogTowBtn(String mag,String title,DialogInterface.OnClickListener confirmListener,DialogInterface.OnClickListener cancelListener) {
        //Logs.wslog("--"+btnDg);
        alertDialogTowBtn(mag,title,null,confirmListener,null,cancelListener);
    }
    //private static Activity dialogActivity = null;
    /*private static AlertDialog dialog = null;


    private static DialogInterface.OnClickListener mCancelListener = null;*/
    public synchronized static void alertDialogTowBtn(String mag,String title,String confirmName,final DialogInterface.OnClickListener confirmListener,String cancelName,final DialogInterface.OnClickListener cancelListener) {
        //Logs.wslog("--"+btnDg);
        if(btnDg==null && !MApplication.currentActivity().isFinishing()){
            //Logs.wslog("--"+BvpApplication.currentActivity());
            btnDg = new AlertDialog.Builder(MApplication.currentActivity());

            if(StringUtils.isEmpty(confirmName)){
                confirmName = "确定";
            }

            btnDg.setCancelable(false)
                    .setTitle(title)
                    .setMessage(mag)
                    .setPositiveButton(confirmName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MApplication.stopWarn();
                            btnDg = null;
                            confirmListener.onClick(dialog,which);

                        }
                    });//设置确定按钮

            if (cancelListener != null) {
                if(StringUtils.isEmpty(cancelName)){
                    cancelName = "取消";
                }
                btnDg.setNegativeButton(cancelName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MApplication.stopWarn();
                        btnDg = null;
                        cancelListener.onClick(dialog,which);
                    }
                });
            }
            btnDg.show();
        }
    }

    public static void alertDialogTowBtn(String mag) {
        alertDialogTowBtn(mag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
    }

    public static  void alertDialogTowBtn(String mag, DialogInterface.OnClickListener clickListener) {
        alertDialogTowBtn(mag, null,clickListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
    }

    public static  void alertDialogTowBtn(String mag,String title, DialogInterface.OnClickListener clickListener) {
        alertDialogTowBtn(mag, title,clickListener, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialog, int which);
    }

    /**
     * 带声音的弹出框
     *
     * @param value
     */
    public static void toastWarnError(String value) {
        toastShort(value);
        MApplication.sp.play(MApplication.m_warn, 1, 1, 0, 0, 1);
    }

    /**
     * 带声音的弹出框
     *
     * @param value
     */
    public static void toastWarnSuesss(String value) {
        toastShort(value);
        MApplication.sp.play(MApplication.m_right, 1, 1, 0, 0, 1);
    }

    /**
     * 长时间toast
     *
     * @param mag
     */
    public static void toast(String mag) {
        Toast.makeText(MApplication.currentActivity(), mag, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间
     *
     * @param mag
     */
    public static void toastShort(String mag) {
        Toast.makeText(MApplication.currentActivity(), mag, Toast.LENGTH_SHORT).show();
    }

}
