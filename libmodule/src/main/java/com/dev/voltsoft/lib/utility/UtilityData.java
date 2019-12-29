package com.dev.voltsoft.lib.utility;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.ContextCompat;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilityData {

    /**
     * 제공된 권한이 허용되어 있는지 확인한다.
     *
     * @param oContext
     *            Context
     * @param strDeniedPermissions
     *            String 거부된 퍼미션 정보
     * @return boolean
     */
    public static boolean isPermissionGranted(Context oContext, String strDeniedPermissions)
    {
        if (UtilityData.isBelowMOS() || strDeniedPermissions == null)
        {
            return true;
        }

        return ContextCompat.checkSelfPermission(oContext, strDeniedPermissions) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *
     * @return booleans ..
     */
    public static boolean isBelowMOS()
    {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
			/* API Level 이 22 이며, M 버전이 아닐 때 true */
            if (isMNC())
            {
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * M preview 1, 2 OS 버전 인경우.
     *
     * @return boolean
     */
    public static boolean isMNC()
    {
        return "MNC".equals(Build.VERSION.CODENAME);
    }

    public static String getMobileNumber(Context context)
    {
        if (context != null)
        {
            try
            {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                @SuppressLint("HardwareIds") String mdn = telephonyManager.getLine1Number();

                if (!TextUtils.isEmpty(mdn) && mdn.contains("+82")) {
                    mdn = mdn.replace("+82" , "01");
                }
                return mdn;
            }
            catch (SecurityException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public static boolean getViewNeedtobeRefreshed(View view)
    {
        try
        {
            if (view != null)
            {
                long currentTimeDate = System.currentTimeMillis();
                long lastUpdatedDate = (long) view.getTag();

                int minute = 1000 * 60;
                int refreshStandard = minute * 30;

                long diffTime = (currentTimeDate - lastUpdatedDate);
                if ((diffTime / refreshStandard) > 1)
                {
                    view.setTag(currentTimeDate);

                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public static void exportSQLiteDB(Context context)
    {
        try
        {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {
                String currentDBPath = "//data//com.voltsoft.edu.engword//databases//cardEngWord.db";
                String backupDBPath = "cardEngWord.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists())
                {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

                if (backupDB.exists())
                {
                    Toast.makeText(context, "DB Export Complete!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String getSubString(String strOriginal , int start , int end) {
        try {
            return strOriginal.substring(start , end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getParameterValid(int position , Object... params)
    {
        try
        {
            return (params[position] != null);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean getEnglishCharacter(Character inputCharacter) 
    {
        if (((inputCharacter >= 0x61 && inputCharacter <= 0x7A)) || (inputCharacter >=0x41 && inputCharacter <= 0x5A)) 
        {
            return true;
        } 
        else 
        {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) 
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {
            return false;
        } else {
            return (activeNetwork.isAvailable());
        }
    }

    public static String getAppVersion(Context context) 
    {
        String version = "0.0.0";
        
        try 
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return version;
    }

    public static String getHashKey(Context context) {
        String strHashKey = null;
        try {
            String strPackageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(strPackageName , PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                strHashKey = new String(Base64.encode(messageDigest.digest() , 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strHashKey;
    }

    public static Class<?> getColumnType(Class instance , String fieldName) {
        for (Field field : instance.getFields()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return field.getType();
            }
        }
        return null;
    }

    public static Field[] getColumns(Class instance) {
        return instance.getFields();
    }

    public static Field[] getColumns(Object instance) {
        return getColumns(instance.getClass());
    }

    public static String getStringFromJSON(JSONObject jsonObject , String jsonField) {
        try
        {
            return jsonObject.getString(jsonField);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String[] getStringArrayFromJSON(JSONObject jsonObject , String jsonField) {
        try
        {
            String data = jsonObject.getString(jsonField);

            if (!TextUtils.isEmpty(data) && (data.contains("[") || data.contains("]") || data.contains(",")))
            {
                data = data.replace("[", "");
                data = data.replace("]", "");

                return data.split(",");
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static int getIntegerFromJSON(JSONObject jsonObject , String jsonField)
    {
        try
        {
            return jsonObject.getInt(jsonField);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public static boolean getBooleanFromJSON(JSONObject jsonObject , String jsonField)
    {
        try
        {
            return jsonObject.getBoolean(jsonField);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static JSONArray getJsonArrayFromJson(JSONObject jsonObject , String jsonField)
    {
        try
        {
            return jsonObject.getJSONArray(jsonField);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static JSONObject getJsonFromJson(JSONObject jsonObject , String jsonField)
    {
        try
        {
            return jsonObject.getJSONObject(jsonField);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getMD5HashCode(String strOriginalData) {
        String MD5 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strOriginalData.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            MD5 = strOriginalData.hashCode()+"";
        }
        return MD5;
    }

    public static String getCurrentDataString()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss" , Locale.getDefault());
        Date currentTime = new Date();
        String strCurrentData = simpleDateFormat.format(currentTime);
        return strCurrentData;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri))
            {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
