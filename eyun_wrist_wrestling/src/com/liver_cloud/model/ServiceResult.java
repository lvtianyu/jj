package com.liver_cloud.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.liver_cloud.util.EnumErrorCode;

public class ServiceResult<T> {

    @JSONField(serialize = false)
    private EnumErrorCode enumError;
    private String result;
    private T values;
    private String errormsg;
    private String errorcode;

    public EnumErrorCode getEnumError() {
        return enumError;
    }

    public void setEnumError(EnumErrorCode enumError) {
        this.enumError = enumError;
    }

    public String getResult() {
        if (this.enumError.result != null) {
            return enumError.result;
        } else {
            return this.result;
        }
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getValues() {
        return values;
    }

    public void setValues(T values) {
        this.values = values;
    }

    public String getErrormsg() {
        if (this.enumError.msg != null) {
            return enumError.msg;
        } else {
            return this.errormsg;
        }
    }

   	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
    public String getErrorcode() {
        if (this.enumError.code != null) {
            return enumError.code;
        } else {
            return this.errorcode;
        }
    }
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}


}
