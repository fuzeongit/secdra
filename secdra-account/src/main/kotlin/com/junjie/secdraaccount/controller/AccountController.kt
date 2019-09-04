package com.junjie.secdraaccount.controller

import com.junjie.secdraaccount.core.component.AccountConfig
import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdracore.annotations.Auth
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdracore.model.Result
import com.junjie.secdracore.util.JwtUtil
import com.junjie.secdracore.util.RegexUtil
import com.junjie.secdradata.constant.VerificationCodeOperation
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("account")
class AccountController(private val accountConfig: AccountConfig,
                        private val redisTemplate: StringRedisTemplate,
                        private val accountService: AccountService) {

    /**
     * 发送验证码
     */
    @PostMapping("sendCode")
    @RestfulPack
    fun sendCode(phone: String, verificationCodeOperation: VerificationCodeOperation): Result<String> {
        !RegexUtil.checkMobile(phone) && throw ProgramException("请输入正确的手机号码")
        var verificationCode = ""
        while (verificationCode.length < 6) {
            verificationCode += Random().nextInt(10).toString()
        }
        return when (verificationCodeOperation) {
            VerificationCodeOperation.REGISTER -> {
                accountService.existsByPhone(phone) && throw ProgramException("手机号码已存在")
//                smsComponent.sendVerificationCode(phone, verificationCode)
                redisTemplate.opsForValue().set(String.format(accountConfig.registerVerificationCodePrefix, phone), verificationCode, accountConfig.verificationCodeTimeout, TimeUnit.MILLISECONDS)
                Result(200, null, verificationCode)
            }
            VerificationCodeOperation.FORGET -> {
                !accountService.existsByPhone(phone) && throw ProgramException("手机号码不存在")
//                smsComponent.sendVerificationCode(phone, verificationCode)
                redisTemplate.opsForValue().set(String.format(accountConfig.forgetVerificationCodePrefix, phone), verificationCode, accountConfig.verificationCodeTimeout, TimeUnit.MILLISECONDS)
                Result(200, null, verificationCode)
            }
            else -> {
                Result(200, null, "888888")
            }
        }
    }

    @PostMapping("/signUp")
    @RestfulPack
    fun signUp(phone: String, password: String, verificationCode: String, response: HttpServletResponse): Boolean {
        !RegexUtil.checkMobile(phone) && throw ProgramException("请输入正确的手机号码")
        !RegexUtil.checkPassword(password) && throw ProgramException("请输入正确的密码（密码由数字，字母和下划线的6-16位字符组成）")
        val redisCode = redisTemplate.opsForValue()[String.format(accountConfig.registerVerificationCodePrefix, phone)]
        verificationCode != redisCode && throw ProgramException("验证码无效")
        //获取系统时间
        val nowMillis = System.currentTimeMillis()
        val user = accountService.signUp(phone, password, Date(nowMillis))
        //清理验证码
        redisTemplate.delete(String.format(accountConfig.registerVerificationCodePrefix, phone))
        //把修改密码时间放到redis
        redisTemplate.opsForValue().set(String.format(accountConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        //生成token
        val token = JwtUtil.createJWT(user.id!!, nowMillis, accountConfig.jwtExpiresSecond, accountConfig.jwtSecretString)
        response.setHeader("token", token)
        return true
    }

    @PostMapping("/signIn")
    @RestfulPack
    fun signIn(phone: String, password: String, response: HttpServletResponse): Boolean {
        val user = accountService.signIn(phone, password)
        val nowMillis = System.currentTimeMillis()
        val token = JwtUtil.createJWT(user.id!!, nowMillis, accountConfig.jwtExpiresSecond, accountConfig.jwtSecretString)
        response.setHeader("token", token)
        return true
    }

    @PostMapping("/forgot")
    @RestfulPack
    fun forgot(phone: String, password: String, verificationCode: String, response: HttpServletResponse): Boolean {
        !RegexUtil.checkMobile(phone) && throw ProgramException("请输入正确的手机号码")
        !RegexUtil.checkPassword(password) && throw ProgramException("请输入正确的密码（密码由数字，字母和下划线的6-16位字符组成）")
        val redisCode = redisTemplate.opsForValue()[String.format(accountConfig.forgetVerificationCodePrefix, phone)]
        verificationCode != redisCode && throw PermissionException("验证码无效")
        val nowMillis = System.currentTimeMillis()
        val user = accountService.forgot(phone, password, Date(nowMillis))
        redisTemplate.delete(String.format(accountConfig.forgetVerificationCodePrefix, phone))
        redisTemplate.opsForValue().set(String.format(accountConfig.updatePasswordTimePrefix, user.id), nowMillis.toString())
        return true
    }

    @Auth
    @PostMapping("/checkLogin")
    @RestfulPack
    fun checkLogin(): Boolean {
        return true
    }
}