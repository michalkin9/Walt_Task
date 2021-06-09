package com.walt;

import com.walt.dao.CityRepository;
import com.walt.dao.DeliveryRepository;
import com.walt.dao.DriverRepository;
import com.walt.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class WaltServiceImpl implements WaltService {

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    DeliveryRepository deliveryRepository;

    @Override
    public Delivery createOrderAndAssignDriver(Customer customer, Restaurant restaurant, Date deliveryTime) {

        checkDeliveryRequest(customer,restaurant,deliveryTime);
        List<Driver> availableDrivers =  getAvailableDrivers(restaurant,deliveryTime);
        if(availableDrivers.size() == 0)  throw new RuntimeException("There is no driver available");
        Driver availableDriver = getLessBusyDriver(availableDrivers);
        Delivery delivery = new Delivery(availableDriver,restaurant,customer,deliveryTime);
        delivery.setDistance(Math.random()*20);
        deliveryRepository.save(delivery);
        return delivery;
    }

    public List<Driver> getAvailableDrivers(Restaurant restaurant, Date deliveryTime){
        List<Driver> availableDrivers = new ArrayList<>();
        for(Driver driver : driverRepository.findAllDriversByCity(restaurant.getCity())){
            if(isDriverAvailable(driver,deliveryTime)){
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    public void checkDeliveryRequest(Customer customer, Restaurant restaurant, Date deliveryTime){
        if(customer==null || restaurant == null || deliveryTime == null){
            throw new RuntimeException("Invalid input");
        }
        if(!(customer.getCity().getId()==(restaurant.getCity().getId()))){
            throw new RuntimeException("customer should only order from the city he lives in.");
        }
    }

    public boolean isDriverAvailable(Driver driver,Date deliveryTime){
        List<Delivery> deliveries = deliveryRepository.getDeliveriesByDriver(driver);
        for(Delivery delivery:deliveries){
            if(deliveryTime.equals(delivery.getDeliveryTime()))
                return false; //if time equals, then the driver is occupied.
        }
        return true;
    }

    public Driver getLessBusyDriver(List<Driver> driversFromCity){
        Collections.sort(driversFromCity, Comparator.comparingDouble(this::calculateSumKm));
        return driversFromCity.get(0);
    }

    public double calculateSumKm(Driver driver){
        double totalKm = 0;
        List<Delivery> deliveries = deliveryRepository.getDeliveriesByDriver(driver);
        if (!deliveries.isEmpty()) {
            for(Delivery delivery:deliveries){
                totalKm += delivery.getDistance();
            }
        }
        return totalKm;
    }

    @Override
    public List<DriverDistance> getDriverRankReport() {
        return deliveryRepository.getDriverRankReport();
    }

    @Override
    public List<DriverDistance> getDriverRankReportByCity(City city) {
       return deliveryRepository.getDriverRankReportByCity(city.getId());
    }
}
