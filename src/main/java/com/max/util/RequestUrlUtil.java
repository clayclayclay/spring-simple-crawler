package com.max.util;

public class RequestUrlUtil
{
    private final static String HOTEL_LIST_CHENG_DU = "https://ihotel.meituan.com/hbsearch/HotelSearch?";

    public static String getHotelListRequestForGet(int offset, int limit, String cityId)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(HOTEL_LIST_CHENG_DU);
        stringBuffer.append("utm_medium=pc&");
        stringBuffer.append("version_name=999.9&");
        stringBuffer.append("cateId=20&");
        stringBuffer.append("attr_28=129&");
        stringBuffer.append("uuid=1D8AEF3978E24416EAE94502EFAFE4EA6C5F2852AECDFA67DF987BBC8B938091%401511339179937&");
        stringBuffer.append("cityId=" + cityId + "&");
        stringBuffer.append("offset=" + offset + "&");
        stringBuffer.append("limit=" + limit + "&");
        stringBuffer.append("startDay=" + DateUtil.getTodayDateString(DateUtil.DATE_FORMAT_2) + "&");
        stringBuffer.append("endDay=" + DateUtil.getTomorrowDateString(DateUtil.DATE_FORMAT_2)+"&");
        stringBuffer.append("q=&");
        stringBuffer.append("sort=defaults");
        return stringBuffer.toString();
    }

}
