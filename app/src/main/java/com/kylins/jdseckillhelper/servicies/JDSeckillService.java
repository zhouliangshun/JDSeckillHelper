package com.kylins.jdseckillhelper.servicies;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.kylins.jdseckillhelper.R;
import com.kylins.jdseckillhelper.activities.MainActivity;

import java.util.List;

public class JDSeckillService extends AccessibilityService {

    private static JDSeckillService install = null;

    private boolean isStart = false;
    private String goodsName;
    private String seckillTime;
    private String seckillPrice;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public JDSeckillService() {
        install = this;
    }

    @Override
    public void onCreate() {
        createNotification();
        super.onCreate();
    }

    public boolean start(String goodsName, String seckillTime, String seckillPrice) {
        if (TextUtils.isEmpty(goodsName) ||
                TextUtils.isEmpty(seckillTime)||
                TextUtils.isEmpty(seckillPrice))
            return false;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
              startJDMSActivity();
            }
        },1000);

        this.goodsName = goodsName;
        this.seckillTime = seckillTime;
        this.seckillPrice = seckillPrice;
        createNotification();
        return true;
    }


    public void stop(){
        isStart = false;
        createNotification();
    }

    public static JDSeckillService getInstall() {
        return install;
    }

    private void startJDMSActivity(){
        //打开京东
        isCheckTime = false;
        isCheckGoods = false;
        isCheckPrice = false;
        isGoChat = false;
        Intent intent = Intent.makeRestartActivityTask(new ComponentName("com.jingdong.app.mall","com.jingdong.app.mall.MainActivity"));
        startActivity(intent);
        isStart = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        destoryNotification();
        super.onDestroy();
    }

    private boolean isCheckTime = false;
    private boolean isCheckGoods = false;
    private boolean isCheckPrice = false;

    private boolean isGoChat = false;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getSource()==null)
            return;
        if(isStart){

            if(!isGoChat){
                List<AccessibilityNodeInfo> accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/t4");
                if (!accessibilityNodeInfos.isEmpty()){
                    if(accessibilityNodeInfos.get(0).getChildCount()==5){
                        accessibilityNodeInfos.get(0).getChild(3).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        isGoChat = true;
                    }

                }
                return;
            }


            //检查名字
            List<AccessibilityNodeInfo> accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/bp7");
            for(AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
                CharSequence charSequence = accessibilityNodeInfo.getText();
                if (charSequence != null && charSequence.toString().trim().equals(goodsName.trim())) {
                    //检查价格
                    List<AccessibilityNodeInfo> prices =  accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/bp9");
                    if(!prices.isEmpty()){
                            charSequence = prices.get(0).getText();
                        if(charSequence!=null){
                            String str = charSequence.toString();
                            float pricNow = Float.parseFloat(str.replaceAll("[^\\.0123456789]",""));
                            float wantPrice = Float.parseFloat(seckillPrice.replaceAll("[^\\.0123456789]",""));
                            if(wantPrice<=pricNow){
                                //检查是否点击

                                List<AccessibilityNodeInfo> buy =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/bok");
                                if(!buy.isEmpty()) {
                                    buy.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    isCheckPrice = true;
                                    return;
                                }

                            }
                        }
                    }
                    }
                    return;
                }


                if(isCheckPrice){
                   accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/avz");
                   if(!accessibilityNodeInfos.isEmpty()){
                       accessibilityNodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);//下单
                   }
                    return;
                }

                //刷新
                accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/f69");
                if (!accessibilityNodeInfos.isEmpty()){
                    accessibilityNodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                }
            }

//
//
//            if(!isCheckTime){
//                List<AccessibilityNodeInfo> accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/dky");
//                for(AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
//                    CharSequence charSequence = accessibilityNodeInfo.getText();
//                    if(charSequence!=null&&charSequence.toString().equals(seckillTime)){
//                        //检查是否点击
//                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        isCheckTime = true;
//                        return;
//                    }
//                }
//            }
//
//            if(!isCheckGoods){
//                List<AccessibilityNodeInfo> accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jingdong.app.mall:id/su");
//                for(AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
//                    CharSequence charSequence = accessibilityNodeInfo.getText();
//                    if(charSequence!=null&&charSequence.toString().equals(seckillPrice)){
//                        //检查是否点击
//                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        isCheckGoods = true;
//                        return;
//                    }
//                }
//            }
//
//            if(!isCheckPrice){
//                List<AccessibilityNodeInfo> accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("com.jd.lib.productdetail:id/detail_price");
//                List<AccessibilityNodeInfo> carAssss =  event.getSource().findAccessibilityNodeInfosByViewId("com.jd.lib.productdetail:id/add_2_car");
//                if(!carAssss.isEmpty()){
//                    for(AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfos) {
//                        CharSequence charSequence = accessibilityNodeInfo.getText();
//                        if(charSequence!=null){
//                            String str = charSequence.toString();
//                            float pricNow = Float.parseFloat(str.replaceAll("[^\\.0123456789]",""));
//                            float wantPrice = Float.parseFloat(seckillPrice.replaceAll("[^\\.0123456789]",""));
//                            if(wantPrice<=pricNow){
//                                //检查是否点击
//                                carAssss.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                                carAssss = event.getSource().findAccessibilityNodeInfosByViewId("com.jd.lib.productdetail:id/pd_txt_shopcar");
//                                if(!carAssss.isEmpty()){
//                                    carAssss.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                                }
//                                isCheckPrice = true;
//                                return;
//                            }
//                        }
//                    }
//                }
//            }
//
//            if(isCheckPrice){
//                List<AccessibilityNodeInfo> accessibilityNodeInfos =  event.getSource().findAccessibilityNodeInfosByViewId("   com.jingdong.app.mall:id/bok");
//                if(!accessibilityNodeInfos.isEmpty()){
//                    accessibilityNodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    stop();
//                }
//            }

    }

    /**
     * 创建通知
     */
    private void createNotification() {
        if(isStart){
            updateNotification("正在抢购"+seckillPrice+"元 "+goodsName+" "+seckillTime+"开始");
        }else {
            updateNotification("点击设置");
        }

    }

    /**
     * 销毁通知
     */
    private void destoryNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }


    private static final int NOTIFICATION_ID = 1023;


    private void updateNotification(String text) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setContentText(text)
//                .setTicker(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(intent).build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        notificationManager.notify(NOTIFICATION_ID, notification);

    }
}
