package com.junjie.secdracore.util


import java.security.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间处理工具类
 * @author fjj
 */
object DateUtil {

    // ==格式到年==
    /**
     * 日期格式，年份，例如：2004，2008
     */
    val DATE_FORMAT_YYYY = "yyyy"


    // ==格式到年月 ==
    /**
     * 日期格式，年份和月份，例如：200707，200808
     */
    val DATE_FORMAT_YYYYMM = "yyyyMM"

    /**
     * 日期格式，年份和月份，例如：200707，2008-08
     */
    val DATE_FORMAT_YYYY_MM = "yyyy-MM"


    // ==格式到年月日==
    /**
     * 日期格式，年月日，例如：050630，080808
     */
    val DATE_FORMAT_YYMMDD = "yyMMdd"

    /**
     * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
     */
    val DATE_FORMAT_YY_MM_DD = "yy-MM-dd"

    /**
     * 日期格式，年月日，例如：20050630，20080808
     */
    val DATE_FORMAT_YYYYMMDD = "yyyyMMdd"

    /**
     * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
     */
    val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"

    /**
     * 日期格式，年月日，例如：2016.10.05
     */
    val DATE_FORMAT_POINTYYYYMMDD = "yyyy.MM.dd"

    /**
     * 日期格式，年月日，例如：2016年10月05日
     */
    val DATE_TIME_FORMAT_YYYYMMDD_CHINESE = "yyyy年MM月dd日"


    // ==格式到年月日 时分 ==

    /**
     * 日期格式，年月日时分，例如：200506301210，200808081210
     */
    val DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm"

    /**
     * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
     */
    val DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm"

    /**
     * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
     */
    val DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm"


    // ==格式到年月日 时分秒==
    /**
     * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
     */
    val DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss"

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
     * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     */
    val DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss"


    // ==格式到年月日 时分秒 毫秒==
    /**
     * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
     */
    val DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS"


    // ==特殊格式==
    /**
     * 日期格式，月日时分，例如：10-05 12:00
     */
    val DATE_FORMAT_MMDDHHMI = "MM-dd HH:mm"


