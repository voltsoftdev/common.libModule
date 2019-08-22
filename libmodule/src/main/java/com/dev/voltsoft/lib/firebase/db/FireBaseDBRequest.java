package com.dev.voltsoft.lib.firebase.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.utility.UtilityData;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FireBaseDBRequest<T> extends BaseRequest implements Runnable
{
    private DatabaseReference Reference;

    private RequestType mRequestType;

    private ArrayList<String> ChildNameList = new ArrayList<>();

    private Class<T> targetClass;

    private T postInstance;

    private String WhereClause;

    private String InstanceKey;

    private String EqualValue;

    private String EqualStartStr;

    private double EqualStartValue = -1;

    private String limitStartValue;

    private String limitEndValue;

    private Object UpdateValue;

    public DatabaseReference getReference()
    {
        return Reference;
    }

    public void setReference(DatabaseReference reference)
    {
        Reference = reference;
    }

    public RequestType getType()
    {
        return mRequestType;
    }

    public void setType(RequestType type)
    {
        mRequestType = type;
    }

    @Override
    public void run()
    {
        if (mRequestType != null && Reference != null)
        {
            switch (mRequestType)
            {
                case POST:
                {
                    post();

                    break;
                }

                case DELETE:
                {
                    break;
                }

                case GET:
                {
                    query();

                    break;
                }

                case UPDATE:
                {
                    update();

                    break;
                }
            }
        }
    }

    private void post()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            ref.addChildEventListener(new ChildEventListener()
            {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                {
                    IResponseListener responseListener = getResponseListener();

                    FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                    Log.d("woozie", ">> onChildAdded dataSnapshot.exists() = " + dataSnapshot.exists());

                    if (dataSnapshot.exists() && responseListener != null)
                    {
                        String key = dataSnapshot.getKey();

                        T t = dataSnapshot.getValue(targetClass);

                        Log.d("woozie", ">> onChildAdded t = " + (t != null));

                        fireBaseDBResponse.addResult(key, t);
                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
            DatabaseReference databaseReference = ref.push();

            if (TextUtils.isEmpty(InstanceKey))
            {
                databaseReference.setValue(postInstance);
            }
            else
            {
                databaseReference.setValue(InstanceKey, postInstance);
            }
        }
    }

    private void update()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            ref.child(InstanceKey).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    dataSnapshot.getRef().child(WhereClause).setValue(UpdateValue);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }
    }

    private void query()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }


        if (ref != null)
        {
            Query query = null;

            if (TextUtils.isEmpty(WhereClause))
            {
                query = ref.orderByKey();
            }
            else
            {
                query = ref.orderByChild(WhereClause);

                if (!TextUtils.isEmpty(EqualValue))
                {
                    query = query.equalTo(EqualValue);
                }
                else if (!TextUtils.isEmpty(EqualStartStr))
                {
                    query = query.startAt(EqualStartStr);
                }
                else if (EqualStartValue != -1)
                {
                    query = query.startAt(EqualStartValue);
                }
                else if (!TextUtils.isEmpty(limitStartValue))
                {
                    query = query.limitToFirst(Integer.parseInt(limitStartValue));
                }
                else if (!TextUtils.isEmpty(limitEndValue))
                {
                    query = query.limitToLast(Integer.parseInt(limitEndValue));
                }
            }

            query.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot child : dataSnapshot.getChildren())
                        {
                            try
                            {
                                String key = child.getKey();

                                T t = child.getValue(targetClass);

                                fireBaseDBResponse.addResult(key, t);
                                fireBaseDBResponse.setResponseSuccess(true);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        responseListener.onResponseListen(fireBaseDBResponse);
                    }
                    else
                    {
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                    fireBaseDBResponse.setResponseSuccess(false);

                    responseListener.onResponseListen(fireBaseDBResponse);
                }
            });
        }
    }

    public FireBaseDBRequest mappingTarget(Class<T> targetClass, String ... s)
    {
        this.targetClass = targetClass;

        ChildNameList.addAll(Arrays.asList(s));

        return this;
    }

    public String getWhereClause()
    {
        return WhereClause;
    }


    public String getValue()
    {
        return WhereClause;
    }

    public T getPostInstance()
    {
        return postInstance;
    }

    public void setPostInstance(T t)
    {
        this.postInstance = t;
    }

    public void setPostInstance(String key, T t)
    {
        this.InstanceKey = key;

        this.postInstance = t;
    }

    public void equalToValue(String ... s)
    {
        WhereClause = (s != null && s.length > 0 ? s[0] : null);

        EqualValue = (s != null && s.length > 1 ? s[1] : null);
    }

    public void startAt(Object ... o)
    {
        Object param1 = (o != null && o.length > 0 ? o[0] : null);

        Object param2 = (o != null && o.length > 1 ? o[1] : null);

        if (param1 instanceof String)
        {
            WhereClause = (String) param1;
        }

        if (param2 instanceof String)
        {
            EqualStartStr = (String) param2;
        }
        else if (param2 instanceof Double)
        {
            EqualStartValue = (double) param2;
        }
    }

    public void linmitToLast(String ... s)
    {
        WhereClause = (s != null && s.length > 0 ? s[0] : null);

        limitEndValue = (s != null && s.length > 1 ? s[1] : null);
    }

    public void linmitToStart(String ... s)
    {
        WhereClause = (s != null && s.length > 0 ? s[0] : null);

        limitStartValue = (s != null && s.length > 1 ? s[1] : null);
    }

    public void update(String key, String w, Object d)
    {
        InstanceKey = key;

        WhereClause = w;

        UpdateValue = d;
    }
}
