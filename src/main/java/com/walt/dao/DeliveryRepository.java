package com.walt.dao;

import com.walt.model.City;
import com.walt.model.Driver;
import com.walt.model.Delivery;
import com.walt.model.DriverDistance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    List<Delivery> getDeliveriesByDriver(Driver driver);

    Collection findDeliveriesByDriver(Driver driver);

    @Query("SELECT d.driver as driver,SUM(d.distance) as totalDistance FROM Delivery d GROUP BY d.driver.id ORDER BY totalDistance DESC ")
    List<DriverDistance> getDriverRankReport();

    @Query("SELECT d.driver as driver,SUM(d.distance) as totalDistance FROM Delivery d WHERE d.driver.city.id =?1 GROUP BY d.driver.id ORDER BY totalDistance DESC ")
    List<DriverDistance> getDriverRankReportByCity(Long cityId);

}


