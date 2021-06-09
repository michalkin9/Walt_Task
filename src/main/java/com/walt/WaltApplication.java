package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
//import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class WaltApplication {

    private static final Logger log = LoggerFactory.getLogger(WaltApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WaltApplication.class);
    }

//    @Bean
//    public CommandLineRunner demo(DeliveryRepository deliveryRepository,CustomerRepository customerRepository, CityRepository cityRepository, DriverRepository driverRepository, RestaurantRepository restaurantRepository) {
//        return (args) -> {
//            // save a few customers
//            City jerusalem = new City("Jerusalem");
//            City tlv = new City("Tel-Aviv");
//            City bash = new City("Beer-Sheva");
//            City haifa = new City("Haifa");
//
//            cityRepository.save(jerusalem);
//            cityRepository.save(tlv);
//            cityRepository.save(bash);
//            cityRepository.save(haifa);
//
//            Restaurant meat = new Restaurant("meat", jerusalem, "All meat restaurant");
//            Restaurant vegan = new Restaurant("vegan", tlv, "Only vegan");
//            Restaurant cafe = new Restaurant("cafe", tlv, "Coffee shop");
//            Restaurant chinese = new Restaurant("chinese", tlv, "chinese restaurant");
//            Restaurant mexican = new Restaurant("restaurant", tlv, "mexican restaurant ");
//
//            restaurantRepository.saveAll(Lists.newArrayList(meat, vegan, cafe, chinese, mexican));
//
//            Customer beethoven = new Customer("Beethoven", tlv, "Ludwig van Beethoven");
//            Customer mozart = new Customer("Mozart", jerusalem, "Wolfgang Amadeus Mozart");
//            Customer chopin = new Customer("Chopin", haifa, "Frédéric François Chopin");
//            Customer rachmaninoff = new Customer("Rachmaninoff", tlv, "Sergei Rachmaninoff");
//            Customer bach = new Customer("Bach", tlv, "Sebastian Bach. Johann");
//
//            customerRepository.saveAll(Lists.newArrayList(beethoven, mozart, chopin, rachmaninoff, bach));
//
//            Driver mary = new Driver("Mary", tlv);
//            Driver patricia = new Driver("Patricia", tlv);
//            Driver jennifer = new Driver("Jennifer", haifa);
//            Driver james = new Driver("James", bash);
//            Driver john = new Driver("John", bash);
//            Driver robert = new Driver("Robert", jerusalem);
//            Driver david = new Driver("David", jerusalem);
//            Driver daniel = new Driver("Daniel", tlv);
//            Driver noa = new Driver("Noa", haifa);
//            Driver ofri = new Driver("Ofri", haifa);
//            Driver nata = new Driver("Neta", jerusalem);
//
//            driverRepository.saveAll(Lists.newArrayList(mary, patricia, jennifer, james, john, robert, david, daniel, noa, ofri, nata));
//            Date n = new Date();
//            Delivery d = new Delivery(mary, meat, mozart, n);
//            d.setDistance(10);
//            Delivery d2 = new Delivery(mary, meat, mozart, n);
//            Delivery d3 = new Delivery(patricia, meat, mozart, n);
//            Delivery d4 = new Delivery(patricia, meat, mozart, n);
//            d3.setDistance(5);
//            d4.setDistance(8);
//            d2.setDistance(20);
//            deliveryRepository.saveAll(Lists.newArrayList(d,d2,d3,d4));
//        };
//    }
        }
