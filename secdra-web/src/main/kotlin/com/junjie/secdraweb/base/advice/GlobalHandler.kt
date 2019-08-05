package com.junjie.secdraweb.base.advice

import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.*
import com.junjie.secdracore.model.Result
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.lang.reflect.UndeclaredThrowableException
import java.sql.SQLException

/**
 * @author fjj
 * 相应体的处理
 */
@ControllerAdvice
class GlobalHandler : ResponseBodyAdvice<Any?> {

    override fun supports(methodParameter: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        return try {
            //只要拥有RestfulPack这个注解就会执行响应体包装
            methodParameter.method!!.isAnnotationPresent(RestfulPack::class.java)
        } catch (e: Exception) {
            false
        }
    }

    override fun beforeBodyWrite(returnValue: Any?, methodParameter: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, p4: ServerHttpRequest, p5: ServerHttpResponse): Result<*> {
        return returnValue as? Result<*> ?: Result(returnValue)
    }

    //运行时异常
    @ResponseBody
    @ExceptionHandler(UndeclaredThrowableException::class)
    fun exceptionHandler(e: UndeclaredThrowableException): Result<Any?> {
        val baseException = e.undeclaredThrowable as? BaseException ?: BaseException(e.message!!, 500)
        return Result(baseException.status, baseException.message, baseException.data)

    }

    //运行时异常
    @ResponseBody
    @ExceptionHandler(RuntimeException::class)
    fun exceptionHandler(e: RuntimeException): Result<Any?> {
        return Result(500, e.message, e.cause)
    }

    //空指针异常
    @ResponseBody
    @ExceptionHandler(NullPointerException::class)
    fun nullPointerExceptionHandler(e: NullPointerException): Result<Any?> {
        return Result(500, e.message, e.cause);
    }

    //sql查询异常
    @ResponseBody
    @ExceptionHandler(SQLException::class)
    fun sqlExceptionHandler(e: SQLException): Result<Any?> {
        return Result(500, e.message, e.cause);
    }

    //找不到异常
    @ResponseBody
    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(e: NotFoundException): Result<Any?> {
        return Result(e.status, e.message, e.cause);
    }

    /**
     * 权限异常
     */
    @ResponseBody
    @ExceptionHandler(PermissionException::class)
    fun permissionExceptionHandler(e: PermissionException): Result<Any?> {
        return Result(e.status, e.message, e.data);
    }


    /**
     * 登录异常
     */
    @ResponseBody
    @ExceptionHandler(SignInException::class)
    fun signInExceptionHandler(e: SignInException): Result<Any?> {
        return Result(e.status, e.message, e.data);
    }


    //自定义异常
    @ResponseBody
    @ExceptionHandler(ProgramException::class)
    fun programExceptionHandler(e: ProgramException): Result<Any?> {
        return Result(e.status, e.message, e.data);
    }


    //系统异常
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): Result<Any?> {
        return Result(500, e.message)
    }
}