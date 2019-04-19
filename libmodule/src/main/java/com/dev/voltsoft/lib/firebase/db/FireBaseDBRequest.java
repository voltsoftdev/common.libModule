package com.dev.voltsoft.lib.firebase.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseRequest;
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

    private String Key;
    private String Value;

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
                    postData();

                    break;
                }

                case DELETE:
                {
                    break;
                }

                case GET:
                {
                    queryData();

                    break;
                }

                case UPDATE:
                {
                    break;
                }
            }
        }
    }

    private void postData()
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
                        T t = dataSnapshot.getValue(targetClass);

                        Log.d("woozie", ">> onChildAdded t = " + (t != null));

                        fireBaseDBResponse.getResponseModel().addModel(t);
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
            ref.push().setValue(postInstance);
        }
    }

    private void queryData()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }


        if (ref != null)
        {
            Query query = null;

            if (TextUtils.isEmpty(Key))
            {
                query = ref.orderByKey();
            }
            else
            {
                query = ref.orderByChild(Key);

                if (!TextUtils.isEmpty(Value)) query = query.equalTo(Value);
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
                                T t = child.getValue(targetClass);

                                fireBaseDBResponse.getResponseModel().addModel(t);
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

    public FireBaseDBRequest addTargetChild(String ... s)
    {
        if (s != null)
        {
            ChildNameList.addAll(Arrays.asList(s));
        }

        return this;
    }


    public FireBaseDBRequest setTargetClass(Class<T> targetClass)
    {
        this.targetClass = targetClass;

        return this;
    }

    public String getKey()
    {
        return Key;
    }

    public void setKey(String k)
    {
        Key = k;
    }

    public String getValue()
    {
        return Value;
    }

    public void setValue(String v)
    {
        Value = v;
    }

    public T getPostInstance()
    {
        return postInstance;
    }

    public void setPostInstance(T t)
    {
        this.postInstance = t;
    }
}
