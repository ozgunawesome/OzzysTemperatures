package services.ozzy.ozzystemperatures;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MockUsbService extends Service {

    private IBinder binder = new MockUsbBinder();
    private Handler handler;
    public static boolean SERVICE_CONNECTED = false;

    private static Timer timer = new Timer();

    private BigDecimal[] sensorValues = {new BigDecimal("30.0"), new BigDecimal("25.0"), new BigDecimal("20.0")};

    private Random random = new Random();

    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);

        SERVICE_CONNECTED = true;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (handler != null) {
                    for (int i = 0; i < 3; i++) {
                        sensorValues[i] = sensorValues[i].add(new BigDecimal(random.nextDouble() - 0.5));
                        handler
                                .obtainMessage(0, new TempSensorMessage("sensor" + i, sensorValues[i]))
                                .sendToTarget();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 0, 1000);
    }

    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        SERVICE_CONNECTED = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class MockUsbBinder extends Binder {
        public MockUsbService getService() {
            return MockUsbService.this;
        }
    }

}
