package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("user")
class UserController(private var userDao: IUserDao) {

    @PostMapping("init")
    fun init(headPath: String, backPath: String, nameList: List<String>? = listOf("北梦木兮", "南国旧事", "醉酒盛唐", "浮生若茶", "潇湘书笛", "南笙一梦", "红颜知己", "犹寒眉心砂", "时光巷陌", "古道琴台", "乌衣巷口", "城春草木深", "几味老友", "乱世皆烽火", "乱世逐鹿", "难觅旧知音", "诗禅", "借月留云", "笑醉歌寒", "佳人远嫁", "浊酒贪杯", "执墨欲书", "江山美人策", "醉卧红尘", "醉卧青丝衫", "金戈伴铁马", "故人还说着旧梦", "素衫青布白少年", "枕上诗书闲", "酌酒弄弦", "世俗儒生", "时间煮雨", "江南过客", "一曲诀别诗", "浊酒几杯", "弹指红颜", "醉卧红尘外", "相顾无言", "茶饮三道", "半路酒家", "故人不归", "战场书生", "青衫烟雨", "一介書生", "疏影横窗君暗数", "温酒烫眉山", "青衣沽酒醉风尘", "咖啡不懂酒的醉", "清风烈酒", "粗茶布衣", "独赏雨雪纷飞?", "隔岸灯火欲将心事亲藏?", "情人醉倒在花海水乡?", "一缕冷香远", "一别几春", "青丝断情", "春雨浴指", "琴音调齐", "深秋愁凉", "绝望抹杀了期待", "继短半山居人", "如梦无痕", "浊发清眸", "今生他是谁", "挽留你的歌", "残缺韵律", "七分少女心.", "海以南不再蓝.", "一夕夙愿", "落满城月", "浮生若伤", "共茶谈笑", "来日岁月", "浮生尽歇", "及故人", "空城旧人", "红颜一笑醉倾城", "西北望长安", "墨袖留香", "轻纱青衣", "情之所钟", "右耳离心", "衣香鬓影")): Any {
        val sourceList = userDao.findAll()
        val phone: Long = (sourceList.map { it.phone!!.toInt() }.max()?.toLong() ?: 13760029486) + 1
        try {
            val headFolder = File(headPath)
            val headNameList = headFolder.list()
            val backFolder = File(backPath)
            val backNameList = backFolder.list()

            headNameList.toList().filter { it.toLowerCase().endsWith(".png") || it.toLowerCase().endsWith(".jpg") || it.toLowerCase().endsWith(".jpeg")}
            backNameList.toList().filter { it.toLowerCase().endsWith(".png") || it.toLowerCase().endsWith(".jpg") || it.toLowerCase().endsWith(".jpeg")}

            val count = if (headNameList.size > backNameList.size) {
                backNameList.size
            } else {
                headNameList.size
            }
            val userList = mutableListOf<User>()
            for (i in 0 until count) {
                val user = User()
                if (i % 2 == 0) {
                    user.gender = Gender.FEMALE
                }
                user.phone = phone.toString()
                user.password = "123456"
                user.name = nameList?.toMutableList()!!.shuffled().first()
                user.head = headNameList[i]
                user.head = backNameList[i]
                userList.add(userDao.save(user))
            }
            return userList
        } catch (e: Exception) {
            println(e.message)
            throw e
        }
    }


    @PostMapping("save")
    fun save(path: String): Boolean {
        val userList = userDao.findAll()
        var i = 0;
        try {
            val file = File(path)
            val fileNameList = file.list()

            for (fileName in fileNameList) {
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    userList[i].background = fileName
                    userDao.save(userList[i])
                    i++;
                }
            }
            return true
        } catch (e: Exception) {
            println(i)
            println(e.message)
            throw e
        }
    }
}