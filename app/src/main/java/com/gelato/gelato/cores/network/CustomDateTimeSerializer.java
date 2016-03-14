package com.gelato.gelato.cores.network;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by YongJae on 2016-02-10.
 */
public class CustomDateTimeSerializer extends StdScalarSerializer<DateTime> {

    public CustomDateTimeSerializer() {
        super(DateTime.class);
    }

    @Override
    public void serialize(DateTime dateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider) throws IOException, JsonGenerationException {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(DateTimeZone.forID("Asia/Seoul"));
        String dateTimeAsString = fmt.print(dateTime);
        jsonGenerator.writeString(dateTimeAsString);
    }
}
