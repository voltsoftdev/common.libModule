package com.dev.voltsoft.lib.firebase.db;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FireBaseDBRequest<T> extends BaseRequest implements Runnable
{
    private class FireBaseQueryClause
    {
        private String fieldName;

        private Object fieldCondtion;
    }

    private DatabaseReference Reference;

    private RequestType mRequestType;

    private ArrayList<String> ChildNameList = new ArrayList<>();

    private Class<T> targetClass;

    private T postInstance;

    private String postInstanceKey;

    private String QueryOrderBy;

    private FireBaseQueryClause EqualClause;

    private FireBaseQueryClause StartAtClause;

    private FireBaseQueryClause EndAtClause;

    private FireBaseQueryClause limitStartClause;

    private FireBaseQueryClause limitEndClause;

    private FireBaseQueryClause updateClause;

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
                    runPost();

                    break;
                }

                case DELETE:
                {
                    break;
                }

                case GET:
                {
                    runQuery();

                    break;
                }

                case UPDATE:
                {
                    runUpdate();

                    break;
                }
            }
        }
    }

    private void runPost()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                    if (dataSnapshot.exists() && responseListener != null)
                    {
                        String key = dataSnapshot.getKey();

                        T t = dataSnapshot.getValue(targetClass);

                        fireBaseDBResponse.addResult(key, t);
                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }
            });


            if (TextUtils.isEmpty(postInstanceKey))
            {
                ref.push().setValue(postInstance);
            }
            else
            {
                ref.child(postInstanceKey).setValue(postInstance);
            }
        }
    }

    private void runUpdate()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (dataSnapshot.exists() && responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }
            });

            ref.child(postInstanceKey).child(updateClause.fieldName).setValue(updateClause.fieldCondtion);
        }
    }

    private void runQuery()
    {
        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            Query query = ref.orderByKey();

            if (EqualClause != null)
            {
                query = ref.orderByChild(EqualClause.fieldName);

                Object o = EqualClause.fieldCondtion;

                if (o instanceof String)
                {
                    query = query.equalTo((String) o);
                }
                else if (o instanceof Integer)
                {
                    query = query.equalTo((int) o);
                }
            }


            if (StartAtClause != null)
            {
                query = ref.orderByChild(StartAtClause.fieldName);

                Object o = StartAtClause.fieldCondtion;

                if (o instanceof String)
                {
                    query = query.startAt((String) o);
                }
                else if (o instanceof Integer)
                {
                    query = query.startAt((int) o);
                }
            }

            if (StartAtClause != null)
            {
                query = ref.orderByChild(StartAtClause.fieldName);

                Object o = StartAtClause.fieldCondtion;

                if (o instanceof String)
                {
                    query = query.startAt((String) o);
                }
                else if (o instanceof Integer)
                {
                    query = query.startAt((int) o);
                }
            }

            if (EndAtClause != null)
            {
                query = ref.orderByChild(EndAtClause.fieldName);

                Object o = EndAtClause.fieldCondtion;

                if (o instanceof String)
                {
                    query = query.endAt((String) o);
                }
                else if (o instanceof Integer)
                {
                    query = query.endAt((int) o);
                }
            }

            if (limitStartClause != null)
            {
                query = ref.orderByChild(limitStartClause.fieldName);

                Object o = limitStartClause.fieldCondtion;

                if (o instanceof Integer)
                {
                    query = query.limitToFirst((int) o);
                }
            }

            if (limitEndClause != null)
            {
                query = ref.orderByChild(limitEndClause.fieldName);

                Object o = limitEndClause.fieldCondtion;

                if (o instanceof Integer)
                {
                    query = query.limitToLast((int) o);
                }
            }

            query.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
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

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
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

    public void setPostInstance(T t)
    {
        this.postInstance = t;
    }

    public void setPostInstance(String key, T t)
    {
        this.postInstanceKey = key;

        this.postInstance = t;
    }

    public void equalToValue(String key, Object o)
    {
        EqualClause = new FireBaseQueryClause();
        EqualClause.fieldName = key;
        EqualClause.fieldCondtion = o;
    }

    public void orderBy(String s)
    {
        QueryOrderBy = s;
    }

    public void startAt(String key, Object o)
    {
        StartAtClause = new FireBaseQueryClause();
        StartAtClause.fieldName = key;
        StartAtClause.fieldCondtion = o;
    }

    public void endAt(String key, Object o)
    {
        EndAtClause = new FireBaseQueryClause();
        EndAtClause.fieldName = key;
        EndAtClause.fieldCondtion = o;
    }

    public void range(String key, Object ... params)
    {
        StartAtClause = new FireBaseQueryClause();
        StartAtClause.fieldName = key;
        StartAtClause.fieldCondtion = (params != null && params.length > 0 ? params[0] : null);

        EndAtClause = new FireBaseQueryClause();
        EndAtClause.fieldName = key;
        EndAtClause.fieldCondtion = (params != null && params.length > 1 ? params[1] : null);
    }

    public void linmitToLast(String key, int limit)
    {
        limitEndClause = new FireBaseQueryClause();
        limitEndClause.fieldName = key;
        limitEndClause.fieldCondtion = limit;
    }

    public void linmitToStart(String key, int limit)
    {
        limitStartClause = new FireBaseQueryClause();
        limitStartClause.fieldName = key;
        limitStartClause.fieldCondtion = limit;
    }

    public void update(String key, String w, Object d)
    {
        postInstanceKey = key;

        updateClause = new FireBaseQueryClause();
        updateClause.fieldName = key;
        updateClause.fieldCondtion = d;
    }
}
