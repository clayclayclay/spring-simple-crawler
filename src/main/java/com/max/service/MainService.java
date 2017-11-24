package com.max.service;

import com.max.constants.ExtractConstants;
import com.max.constants.ParameterConstants;
import com.max.constants.UrlConstants;
import com.max.domain.entity.Hotel;
import com.max.domain.entity.HotelRoom;
import com.max.repository.HotelRepository;
import com.max.repository.HotelRoomRepository;
import com.max.util.DateUtil;
import com.max.util.RequestUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.*;


/**
 * other tools:
 * 神箭手
 * nute
 * crawler4j
 * other tools
 * 迅代理
 * https://www.zhihu.com/question/31427895
 */
@Service
public class MainService implements PageProcessor {


    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    private String cityId;
    private String cityName;

    /**
     * select field:
     * 酒店名
     * 酒店ID
     * 地址
     *
     * 电话
     * 房型
     * 价格
     */

    private static int offset = 0;
    private static int limit = 20;
    private static int amount = 1000;

    private Site site = Site.me()
            .setSleepTime(1000);
//            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
//            .setCharset("UTF-8")
//            .addHeader("Host", "ihotel.meituan.com")
//            .addHeader("Origin", "http://hotel.meituan.com")
//            .addHeader("Upgrade-Insecure-Requests", "1")
//            .addCookie("IJSESSIONID", "1ikmewgjlt7p77wsyvmhy61e7")
//            .addCookie("cityname", "%E5%8C%97%E4%BA%AC")
//            .addCookie("ci", "1")
//            .addCookie("uuid", "693a6a3d4ae448a89a17.1511504269.1.0.0")
//            .addCookie("iuuid", "621BFAB4308D43DE0A3EFBBF8CA0401A7769006EC590DC1F9EE848771D22E946")
//            .addCookie("_lxsdk_cuid", "15fecacc0aa72-008189a26f820a-7b113d-1fa400-15fecacc0abc8")
//            .addCookie("_lxsdk_s", "15fed44645b-50b-191-f2c%7C%7C3")
//            .addCookie("_mta", "53214999.1511506788557.1511506788557.1511506788557.1")
//            .addCookie("rvct", "1");


    @Override
    public Site getSite()
    {
        return site;
    }


    public void process(Page page) {

        Map<String, String> cookie = page.getRequest().getCookies();
        System.out.println(cookie);

        if (page.getUrl().regex(UrlConstants.HOTEL_LIST).match())
        {
            if (offset < amount)
            {
                page.addTargetRequests(getHotelDetailRequestForGet(page.getJson().jsonPath(ExtractConstants.HOTEL_DETAIL_JPATH).all()));
                page.addTargetRequest(RequestUrlUtil.getHotelListRequestForGet(offset += 20, limit, getCityId()));
            }
            page.setSkip(true);
        }
        else if (page.getUrl().regex(ExtractConstants.HOTEL_DETAIL_REGEX).match())
        {
            Hotel hotel = getBasicHotelDetail(page);
            showHotelData(hotel);
            hotelRepository.save(hotel);

//          add request for house rooms
            page.addTargetRequest(getHouseRoomRequestForGet(hotel.getSellerId()));
            page.setSkip(true);
        }
        else if (page.getUrl().regex(UrlConstants.HOTEL_ROOM_ROOT).match())
        {
            List<HotelRoom> rooms = getHouseRooms(page);
            hotelRoomRepository.save(rooms);
            showHotelRoom(rooms);
            page.setSkip(true);
        }
    }

    private Hotel getBasicHotelDetail(Page page)
    {
        Hotel hotel = new Hotel();
        //get seller id
        hotel.setSellerId(getSellerId(page));
        //get hotel name
        hotel.setHotelName(getHotelName(page));
        //get hotel address
        hotel.setHotelAddress(getHotelAddress(page));
        //get hotel telephone
        hotel.setTelephone(getHotelTelephone(page));
        //get city
        hotel.setCity(getCityName());
        return hotel;
    }

    private String getSellerId(Page page)
    {
        return page.getUrl().regex(ExtractConstants.HOTEL_ID_REGEX).get();
    }

    private String getHotelName(Page page)
    {
        return page.getHtml().xpath(ExtractConstants.HOTEL_NAME_XPATH).get();
    }


    private String getHotelAddress(Page page)
    {
        return page.getHtml().xpath(ExtractConstants.HOTEL_ADDRESS_XPATH).get();
    }

    private String getHotelTelephone(Page page)
    {
        return page.getHtml().xpath(ExtractConstants.HOTEL_TELEPHONE_XPATH).get();
    }

