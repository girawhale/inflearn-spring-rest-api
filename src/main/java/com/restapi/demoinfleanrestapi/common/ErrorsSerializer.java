package com.restapi.demoinfleanrestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeFieldName("errors"); // Spring Boot 2.3부터 Jackson라이브러리가 Array를 만드는 것을 허용하지 않음
        jsonGenerator.writeStartArray();

        errors.getFieldErrors().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("field", e.getField());
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null)
                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());

                jsonGenerator.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());

                jsonGenerator.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
