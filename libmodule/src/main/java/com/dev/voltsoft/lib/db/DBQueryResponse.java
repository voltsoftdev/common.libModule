package com.dev.voltsoft.lib.db;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;

public class DBQueryResponse<M extends BaseModel> extends BaseResponse {

    private boolean mbInserted;
    private boolean mbUpdated;
    private boolean mbDeleted;

    public boolean isInserted() {
        return mbInserted;
    }

    public void setInserted(boolean bInserted) {
        this.mbInserted = bInserted;
    }

    public boolean isUpdated() {
        return mbUpdated;
    }

    public void setUpdated(boolean bUpdated) {
        this.mbUpdated = bUpdated;
    }

    public boolean isDeleted() {
        return mbDeleted;
    }

    public void setDeleted(boolean bDeleted) {
        this.mbDeleted = bDeleted;
    }
}
