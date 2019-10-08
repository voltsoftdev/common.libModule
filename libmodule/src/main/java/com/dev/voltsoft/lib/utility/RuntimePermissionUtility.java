
package com.dev.voltsoft.lib.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

/**
 * M OS 이상일 때 권한 허용 여부를 확인하기 위한 유티릴티이다.<br>
 * M OS 미만 버전이면 모든 권한은 허용 상태이다.
 * 
 * @author Edison
 * 
 */
public class RuntimePermissionUtility {

	/**
	 * 거부된 Permission 목록을 구한다.
	 * 
	 * @param oContext
	 *            Context
	 * @param aDangerousPermission
	 *            Dangerous 퍼미션
	 * @return String[]
	 */
	public static String[] getDeniedPermission(Context oContext, String[] aDangerousPermission) {

		if (isBelowMOS()) {
			/* M OS 미만 버전이면 모든 권한은 허용 상태이다 */
			return null;
		}

		/* Dangerous Permission 목록을 구한다. */
		if (null == aDangerousPermission || 0 == aDangerousPermission.length) {
			/* Dangerous Permission 이 존재하지 않는다. */
			return null;
		}

		/* 거부된 Permission 구한다. */
		StringBuilder oDeniedPermisssionStringBuilder = new StringBuilder();
		try {
			for (String permission : aDangerousPermission) {

				if (false == hasSelfPermission(oContext, permission)) {
					if (0 < oDeniedPermisssionStringBuilder.length()) {
						oDeniedPermisssionStringBuilder.append("/");
					}
					oDeniedPermisssionStringBuilder.append(permission);
				}
			}
		} catch (Exception e) {
		}

		/* 거부된 Permission 모음 문자열을 스트링배열로 변환한다. */
		String[] aResultPermission = null;
		if (0 < oDeniedPermisssionStringBuilder.length()) {
			try {
				aResultPermission = oDeniedPermisssionStringBuilder.toString().split("/");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return aResultPermission;
	}

	/**
	 * App 내에서 사용하는 Dangerous Permission 목록을 구한다.
	 * 
	 * @param oContext
	 *            Context
	 * @param strPackageName
	 *            String
	 * @return String[]
	 */
	public static String[] findDangerousPermissionForApp(Context oContext, String strPackageName) {

		/* Dangerous Level의 퍼미션 문자열을 모집한다. 각 퍼미션별 구분은 "/"으로 구분한다. */
		StringBuilder oPermissionStringBuilder = new StringBuilder();
		HashMap<String, String> oDangerousPermissionMap = null;
		try {

			PackageManager pm = oContext.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(strPackageName, PackageManager.GET_PERMISSIONS);

			/* Package에서 사용하는 퍼미션 이름 모음 */
			String[] aPermissionName = packageInfo.requestedPermissions;
			/* 안드로이드 퍼미션 모음. */
			oDangerousPermissionMap = getAndroidDengurousPermission(oContext);
			if (null != aPermissionName && null != oDangerousPermissionMap) {
				for (String strPermission : aPermissionName) {
					if (isDangerousPermission(oDangerousPermissionMap, strPermission)) {
						if (0 < oPermissionStringBuilder.length()) {
							oPermissionStringBuilder.append("/");
						}
						oPermissionStringBuilder.append(strPermission);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* 사용한 Map 인스턴스는 초기화 한다. */
		if (null != oDangerousPermissionMap) {
			oDangerousPermissionMap.clear();
			oDangerousPermissionMap = null;
		}

		/* 퍼미션 문자열 모음을 스트링배열로 변환한다. */
		String[] aPermission = null;
		if (0 < oPermissionStringBuilder.length()) {
			try {
				aPermission = oPermissionStringBuilder.toString().split("/");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/* 퍼미션 스트링배열을 리턴한다. */
		return aPermission;
	}

	/**
	 *
	 * @param oMap
	 *            HashMap<String, String>
	 * @param strPermission
	 *            String
	 * @return boolean
	 */
	public static boolean isDangerousPermission(HashMap<String, String> oMap, String strPermission) {
		if (oMap.containsValue(strPermission)) {
			return true;
		}
		return false;
	}

	/**
	 * 앱내에 등록된 Dangerous 퍼미션들이 권한이 거부된 것을 확인한다.
	 * 
	 * @param oActivity
	 *            Activity
	 * @return boolean true: 거부된 권한이 존재한다. false: 거부된 권한이 없다.
	 */
	public static boolean hasDeniedPermissionForApp(Activity oActivity) {

		if (isBelowMOS()) {
			/* 거부된 퍼미션이 없다. */
			return false;
		}

		/* App에서 사용하는 Dangerous Permission 목록을 구한다. */
		String[] aDangerousPermission = findDangerousPermissionForApp(oActivity, oActivity.getPackageName());

		/* 퍼미션중 거부된 퍼미션만 구해 온다. */
		String[] aPermission = getDeniedPermission(oActivity, aDangerousPermission);
		if (null == aPermission || 0 == aPermission.length) {
			/* 거부된 퍼미션이 없다. */
			return false;
		}

		/* 거부된 퍼미션이 존재한다. */
		return true;
	}

	/**
	 *
	 * @param oContext
	 *            Context
	 * @return HashMap<String, String>
	 */
	public static HashMap<String, String> getAndroidDengurousPermission(Context oContext) {

		HashMap<String, String> oPermission = new HashMap<String, String>();
		try {
			PackageManager pm = oContext.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo("android", PackageManager.GET_PERMISSIONS);
			PermissionInfo[] aPermissionInfo = packageInfo.permissions;

			if (null != aPermissionInfo) {
				for (PermissionInfo info : aPermissionInfo) {
					if (PermissionInfo.PROTECTION_DANGEROUS == info.protectionLevel) {
						oPermission.put(info.name, info.name);
					}
				}
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return oPermission;
	}

	/**
	 * 권한 설정 팝업에서 "다시 묻지 않기" 체크가 된 것인지 판단한다.<br>
	 * 전달된 권한들 중에 하나라도 존재하면 응답값은 true이면 체크된 것이다.
	 * 
	 * @param oActivity
	 *            Activity
	 * @param permissions
	 *            String[]
	 * @param grantResults
	 *            int[]
	 * @return boolean
	 */
	public static boolean isCheckedNotRetryRequestPermissionPopup(Activity oActivity, String[] permissions,
                                                                  int[] grantResults) {

		int nSize = 0;
		if (null == grantResults) {
			return true;
		}
		nSize = grantResults.length;
		for (int i = 0; i < nSize; i++) {
			if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
				if (false == ActivityCompat.shouldShowRequestPermissionRationale(oActivity, permissions[i])) {
					/* 권한이 거부되었고 이 값이 false이면 "다시 묻지 않기"가 체크된 상태이다. */
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * 제공된 모든 퍼미션을 승인되었는지 체크한다.
	 * 
	 * @see Activity#onRequestPermissionsResult(int, String[], int[])
	 * 
	 * @param grantResults
	 *            int[]
	 * @return boolean
	 */
	public static boolean verifyPermissions(int[] grantResults) {
		// Verify that each required permission has been granted, otherwise return false.
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 제공된 권한들이 허용되어 있는지 확인한다.
	 * 
	 * @param oContext
	 *            Context
	 * @param aDeniedPermissions
	 *            String[] 거부된 퍼미션 정보 들
	 * @return boolean
	 */
	public static boolean hasSelfPermission(Context oContext, String[] aDeniedPermissions) {
		if (isBelowMOS() || null == aDeniedPermissions) {
			return true;
		}

		for (String permission : aDeniedPermissions) {
			if (false == hasSelfPermission(oContext, permission)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 제공된 권한이 허용되어 있는지 확인한다.
	 * 
	 * @param oContext
	 *            Context
	 * @param strDeniedPermissions
	 *            String 거부된 퍼미션 정보
	 * @return boolean
	 */
	public static boolean hasSelfPermission(Context oContext, String strDeniedPermissions) {
		if (isBelowMOS() || null == strDeniedPermissions) {
			return true;
		}

		return ContextCompat.checkSelfPermission(oContext, strDeniedPermissions) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * M preview 1, 2 OS 버전 인경우.
	 * 
	 * @return boolean
	 */
	public static boolean isMNC() {
		return "MNC".equals(Build.VERSION.CODENAME);
	}

	/**
	 *
	 * @return booleans ..
	 */
	public static boolean isBelowMOS() {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
			/* API Level 이 22 이며, M 버전이 아닐 때 true */
			if (isMNC()) {
				return false;
			}
			return true;
		}
		return false;
	}

}
