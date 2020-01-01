package com.dev.voltsoft.lib.component;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SparseArray;
import android.view.*;
import android.widget.CompoundButton;
import com.dev.voltsoft.lib.constatns.RuntimePermissionConstant;
import com.dev.voltsoft.lib.session.SessionRequestHandler;
import com.dev.voltsoft.lib.utility.*;
import com.dev.voltsoft.lib.utility.RuntimePermission;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class CommonActivity extends AppCompatActivity implements View.OnClickListener, FragmentManager.OnBackStackChangedListener, CompoundButton.OnCheckedChangeListener, ActivityCompat.OnRequestPermissionsResultCallback {

    /** ONE vod M OS에서 체크해야 할 퍼미션의 종류 */
	/* READ_EXTERNAL_STORAGE (API 16) STORAGE 그룹이다. */
    public static final String[] PERMISSIONS_NECESSARY = {
            Manifest.permission.RECORD_AUDIO, // STORAGE 그룹
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // STORAGE 그룹
            Manifest.permission.READ_PHONE_STATE, // PHONE 그룹
    };

    /**
     * 연속 클릭 이벤트 방지 맵.
     */
    private SparseArray mPreviousClickEvent = new SparseArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        EasyLog.LogMessage(this, ">> onCreate ");

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.removeOnBackStackChangedListener(this);
            fragmentManager.addOnBackStackChangedListener(this);

            init(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);

        EasyLog.LogMessage(this, ">> onCreate ");

        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.removeOnBackStackChangedListener(this);
            fragmentManager.addOnBackStackChangedListener(this);

            init(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected abstract void init(Bundle savedInstanceState) throws Exception;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        SessionRequestHandler.getInstance().handleActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SessionRequestHandler.getInstance().pause(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int end = grantResults.length;
        if (end > 0)
        {

            ArrayList<RuntimePermission> deniedPermissionList = new ArrayList<>();

            boolean bPermissionsDenied = false;

            boolean bAllNoAskAgainState = true;

            for (int i = 0; i < end; i++)
            {
                String permission = permissions[i];

                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    bPermissionsDenied = true;

                    RuntimePermissionHelper.getInstance().setCheckState(RuntimePermissionConstant.SELECT_DENIED);

                    boolean showRationale = false;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }

                    RuntimePermission deniedPermission = new RuntimePermission();

                    deniedPermission.PermissionName = permission;
                    deniedPermission.ShowRationale = showRationale;

                    bAllNoAskAgainState &= !showRationale;

                    deniedPermissionList.add(deniedPermission);
                }
            }

            if (bPermissionsDenied)
            {
                onPermissionsDenied(bAllNoAskAgainState, deniedPermissionList);

                return;
            }
        }

        RuntimePermissionHelper.getInstance().setCheckState(RuntimePermissionConstant.ALLPERMISSION_ALLOWED);

        onPermissionsGranted(permissions);
    }

    protected void onPermissionsDenied(boolean isAllNoAskAgainState, ArrayList<RuntimePermission> deniedPermissionList)
    {
        EasyLog.LogMessage(">> onPermissionsDenied " + this.getClass().getSimpleName() + " ");
    }

    protected void onPermissionsGranted(String[] permissions)
    {
        EasyLog.LogMessage(">> onPermissionsGranted " + this.getClass().getSimpleName() + " ");
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onClick(View v)
    {
        EasyLog.LogMessage(">> onClick " + this.getClass().getSimpleName() + " ");

        int viewId = v.getId();

        int previousEventTime = (int) mPreviousClickEvent.get(viewId, 0);

        if ((SystemClock.elapsedRealtime() - previousEventTime) < 200 && previousEventTime > 0)
        {
            EasyLog.LogMessage("-- onClick prevent too fast click event .. ");

            return;
        }

        mPreviousClickEvent.put(viewId, (int) SystemClock.elapsedRealtime());

        onClickEvent(v);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onCheckedChanged(CompoundButton v, boolean b)
    {
        int viewId = v.getId();

        int previousEventTime = (int) mPreviousClickEvent.get(viewId, 0);

        if ((SystemClock.elapsedRealtime() - previousEventTime) < 200 && previousEventTime > 0)
        {
            return;
        }
        mPreviousClickEvent.put(viewId, (int) SystemClock.elapsedRealtime());

        onCheckedChangedEvent(v, b);
    }

    protected void onClickEvent(View v)
    {
        EasyLog.LogMessage(this, ">> onClickEvent ");
    }

    protected void onCheckedChangedEvent(CompoundButton v, boolean b)
    {
        EasyLog.LogMessage(this, ">> onCheckedChangedEvent ");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Fragment fragment = getVisibleTopFragment();

            if (fragment instanceof CommonFragment)
            {
                CommonFragment commonFragment = (CommonFragment) fragment;

                if (!commonFragment.onBackPressed())
                {
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public final void onBackStackChanged()
    {
        EasyLog.LogMessage(this, ">> onBackStackChanged ");

        Fragment fragment = getVisibleTopFragment();

        onFragmentVisible(fragment);
    }

    protected void onFragmentVisible(Fragment fragment)
    {
        String name = (fragment == null ? null : fragment.getClass().getSimpleName());

        EasyLog.LogMessage(this, ">> onFragmentVisible ", name);
    }

    protected View inflate(int resourceId)
    {
        return LayoutInflater.from(this).inflate(resourceId, null);
    }

    protected View inflate(int resourceId, ViewGroup parent)
    {
        return LayoutInflater.from(this).inflate(resourceId, parent, false);
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V find(int id)
    {
        return (V) findViewById(id);
    }

    protected <A extends Activity> void moveToActivity(Class<A> aClass)
    {
        Intent intent = new Intent(this, aClass);

        startActivity(intent);
    }

    protected <A extends Activity> void moveToActivity(Class<A> aClass, String bundleName, Bundle bundle)
    {
        Intent intent = new Intent(this, aClass);

        intent.putExtra(bundleName, bundle);

        startActivity(intent);
    }

    protected <A extends Activity> void moveToActivityForResult(Class<A> aClass, int requestCode)
    {
        Intent intent = new Intent(this, aClass);

        startActivityForResult(intent, requestCode);
    }

    protected <A extends Activity> void moveToActivityForResult(Class<A> aClass, int requestCode, String bundleName, Bundle bundle)
    {
        Intent intent = new Intent(this, aClass);

        intent.putExtra(bundleName, bundle);

        startActivityForResult(intent, requestCode);
    }

    protected final void post(Runnable r)
    {
        (new Handler(getMainLooper())).post(r);
    }

    protected final void post(Runnable r, int delay)
    {
        (new Handler(getMainLooper())).postDelayed(r, delay);
    }

    protected void replaceFragment(int containerId, Fragment fragment)
    {
        try
        {
            String tag = fragment.getClass().getSimpleName();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId , fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void replaceFragment(int containerId, Fragment fragment, String tag)
    {
        try
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId , fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void addFragmentStack(int containerId, Fragment fragment)
    {
        try
        {
            String tag = fragment.getClass().getSimpleName();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void addFragmentStack(int containerId, Fragment fragment, String tag)
    {
        try
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected <F extends CommonFragment> void popBackFragmentStack(F f)
    {
        if (f != null)
        {
            popBackFragmentStack(f.getClass().getSimpleName());
        }
    }

    protected void popBackFragmentStack()
    {
        getSupportFragmentManager().popBackStack();
    }

    protected void popBackFragmentStack(String tag)
    {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected int getVisibleTopFragmentCount()
    {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        return (fragmentList == null ? 0 : fragmentList.size());
    }

    protected Fragment getVisibleTopFragment()
    {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        if (fragmentList != null)
        {
            Log.d("woozie", ">> getVisibleTopFragment fragmentList = " + fragmentList.size());

            int fragmentListSize = fragmentList.size();

            for (int i = (fragmentListSize - 1) ; i > -1 ; i --)
            {
                Fragment fragment = fragmentList.get(i);

                Log.d("woozie", ">> getVisibleTopFragment fragment = " + fragment.getTag());
                Log.d("woozie", ">> getVisibleTopFragment isVisible = " + fragment.isVisible());

                if (fragment.isVisible())
                {
                    return fragment;
                }
            }
        }

        return null;
    }

    protected String[] getDeniedPermission() {

        if (UtilityData.isBelowMOS()) {
			/* M OS 미만 버전이면 모든 권한은 허용 상태이다 */
            return null;
        }

		/* Dangerous Permission 목록을 구한다. */
        if (null == PERMISSIONS_NECESSARY || 0 == PERMISSIONS_NECESSARY.length) {
			/* Dangerous Permission 이 존재하지 않는다. */
            return null;
        }

		/* 거부된 Permission 구한다. */
        StringBuilder oDeniedPermisssionStringBuilder = new StringBuilder();
        try
        {
            for (String permission : PERMISSIONS_NECESSARY) {

                if (!UtilityData.isPermissionGranted(this, permission)) {

                    if (0 < oDeniedPermisssionStringBuilder.length())
                    {
                        oDeniedPermisssionStringBuilder.append("/");
                    }

                    oDeniedPermisssionStringBuilder.append(permission);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
}
