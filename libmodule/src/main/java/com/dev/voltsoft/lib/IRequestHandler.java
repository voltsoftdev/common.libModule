package com.dev.voltsoft.lib;

import android.content.Intent;
import com.dev.voltsoft.lib.model.BaseRequest;

import java.util.Observer;

public interface IRequestHandler<R extends BaseRequest>
{
    void handle(R r);
}
