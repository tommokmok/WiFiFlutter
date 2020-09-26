package com.alternadom.wifiiot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import androidx.annotation.RequiresApi;


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class SimpleNetworkCallbackImpl extends ConnectivityManager.NetworkCallback{
    //Static Constants
    public static String NETWORK_ON_LINK_CHANGE_ACTION ="NetworkCallback.onLinkPropertiesChanged";
    public static String NETWORK_ON_LINK_LOST_ACTION="NetworkCallback.onLost";
    public static String ON_LOST_EVT="onLost";
    public static String ON_LINK_CHANGED_EVT="onLinkChanged";
    final String TAG="NetworkCallbackImpl";
    //Need context for send the intent
    private Context context;
    public SimpleNetworkCallbackImpl(Context context) {
        this.context = context;

    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
//        Log.i(TAG, "onAvailable: network -"+network.toString());
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);

        try {
            Log.i(TAG, "onLosing: -network  "+network.getByName(null).getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLost(Network network) {
        Log.i(TAG, "onLost: ");
        Intent intent = new Intent();
        intent.setAction(NETWORK_ON_LINK_LOST_ACTION);
        intent.putExtra("EvtType", "onLost");
        intent.putExtra("data", "NA");

        context.sendBroadcast(intent);

        super.onLost(network);
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
//        Log.i(TAG, "onUnavailable: ");
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
//        Log.i(TAG, "onCapabilitiesChanged: networkCapabilities- "+networkCapabilities.toString());
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        List<InetAddress> dns=linkProperties.getDnsServers();
        Log.i(TAG, "onLinkPropertiesChanged : LinkProperties= "+dns.get(0).getHostAddress());
        Intent intent = new Intent();
        intent.setAction(NETWORK_ON_LINK_CHANGE_ACTION);
        intent.putExtra("EvtType", "onLinkChanged");
        intent.putExtra("data", dns.get(0).getHostAddress());

        context.sendBroadcast(intent);

    }
}
