package com.max.repository;

import com.max.domain.entity.Hotel;
import com.max.domain.entity.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer>
{

}
