package com.outsystems.plugins.debug;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.outsystems.plugins.debug.console.OSConsole;
import com.outsystems.plugins.loader.clients.ChromeClient;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.json.JSONArray;
import org.json.JSONException;

public class OSDebugLogs extends CordovaPlugin {

    private OSConsole mOsConsole;

    private ViewGroup consoleViewGroup;

    @Override
    protected void pluginInitialize() {

        SystemWebViewEngine webViewEngine = (SystemWebViewEngine)this.webView.getEngine();
        SystemWebView systemWebView = (SystemWebView)webViewEngine.getView();

        OSDebugLogsChromeClient chromeClient = new OSDebugLogsChromeClient(webViewEngine,this.cordova);
        
        systemWebView.setWebChromeClient(chromeClient);


        cordova.getActivity().runOnUiThread(new Runnable() {
            @SuppressWarnings("ResourceType")
            @Override
            public void run() {

                CordovaActivity cordovaActivity = (CordovaActivity) cordova.getActivity();

                ViewGroup mainViewGroup = (ViewGroup) webView.getView().getParent();

                ViewGroup rootView = (ViewGroup)mainViewGroup.getParent();

                if(rootView instanceof LinearLayout){
                    LinearLayout linearLayout = (LinearLayout)rootView;
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                }

                RelativeLayout consoleViewGroup = new RelativeLayout(cordovaActivity);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                consoleViewGroup.setLayoutParams(params);
                consoleViewGroup.setId(2016);
                consoleViewGroup.setVisibility(View.GONE);

                rootView.addView(consoleViewGroup, 0);
                rootView.invalidate();

                setConsoleViewGroup(consoleViewGroup);
                // Ensure FragmentActivity is passed for AndroidX compatibility
                mOsConsole = new OSConsole(cordovaActivity, getConsoleViewGroup());
            }
        });
    }

    public ViewGroup getConsoleViewGroup() {
        return consoleViewGroup;
    }

    public void setConsoleViewGroup(ViewGroup consoleViewGroup) {
        this.consoleViewGroup = consoleViewGroup;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("openConsole")) {            
            this.openConsole(callbackContext);
            return true;
        }
        else{
            if (action.equals("closeConsole")) {                
                this.closeConsole(callbackContext);
                return true;
            }
        }
        return false;
    }

    private void openConsole(CallbackContext callbackContext) {

        PluginResult pluginResult = null;

        try{

            cordova.getActivity().runOnUiThread(new Runnable() {
                @SuppressWarnings("ResourceType")
                @Override
                public void run() {

                    if (mOsConsole == null) {
                        mOsConsole = new OSConsole((CordovaActivity)cordova.getActivity(), getConsoleViewGroup());
                    }

                    mOsConsole.open();
                }
            });

            pluginResult = new PluginResult(PluginResult.Status.OK);

        }catch(Exception e){
            pluginResult = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }

        callbackContext.sendPluginResult(pluginResult);
    }

    private void closeConsole(CallbackContext callbackContext) {

        PluginResult pluginResult = null;

        try{

            cordova.getActivity().runOnUiThread(new Runnable() {
                @SuppressWarnings("ResourceType")
                @Override
                public void run() {

                    if (mOsConsole == null) {
                        mOsConsole = new OSConsole((CordovaActivity)cordova.getActivity(), getConsoleViewGroup());
                    }

                    mOsConsole.close();
                }
            });

            pluginResult = new PluginResult(PluginResult.Status.OK);

        }catch(Exception e){
            pluginResult = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }

        callbackContext.sendPluginResult(pluginResult);
    }


    private class OSDebugLogsChromeClient extends ChromeClient {

        public OSDebugLogsChromeClient(SystemWebViewEngine parentEngine, CordovaInterface cordovaInterface) {
            super(parentEngine, cordovaInterface);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

            if (OSDebugLogs.this.mOsConsole != null) {
                String str = String.format("Line %d : %s", consoleMessage.lineNumber(), consoleMessage.message());
                if (OSDebugLogs.this.mOsConsole.getConsoleInterface() != null) {
                    OSDebugLogs.this.mOsConsole.getConsoleInterface().log(str);
                }
            }

            return super.onConsoleMessage(consoleMessage);
        }
    }
    
}
