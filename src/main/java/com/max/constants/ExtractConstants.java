package com.max.constants;

public interface ExtractConstants
{

    String HOTEL_DETAIL_REGEX = "http://hotel.meituan.com/\\d+";

    String HOTEL_NAME_XPATH = "//span[@class='fs26 fc3 pull-left bold']/text()";
    String HOTEL_ID_REGEX = "\\d+";
    String HOTEL_ADDRESS_XPATH = "//div[@class='fs12 mt6 mb10']/span/text()";
    String HOTEL_TELEPHONE_XPATH = "//div[@class='ml20 mr20 pt20 pb20 bor-bottom clear']/dd/span/text()";

    String HOTEL_DETAIL_JPATH = "$.ct_pois[*].poiid";
    String HOTEL_ROOM_NAME_JPATH = "$.mergeList.data[*].aggregateGoods[*].prepayGood.goodsRoomModel.roomName";
    String HOTEL_ROOM_ID_JPATH = "$.mergeList.data[*].aggregateGoods[*].prepayGood.goodsRoomModel.roomId";
    String HOTEL_ROOM_PRICE_JPATH = "$.mergeList.data[*].aggregateGoods[*].prepayGood.averagePrice";
    String HOTEL_POID_JPATH = "$.mergeList.data[*].aggregateGoods[*].prepayGood.goodsRoomModel.poiId";

}
