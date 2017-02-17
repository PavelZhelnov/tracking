package com.tmw.tracking.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {
    /**
     * {@inheritDoc}
     * @see JsonSerializer#serializeWithType(Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider, com.fasterxml.jackson.databind.jsontype.TypeSerializer)
     */
    @Override
    public void serialize(final Date date, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if(date != null)jsonGenerator.writeString(DateFormatUtils.format(date, Utils.DATE_FORMAT));
    }

    /**
     * {@inheritDoc}
     * @see JsonSerializer#handledType()
     */
    @Override
    public java.lang.Class<Date> handledType() {
        return Date.class;
    }
}
