package com.alternadom.wifiiot;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import java.util.HashMap;

import androidx.annotation.RequiresApi;
import io.flutter.plugin.common.EventChannel;

public class NetworkCallbackReceiver extends BroadcastReceiver
        implements EventChannel.StreamHandler{
    private Context context;

    public NetworkCallbackReceiver(Context context) {
        this.context = context;

    }

    private EventChannel.EventSink events;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (events != null) {
            final String action = intent.getAction();
            if (SimpleNetworkCallbackImpl.NETWORK_ON_LINK_CHANGE_ACTION.equals(action)){
                final String hostAddr = intent.getStringExtra("data");
                final String eventType = intent.getStringExtra(SimpleNetworkCallbackImpl.ON_LINK_CHANGED_EVT);
                HashMap<String, String> data = new HashMap<>();
                data.put("EvtType", eventType);
                data.put("data", hostAddr);
                events.success(data);

            }
            if (SimpleNetworkCallbackImpl.NETWORK_ON_LINK_LOST_ACTION.equals(action)){
                final String hostAddr = intent.getStringExtra("data");
                final String eventType = intent.getStringExtra(SimpleNetworkCallbackImpl.ON_LOST_EVT);
                HashMap<String, String> data = new HashMap<>();
                data.put("EvtType", eventType);
                data.put("data", hostAddr);
                events.success(data);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        this.events = events;
        context.registerReceiver(this,makeIntentFilter());
    }

    @Override
    public void onCancel(Object arguments) {
        context.unregisterReceiver(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private IntentFilter makeIntentFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SimpleNetworkCallbackImpl.NETWORK_ON_LINK_CHANGE_ACTION);
        intentFilter.addAction(SimpleNetworkCallbackImpl.NETWORK_ON_LINK_LOST_ACTION);

        return intentFilter;
    }
}
