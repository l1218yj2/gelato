package com.gelato.gelato.cores.network;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by YongJae on 2016-02-10.
 */
public class CustomDateTimeDeserializer extends StdScalarDeserializer<DateTime> {

    public CustomDateTimeDeserializer() {
        super(DateTime.class);
    }

    @Override
    public DateTime deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_STRING) {
            String dateTimeAsString = jsonParser.getText().trim();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(DateTimeZone.forID("Asia/Seoul"));
            DateTime result = fmt.parseDateTime(dateTimeAsString);
            return result;
        }
        throw deserializationContext.mappingException(getValueClass());
    }
}