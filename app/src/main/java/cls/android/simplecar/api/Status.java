package cls.android.simplecar.api;

import android.os.Bundle;

import org.json.JSONObject;

public class Status {
    private boolean isSuccessfulAction;
    private int errorCode;
    private JSONObject additionalInformation;

    public Status(boolean isSuccessfulAction, int errorCode, JSONObject additionalInformation) {
        this.isSuccessfulAction = isSuccessfulAction;
        this.errorCode = errorCode;
        this.additionalInformation = additionalInformation;
    }

    public Status() {
    }

    public boolean isSuccessfulAction() {
        return isSuccessfulAction;
    }

    public void setSuccessfulAction(boolean successfulAction) {
        isSuccessfulAction = successfulAction;
    }



    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public JSONObject getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(JSONObject additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
