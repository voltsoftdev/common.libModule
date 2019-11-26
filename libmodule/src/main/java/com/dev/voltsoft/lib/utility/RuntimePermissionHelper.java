package com.dev.voltsoft.lib.utility;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.core.app.ActivityCompat;
import com.dev.voltsoft.lib.constatns.RuntimePermissionConstant;

/**
 * RuntimePermissionHelper..
 */
public class RuntimePermissionHelper implements RuntimePermissionConstant {

	private static int mnCurrentState = 0;

	private static final int REQUEST_START_APP = 1;

	private static class LazyHolder
	{
		private static RuntimePermissionHelper mInstance = new RuntimePermissionHelper();
	}

	/**
	 * 권한 확인용 인스턴스를 확보한다.
	 *
	 * @return RuntimePermissionVodBoxHelper
	 */
	public static RuntimePermissionHelper getInstance()
	{
		return LazyHolder.mInstance;
	}

	/** ONE vod M OS에서 체크해야 할 퍼미션의 종류 */
	/* READ_EXTERNAL_STORAGE (API 16) STORAGE 그룹이다. */
	public static String[] PERMISSIONS_NECESSARY;

	/**
	 * 요구사항 Code에 맞는 퍼미션들을 구한다. <br>
	 * 정의된 코드가 아니면 null로 린턴된다.
	 *
	 * @param nRequestCode
	 *            int
	 * @return String[]
	 */
	public static String[] getPermissions(int nRequestCode) {
		switch (nRequestCode) {
			case REQUEST_START_APP:
				return PERMISSIONS_NECESSARY;
			default:
				return null;
		}
	}

	private static final String PermissionAllowed = "PermissionAllowed";

	/**
	 * 거부된 권한이 있을 경우 false 반환. <br>
	 *
	 * @param context
	 *            context
	 *
	 * @return boolean <br>
	 *         true : 런타임 퍼미션 모두 허용된 상태 / false : 런타임 퍼미션이 하나라도 불허용 된 상태
	 */
	public boolean isRuntimePermissionAllowed(Application context)
	{
		if (RuntimePermissionUtility.isBelowMOS())
		{
			CommonPreference commonPreference = new CommonPreference();
			commonPreference.init(context);

			return commonPreference.getSharedValueByBoolean(PermissionAllowed , false);
		}
		else
		{
			try
			{
				String[] aDeniedPermission = RuntimePermissionUtility.getDeniedPermission(
						context, getPermissions(REQUEST_START_APP));

				return (aDeniedPermission == null || aDeniedPermission.length == 0);
			}
			catch (Exception e)
			{
				e.printStackTrace();

				return false;
			}
		}
	}

	/**
	 * 앱 런타임 퍼미션 권한 체크 상태를 반환.
	 *
	 * @param context
	 *            context
	 * @return int
	 */
	public int checkAndRequestPermission(Context context)
	{
		String[] aDeniedPermission = RuntimePermissionUtility.getDeniedPermission(
				context, getPermissions(REQUEST_START_APP));

		return checkAndRequestPermission(aDeniedPermission);
	}

	/**
	 * 앱 런타임 퍼미션 권한 체크 상태를 반환.
	 *
	 * @param aDeniedPermission
	 *            aDeniedPermission
	 * @return int
	 */
	public int checkAndRequestPermission(String[] aDeniedPermission) {

		if (mnCurrentState != INIT) {
			return mnCurrentState;
		}

		if (null == aDeniedPermission) {
			return ALLPERMISSION_ALLOWED;
		} else {
			return DENIEDPERMISSION_EXIST;
		}
	}

	/**
	 * M OS 이상 일 때 권한 허용 요청을 한다.
	 *
	 * @param oActivity
	 *            oActivity
	 */
	public void doRequestPermissions(final Activity oActivity) {
		String[] deniedPermissions = RuntimePermissionUtility.getDeniedPermission(
				oActivity , PERMISSIONS_NECESSARY);
		RuntimePermissionHelper.getInstance().setCheckState(RUNNING);

		ActivityCompat.requestPermissions(oActivity, deniedPermissions, REQUEST_START_APP);
	}

	/**
	 *
	 * @param state
	 *            state
	 */
	public void setCheckState(int state) {
		mnCurrentState = state;
	}

	/**
	 *
	 * @return int
	 */
	public int getCheckState() {
		return mnCurrentState;
	}
}
