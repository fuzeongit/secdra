package com.junjie.secdraweb.base.advice

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.model.Result
import javassist.NotFoundException
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class GlobalHandler : ResponseBodyAdvice<Any?> {
    override fun supports(methodParameter: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(returnValue: Any?, methodParameter: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, p4: ServerHttpRequest, p5: ServerHttpResponse): Result<*> {
        return returnValue as? Result<*> ?: Result(returnValue)
    }

    //空指针异常
    @ResponseBody
    @ExceptionHandler(NullPointerException::class)
    fun nullPointerExceptionHandler(e: NullPointerException): Result<Any?> {
        return Result(500, e.message!!);
    }

    //找不到异常
    @ResponseBody
    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(e: NotFoundException): Result<Any?> {
        return Result(404, e.message!!);
    }

    //自定义异常
    @ResponseBody
    @ExceptionHandler(ProgramException::class)
    fun programExceptionHandler(e: ProgramException): Result<Any?> {
        return Result(e.status, e.message!!);
    }

    //其他异常
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): Result<Any?> {
        return Result(500, e.message!!);
    }


}