    /**
     * 获取某日期的年份
     *
     * @param date 日期
     * @return 年份
     */
    fun getYear(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.YEAR)
    }

    /**
     * 获取某日期的月份
     *
     * @param date 日期
     * @return 月份
     */
    fun getMonth(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.MONTH) + 1
    }

    /**
     * 获取某日期的天号
     *
     * @param date 日期
     * @return 天号
     */
    fun getDay(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DATE)
    }

    /**
     * 格式化Date时间
     *
     * @param time       Date类型时间
     * @param timeType String类型格式
     * @return 格式化后的字符串
     */
    fun parseDateToStr(time: Date, timeType: String): String {
        val dateFormat = SimpleDateFormat(timeType)
        return dateFormat.format(time)
    }

    /**
     * 格式化Timestamp时间
     *
     * @param timestamp Timestamp类型时间
     * @param timeType  String类型格式
     * @return 格式化后的字符串
     */
    fun parseTimestampToStr(timestamp: Timestamp, timeType: String): String {
        val df = SimpleDateFormat(timeType)
        return df.format(timestamp)
    }

    /**
     * 格式化Date时间
     *
     * @param time         Date类型时间
     * @param timeType     String类型格式
     * @param defaultValue 默认值为当前时间Date
     * @return 格式化后的字符串
     */
    fun parseDateToStr(time: Date, timeType: String, defaultValue: Date?): String {
        try {
            val dateFormat = SimpleDateFormat(timeType)
            return dateFormat.format(time)
        } catch (e: Exception) {
            return if (defaultValue != null)
                parseDateToStr(defaultValue, timeType)
            else
                parseDateToStr(Date(), timeType)
        }

    }

    /**
     * 格式化Date时间
     *
     * @param time         Date类型时间
     * @param timeType     String类型格式
     * @param defaultValue 默认时间值String类型
     * @return 格式化后的字符串
     */
    fun parseDateToStr(time: Date, timeType: String, defaultValue: String): String {
        try {
            val dateFormat = SimpleDateFormat(timeType)
            return dateFormat.format(time)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    /**
     * 格式化String时间
     *
     * @param time       String类型时间
     * @param timeType String类型格式
     * @return 格式化后的Date日期
     */
    fun parseStrToDate(time: String?, timeType: String): Date? {
        if (time == null || time == "") {
            return null
        }
        var date: Date? = null
        try {
            val dateFormat = SimpleDateFormat(timeType)
            date = dateFormat.parse(time)
        } catch (e: Exception) {
            println(e.message)
        }

        return date
    }

    /**
     * 格式化String时间
     *
     * @param strTime      String类型时间
     * @param timeType   String类型格式
     * @param defaultValue 异常时返回的默认值
     * @return 日期
     */
    fun parseStrToDate(strTime: String, timeType: String,
                       defaultValue: Date): Date {
        try {
            val dateFormat = SimpleDateFormat(timeType)
            return dateFormat.parse(strTime)
        } catch (e: Exception) {
            return defaultValue
        }

    }

    /**
     * 当strTime为2008-9时返回为2008-9-1 00:00格式日期时间，无法转换返回null.
     *
     * @param strTime  不完整String类型时间
     * @return 完整的日期
     */
    fun strToDate(strTime: String?): Date? {
        if (strTime == null || strTime.trim { it <= ' ' }.length <= 0)
            return null

        var date: Date? = null
        val list = ArrayList<String>(0)

        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS)
        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI)
        list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI)
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS)
        list.add(DATE_FORMAT_YYYY_MM_DD)
        //list.add(DATE_FORMAT_YY_MM_DD);
        list.add(DATE_FORMAT_YYYYMMDD)
        list.add(DATE_FORMAT_YYYY_MM)
        list.add(DATE_FORMAT_YYYYMM)
        list.add(DATE_FORMAT_YYYY)

        for (format in list) {
            if (strTime.indexOf("-") > 0 && !format.contains("-"))
                continue
            if (!strTime.contains("-") && format.indexOf("-") > 0)
                continue
            if (strTime.length > format.length)
                continue
            date = parseStrToDate(strTime, format)
            if (date != null)
                break
        }

        return date
    }

    /**
     * 解析两个日期之间的所有月份
     *
     * @param beginDateStr 开始日期，至少精确到yyyy-MM
     * @param endDateStr   结束日期，至少精确到yyyy-MM
     * @return yyyy-MM日期集合
     */
    fun getMonthListOfDate(beginDateStr: String, endDateStr: String): List<String>? {
        // 指定要解析的时间格式
        val f = SimpleDateFormat("yyyy-MM")
        // 返回的月份列表
        var sRet = ""

        // 定义一些变量
        var beginDate: Date? = null
        var endDate: Date? = null

        var beginGC: GregorianCalendar? = null
        var endGC: GregorianCalendar? = null
        val list = ArrayList<String>()

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr)
            endDate = f.parse(endDateStr)

            // 设置日历
            beginGC = GregorianCalendar()
            beginGC.time = beginDate!!

            endGC = GregorianCalendar()
            endGC.time = endDate!!

            // 直到两个时间相同
            while (beginGC.time.compareTo(endGC.time) <= 0) {
                sRet = (beginGC.get(Calendar.YEAR).toString() + "-"
                        + (beginGC.get(Calendar.MONTH) + 1))
                list.add(sRet)
                // 以月为单位，增加时间
                beginGC.add(Calendar.MONTH, 1)
            }
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 解析两个日期段之间的所有日期
     *
     * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
     * @param endDateStr   结束日期  ，至少精确到yyyy-MM-dd
     * @return yyyy-MM-dd日期集合
     */
    fun getDayListOfDate(beginDateStr: String, endDateStr: String): List<String>? {
        // 指定要解析的时间格式
        val f = SimpleDateFormat("yyyy-MM-dd")

        // 定义一些变量
        var beginDate: Date? = null
        var endDate: Date? = null

        var beginGC: Calendar? = null
        var endGC: Calendar? = null
        val list = ArrayList<String>()

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr)
            endDate = f.parse(endDateStr)

            // 设置日历
            beginGC = Calendar.getInstance()
            beginGC!!.time = beginDate!!

            endGC = Calendar.getInstance()
            endGC!!.time = endDate!!
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            // 直到两个时间相同
            while (beginGC.time.compareTo(endGC.time) <= 0) {

                list.add(sdf.format(beginGC.time))
                // 以日为单位，增加时间
                beginGC.add(Calendar.DAY_OF_MONTH, 1)
            }
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获取当下年份指定前后数量的年份集合
     *
     * @param before 当下年份前年数
     * @param behind 当下年份后年数
     * @return 集合
     */
    fun getYearListOfYears(before: Int, behind: Int): List<Int>? {
        if (before < 0 || behind < 0) {
            return null
        }
        val list = ArrayList<Int>()
        var c: Calendar? = null
        c = Calendar.getInstance()
        c!!.time = Date()
        val currYear = Calendar.getInstance().get(Calendar.YEAR)

        val startYear = currYear - before
        val endYear = currYear + behind
        for (i in startYear until endYear) {
            list.add(i)
        }
        return list
    }

    /**
     * 获取当前日期是一年中第几周
     *
     * @param date 日期
     * @return 当前日期的第几周
     */
    fun getWeekOfYear(date: Date): Int {
        val c = GregorianCalendar()
        c.firstDayOfWeek = Calendar.MONDAY
        c.minimalDaysInFirstWeek = 7
        c.time = date

        return c.get(Calendar.WEEK_OF_YEAR)
    }

    /**
     * 获取某一年各星期的始终时间
     * 实例：getWeekList(2016)，第52周(从2016-12-26至2017-01-01)
     *
     * @param  year 年份
     * @return map
     */
    fun getWeekTimeOfYear(year: Int): HashMap<Int, String> {
        val map = LinkedHashMap<Int, String>()
        val c = GregorianCalendar()
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
        val count = getWeekOfYear(c.time)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var dayOfWeekStart = ""
        var dayOfWeekEnd = ""
        for (i in 1..count) {
            dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i))
            dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i))
            map[i] = "第" + i + "周(从" + dayOfWeekStart + "至" + dayOfWeekEnd + ")"
        }
        return map

    }

    /**
     * 获取某一年的总周数
     *
     * @param year 年份
     * @return 总周数
     */
    fun getWeekCountOfYear(year: Int): Int {
        val c = GregorianCalendar()
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
        return getWeekOfYear(c.time)
    }

    /**
     * 获取指定日期所在周的第一天
     *
     * @param date 日期
     * @return 周一的日期
     */
    fun getFirstDayOfWeek(date: Date): Date {
        val c = GregorianCalendar()
        c.firstDayOfWeek = Calendar.MONDAY
        c.time = date
        c.set(Calendar.DAY_OF_WEEK, c.firstDayOfWeek) // Monday
        return c.time
    }

    /**
     * 获取指定日期所在周的最后一天
     *
     * @param date 日期
     * @return 周日的日期
     */
    fun getLastDayOfWeek(date: Date): Date {
        val c = GregorianCalendar()
        c.firstDayOfWeek = Calendar.MONDAY
        c.time = date
        c.set(Calendar.DAY_OF_WEEK, c.firstDayOfWeek + 6) // Sunday
        return c.time
    }

    /**
     * 获取某年某周的第一天
     *
     * @param year 目标年份
     * @param week 目标周数
     * @return 日期
     */
    fun getFirstDayOfWeek(year: Int, week: Int): Date {
        val c = GregorianCalendar()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DATE, 1)

        val cal = c.clone() as GregorianCalendar
        cal.add(Calendar.DATE, week * 7)

        return getFirstDayOfWeek(cal.time)
    }

    /**
     * 获取某年某周的最后一天
     *
     * @param year 目标年份
     * @param week 目标周数
     * @return 日期
     */
    fun getLastDayOfWeek(year: Int, week: Int): Date {
        val c = GregorianCalendar()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, Calendar.JANUARY)
        c.set(Calendar.DATE, 1)

        val cal = c.clone() as GregorianCalendar
        cal.add(Calendar.DATE, week * 7)

        return getLastDayOfWeek(cal.time)
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return 日期
     */
    fun getFirstDayOfMonth(year: Int, month: Int): Date {
        var month = month
        month = month - 1
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)

        val day = c.getActualMinimum(Calendar.DAY_OF_MONTH)

        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.time
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return 日期
     */
    fun getLastDayOfMonth(year: Int, month: Int): Date {
        var month = month
        month = month - 1
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        val day = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.time
    }

    /**
     * 获取某个日期为星期几
     *
     * @param date 日期
     * @return String "星期*"
     */
    fun getDayWeekOfDate1(date: Date): String {
        val weekDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance()
        cal.time = date

        var w = cal.get(Calendar.DAY_OF_WEEK) - 1
        if (w < 0)
            w = 0

        return weekDays[w]
    }

    /**
     * 获得指定日期的星期几数
     *
     * @param date 日期
     * @return int
     */
    fun getDayWeekOfDate2(date: Date): Int {
        val aCalendar = Calendar.getInstance()
        aCalendar.time = date
        return aCalendar.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * 验证字符串是否为日期
     * 验证格式:YYYYMMDD、YYYY_MM_DD、YYYYMMDDHHMISS、YYYYMMDD_HH_MI、YYYY_MM_DD_HH_MI、YYYYMMDDHHMISSSSS、YYYY_MM_DD_HH_MI_SS
     *
     * @param strTime 验证字符串
     * @return null时返回false;true为日期，false不为日期
     */
    fun validateIsDate(strTime: String?): Boolean {
        if (strTime == null || strTime.trim { it <= ' ' }.length <= 0)
            return false

        var date: Date? = null
        val list = ArrayList<String>(0)

        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS)
        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI)
        list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI)
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS)
        list.add(DATE_FORMAT_YYYY_MM_DD)
        //list.add(DATE_FORMAT_YY_MM_DD);
        list.add(DATE_FORMAT_YYYYMMDD)
        //list.add(DATE_FORMAT_YYYY_MM);
        //list.add(DATE_FORMAT_YYYYMM);
        //list.add(DATE_FORMAT_YYYY);

        for (format in list) {
            if (strTime.indexOf("-") > 0 && !format.contains("-"))
                continue
            if (!strTime.contains("-") && format.indexOf("-") > 0)
                continue
            if (strTime.length > format.length)
                continue
            date = parseStrToDate(strTime.trim { it <= ' ' }, format)
            if (date != null)
                break
        }

        if (date != null) {
            println("生成的日期:" + DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS, "--null--"))
            return true
        }
        return false
    }


    /**
     * 获得指定时间加减参数后的日期(不计算则输入0)
     *
     * @param date        指定日期
     * @param year        年数，可正可负
     * @param month       月数，可正可负
     * @param day         天数，可正可负
     * @param hour        小时数，可正可负
     * @param minute      分钟数，可正可负
     * @param second      秒数，可正可负
     * @param millisecond 毫秒数，可正可负
     * @return 计算后的日期
     */
    fun addDate(date: Date, year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.YEAR, year)//加减年数
        c.add(Calendar.MONTH, month)//加减月数
        c.add(Calendar.DATE, day)//加减天数
        c.add(Calendar.HOUR, hour)//加减小时数
        c.add(Calendar.MINUTE, minute)//加减分钟数
        c.add(Calendar.SECOND, second)//加减秒
        c.add(Calendar.MILLISECOND, millisecond)//加减毫秒数

        return c.time
    }

    /**
     * 获得两个日期的时间戳之差
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间戳之差
     */
    fun getDistanceTimestamp(startDate: Date, endDate: Date): Long {
        return (endDate.time - startDate.time + 1000000) / (3600 * 24 * 1000)
    }

    /**
     * 判断二个时间是否为同年同月
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 是否
     */
    fun compareIsSameMonth(date1: Date, date2: Date): Boolean? {
        var flag = false
        val year1 = getYear(date1)
        val year2 = getYear(date2)
        if (year1 == year2) {
            val month1 = getMonth(date1)
            val month2 = getMonth(date2)
            if (month1 == month2) flag = true
        }
        return flag
    }

    /**
     * 获得两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param date1 时间参数 1 格式：1990-01-01 12:00:00
     * @param date2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    fun getDistanceTime(date1: Date, date2: Date): LongArray {
        var day: Long = 0
        var hour: Long = 0
        var min: Long = 0
        var sec: Long = 0
        try {

            val time1 = date1.time
            val time2 = date2.time
            val diff: Long
            if (time1 < time2) {
                diff = time2 - time1
            } else {
                diff = time1 - time2
            }
            day = diff / (24 * 60 * 60 * 1000)
            hour = diff / (60 * 60 * 1000) - day * 24
            min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
            sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return longArrayOf(day, hour, min, sec)
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param date1 时间参数 1 格式：1990-01-01 12:00:00
     * @param date2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：{天, 时, 分, 秒}
     */
    fun getDistanceTime(date1: String, date2: String): LongArray {
        val df = SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)
        val one: Date
        val two: Date
        var day: Long = 0
        var hour: Long = 0
        var min: Long = 0
        var sec: Long = 0
        try {
            one = df.parse(date1)
            two = df.parse(date2)
            val time1 = one.time
            val time2 = two.time
            val diff: Long
            if (time1 < time2) {
                diff = time2 - time1
            } else {
                diff = time1 - time2
            }
            day = diff / (24 * 60 * 60 * 1000)
            hour = diff / (60 * 60 * 1000) - day * 24
            min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
            sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return longArrayOf(day, hour, min, sec)
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param date1 时间参数 1：
     * @param date2 时间参数 2：
     * @return 相差天数
     */
    @Throws(Exception::class)
    fun getDistanceDays(date1: String, date2: String): Long? {
        val df = SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)
        val one: Date
        val two: Date
        var days: Long = 0
        try {
            one = df.parse(date1)
            two = df.parse(date2)
            val time1 = one.time
            val time2 = two.time
            val diff: Long
            if (time1 < time2) {
                diff = time2 - time1
            } else {
                diff = time1 - time2
            }
            days = diff / (1000 * 60 * 60 * 24)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return days
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date 指定日期
     * @return 当天开始时间
     */
    fun getDayBeginTime(date: Date): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.time
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date 指定日期
     * @return 当天结束时间
     */
    fun getDayEndTime(date: Date): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.time
    }
}