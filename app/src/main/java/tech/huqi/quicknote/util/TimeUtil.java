package tech.huqi.quicknote.util;

/**
 * Created by hzhuqi on 2019/4/15
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 和时间以及日期操作相关的工具类
 */
public class TimeUtil {

    /**
     * 根据指定格式format返回当前系统时间
     *
     * @param format
     * @return
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }
}
