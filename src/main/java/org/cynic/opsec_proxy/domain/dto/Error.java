package org.cynic.opsec_proxy.domain.dto;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;

public record Error(String code, Serializable... values) {
    public Error(String code, Serializable... values) {
        this.code = code;
        this.values = ObjectUtils.cloneIfPossible(values);
    }

    public String getCode() {
        return code;
    }

    public Serializable[] getValues() {
        return values;
    }
}