    private List<String> getHouseLayouts(Page page)
    {
        return page.getJson().jsonPath(ExtractConstants.HOTEL_ROOM_NAME_JPATH).all();
    }

    private List<HotelRoom> getHouseRooms(Page page)
    {
        Json json = page.getJson();
        System.out.println(json.get());
        List<String> roomIds = page.getJson().jsonPath(ExtractConstants.HOTEL_ROOM_ID_JPATH).all();
        List<String> roomPrices = page.getJson().jsonPath(ExtractConstants.HOTEL_ROOM_PRICE_JPATH).all();
        List<String> roomNames = getHouseLayouts(page);
        String poid = getHousePoid(page);
        List<HotelRoom> rooms = new ArrayList<>();
        for (int i = 0; i < roomIds.size(); i++)
        {
            HotelRoom room = new HotelRoom();
            room.setRoomName(roomNames.get(i));
            room.setRoomId(roomIds.get(i));
            room.setRoomPrice(roomPrices.get(i).substring(0, roomPrices.get(i).length() - 2));
            room.setSellerId(poid);
            room.setFromDate(DateUtil.getTodayDateString(DateUtil.DATE_FORMAT_1));
            room.setEndDate(DateUtil.getTomorrowDateString(DateUtil.DATE_FORMAT_1));
            rooms.add(room);
        }
        return rooms;
    }


    private String getHousePoid(Page page)
    {
        return page.getJson().jsonPath(ExtractConstants.HOTEL_POID_JPATH).get();
    }



    private void showHotelData(Hotel hotelDetail)
    {
        System.out.println(hotelDetail);
        System.out.println();
    }

    private void showHotelRoom(List<HotelRoom> rooms)
    {
        for (HotelRoom room : rooms)
        {
            System.out.println(room);
            System.out.println();
        }
    }

    /**
     *
     * @param poids
     * @return
     */
    private List<String> getHotelDetailRequestForGet(List<String> poids)
    {
        List<String> hotelDetailUrls = new ArrayList<String>(poids.size());
        for (String poid : poids)
        {
            hotelDetailUrls.add(new String(UrlConstants.HOTEL_DETAIL + poid));
        }
        return hotelDetailUrls;
    }

    public String getHouseRoomRequestForGet(String poiId)
    {
        StringBuffer houseRoomUrl = new StringBuffer();
        houseRoomUrl.append(UrlConstants.HOTEL_ROOM_ROOT);
        houseRoomUrl.append(ParameterConstants.TYPE_NAME + "=" + ParameterConstants.TYPE_VALUE + "&");
        houseRoomUrl.append(ParameterConstants.UTM_MEDIUM_NAME + "=" + ParameterConstants.UTM_MEDIUM_VALUE + "&");
        houseRoomUrl.append(ParameterConstants.VERSION_NAME + "=" + ParameterConstants.VERSION_VALUE + "&");
        houseRoomUrl.append(ParameterConstants.POI_ID_NAME + "=" + poiId + "&");
        houseRoomUrl.append(ParameterConstants.START_NAME + "=" + DateUtil.getTodayTimestampString()+"&");
        houseRoomUrl.append(ParameterConstants.END_NAME + "=" + DateUtil.getTomorrowTimestampString()+"&");
        houseRoomUrl.append(ParameterConstants.TOKEN_NAME + "=" + ParameterConstants.TOKEN_VALUE);
        return houseRoomUrl.toString();
    }

    /**
     *
     * @return
     */
//    public Request getHouseRoomRequestsForGet(String poiId)
//    {
//        Request request = new Request();
//        request.setUrl(getHouseRoomRequestForGet(poiId));
//        Map<String, String> cookieData = getSite().getCookies();
//        Set<Map.Entry<String, String>> cookies = cookieData.entrySet();
//        Iterator<Map.Entry<String, String>> iterable1 = cookies.iterator();
//        while (iterable1.hasNext())
//        {
//            Map.Entry<String, String> cookie = iterable1.next();
//            request.addCookie(cookie.getKey(), cookie.getValue());
//        }
//
//        Map<String, String> headerData = getSite().getHeaders();
//        Set<Map.Entry<String, String>> headers = headerData.entrySet();
//        Iterator<Map.Entry<String, String>> iterable2 = headers.iterator();
//        while (iterable2.hasNext())
//        {
//            Map.Entry<String, String> cookie = iterable2.next();
//            request.addHeader(cookie.getKey(), cookie.getValue());
//        }
//        return request;
//    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
