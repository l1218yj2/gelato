package com.gelato.gelato.cores.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Mathpresso3 on 2015-08-02.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultResponse {
    public String error;
    public JsonNode info;
}
