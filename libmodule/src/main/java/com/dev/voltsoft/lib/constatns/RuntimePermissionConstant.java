package com.dev.voltsoft.lib.constatns;

/**
 * RuntimePermissionConstant..
 */
public interface RuntimePermissionConstant {
    /**
     * 액티비티가 존재하지 않을경우 에러코드.
     */
    int ERR_TRYFAIL = -1;
    /**
     * 런타임 퍼미션 상태 체크 초기 상태.
     */
    int INIT = 0;
    /**
     * 현재 런타임 퍼미션 허용 알람창이 떠올라있는 상태 (사용자가 허용선택中).
     */
    int RUNNING = 1;
    /**
     * 초기에 권한이 하나라도 거부되어 있는 경우.
     */
    int DENIEDPERMISSION_EXIST = 2;
    /**
     * 초기에 모든 권한이 허용되어 있는 경우.
     */
    int ALLPERMISSION_ALLOWED = 3;
    /**
     * 사용자가 권한을 하나라도 거부로 선택한 경우.
     */
    int SELECT_DENIED = 4;
    /**
     * 사용자가 권한을 모두 허용한 경우.
     */
    int SELECT_GRANTED = 5;
}
