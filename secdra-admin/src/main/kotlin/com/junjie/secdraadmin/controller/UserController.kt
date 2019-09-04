package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.database.primary.dao.UserDAO
import com.junjie.secdradata.database.primary.entity.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.*

@RestController
@RequestMapping("user")
class UserController(private val userDAO: UserDAO, private val accountService: AccountService) {

    @GetMapping("i")
    fun i(): Int {
        val list = userDAO.findAll()
        var j = 0
        for (item in list) {
            j++
            val account = accountService.signUp(j.toString(), "123456", Date())
            item.accountId = account.id!!
            userDAO.save(item)
        }
        return j
    }

    @PostMapping("init")
    fun init(headPath: String, backPath: String, nameList: List<String>? = listOf("北梦木兮", "南国旧事", "醉酒盛唐", "浮生若茶", "潇湘书笛", "南笙一梦", "红颜知己", "犹寒眉心砂", "时光巷陌", "古道琴台", "乌衣巷口", "城春草木深", "几味老友", "乱世皆烽火", "乱世逐鹿", "难觅旧知音", "诗禅", "借月留云", "笑醉歌寒", "佳人远嫁", "浊酒贪杯", "执墨欲书", "江山美人策", "醉卧红尘", "醉卧青丝衫", "金戈伴铁马", "故人还说着旧梦", "素衫青布白少年", "枕上诗书闲", "酌酒弄弦", "世俗儒生", "时间煮雨", "江南过客", "一曲诀别诗", "浊酒几杯", "弹指红颜", "醉卧红尘外", "相顾无言", "茶饮三道", "半路酒家", "故人不归", "战场书生", "青衫烟雨", "一介書生", "疏影横窗君暗数", "温酒烫眉山", "青衣沽酒醉风尘", "咖啡不懂酒的醉", "清风烈酒", "粗茶布衣", "独赏雨雪纷飞?", "隔岸灯火欲将心事亲藏?", "情人醉倒在花海水乡?", "一缕冷香远", "一别几春", "青丝断情", "春雨浴指", "琴音调齐", "深秋愁凉", "绝望抹杀了期待", "继短半山居人", "如梦无痕", "浊发清眸", "今生他是谁", "挽留你的歌", "残缺韵律", "七分少女心.", "海以南不再蓝.", "一夕夙愿", "落满城月", "浮生若伤", "共茶谈笑", "来日岁月", "浮生尽歇", "及故人", "空城旧人", "红颜一笑醉倾城", "西北望长安", "墨袖留香", "轻纱青衣", "情之所钟", "右耳离心", "衣香鬓影")) {

    }
}