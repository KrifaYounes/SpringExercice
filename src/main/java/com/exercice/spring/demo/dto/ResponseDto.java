package com.exercice.spring.demo.dto;

public class ResponseDto<T> {

    private T object;
    private ErrorDto errorDto;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }

    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }
}
