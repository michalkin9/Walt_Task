package com.walt;

import com.walt.dao.*;
import com.walt.model.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WaltTest {

    @TestConfiguration
    static class WaltServiceImplTestContextConfiguration {

        @Bean
        public WaltService waltService() {
            return new WaltServiceImpl();
        }
    }

    @Autowired
    WaltService waltService;

    @Resource
    CityRepository cityRepository;

    @Resource
    CustomerRepository customerRepository;

    @Resource
    DriverRepository driverRepository;

    @Resource
    DeliveryRepository deliveryRepository;

    @Resource
    RestaurantRepository restaurantRepository;

    @BeforeEach()
    public void prepareData() {

        City jerusalem = new City("Jerusalem");
        City tlv = new City("Tel-Aviv");
        City bash = new City("Beer-Sheva");
        City haifa = new City("Haifa");

        cityRepository.save(jerusalem);
        cityRepository.save(tlv);
        cityRepository.save(bash);
        cityRepository.save(haifa);

        createDrivers(jerusalem, tlv, bash, haifa);

        createCustomers(jerusalem, tlv, haifa, bash);

        createRestaurant(jerusalem, tlv, bash);

        creatDeliveries();
    }

    private void createRestaurant(City jerusalem, City tlv, City bash) {
        Restaurant meat = new Restaurant("meat", jerusalem, "All meat restaurant");
        Restaurant vegan = new Restaurant("vegan", tlv, "Only vegan");
        Restaurant cafe = new Restaurant("cafe", tlv, "Coffee shop");
        Restaurant chinese = new Restaurant("chinese", tlv, "chinese restaurant");
        Restaurant mexican = new Restaurant("restaurant", tlv, "mexican restaurant ");
        Restaurant sushi = new Restaurant("sushi", bash, "sushi restaurant ");
        restaurantRepository.saveAll(Lists.newArrayList(sushi, meat, vegan, cafe, chinese, mexican));
    }

    private void createCustomers(City jerusalem, City tlv, City haifa, City bash) {

        Customer beethoven = new Customer("Beethoven", tlv, "Ludwig van Beethoven");
        Customer mozart = new Customer("Mozart", jerusalem, "Wolfgang Amadeus Mozart");
        Customer chopin = new Customer("Chopin", haifa, "Frédéric François Chopin");
        Customer rachmaninoff = new Customer("Rachmaninoff", tlv, "Sergei Rachmaninoff");
        Customer bach = new Customer("Bach", tlv, "Sebastian Bach. Johann");
        Customer michal = new Customer("Michal", bash, "Sebastian Bach. Johann");
        customerRepository.saveAll(Lists.newArrayList(michal, beethoven, mozart, chopin, rachmaninoff, bach));
    }

    private void createDrivers(City jerusalem, City tlv, City bash, City haifa) {
        Driver mary = new Driver("Mary", tlv);
        Driver patricia = new Driver("Patricia", tlv);
        Driver jennifer = new Driver("Jennifer", haifa);
        Driver james = new Driver("James", bash);
        Driver john = new Driver("John", bash);
        Driver robert = new Driver("Robert", jerusalem);
        Driver david = new Driver("David", jerusalem);
        Driver daniel = new Driver("Daniel", tlv);
        Driver noa = new Driver("Noa", haifa);
        Driver ofri = new Driver("Ofri", haifa);
        Driver nata = new Driver("Neta", jerusalem);

        driverRepository.saveAll(Lists.newArrayList(mary, patricia, jennifer, james, john, robert, david, daniel, noa, ofri, nata));
    }

    private void creatDeliveries() {

        Customer mozartCustomer = customerRepository.findByName("mozart"); //tlv
        Customer michalCustomer = customerRepository.findByName("Michal");//bash

        Restaurant testRestaurant = restaurantRepository.findByName("vegan"); //tlv
        Restaurant bashRestaurant = restaurantRepository.findByName("sushi"); //bash

        Driver maryDriver = driverRepository.findByName("Mary"); //tlv
        Driver patriciaDriver = driverRepository.findByName("Patricia"); //tlv
        Driver jamesdriverBash = driverRepository.findByName("James");//bash

        Date n = new Date(2021, 06, 9, 15, 00, 00);
        Delivery d = new Delivery(maryDriver, testRestaurant, mozartCustomer, n);
        d.setDistance(10);
        Delivery d2 = new Delivery(maryDriver, testRestaurant, mozartCustomer, n);
        d2.setDistance(15);

        Delivery d3 = new Delivery(patriciaDriver, testRestaurant, mozartCustomer, n);
        Delivery d4 = new Delivery(patriciaDriver, testRestaurant, mozartCustomer, n);
        d3.setDistance(12);
        d4.setDistance(3);
        Delivery d5 = new Delivery(jamesdriverBash, bashRestaurant, michalCustomer, n);
        d5.setDistance(1);
        //Mary - 25
        //patrica - 15
        //james -1

        deliveryRepository.saveAll(Lists.newArrayList(d5, d, d3, d2, d4));
    }

    @Test
    public void testBasics() {

        assertEquals(((List<City>) cityRepository.findAll()).size(), 4);
        assertEquals((driverRepository.findAllDriversByCity(cityRepository.findByName("Beer-Sheva")).size()), 2);
    }

    @Test
    void checkErrorForDifferentCityInOrder() {
        Customer testCustomer = customerRepository.findByName("Beethoven");
        Restaurant testRestaurant = restaurantRepository.findByName("meat");
        Date date = new Date();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            waltService.createOrderAndAssignDriver(testCustomer, testRestaurant, date);
        });

        String expectedMessage = "customer should only order from the city he lives in.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkInvalidInput() {
        Customer testCustomer = customerRepository.findByName("Beethoven");
        //restaurant should be in database. there for would be exception.
        Restaurant testRestaurant = restaurantRepository.findByName("pizza");
        Date date = new Date();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            waltService.createOrderAndAssignDriver(testCustomer, testRestaurant, date);
        });

        String expectedMessage = "Invalid input";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkNoAvailableDriver() {
        Customer testCustomer1 = customerRepository.findByName("Michal");
        Restaurant restaurant = restaurantRepository.findByName("sushi");
        Date date = new Date(2021, 06, 9, 17, 00, 00);

        Delivery delivery1 = waltService.createOrderAndAssignDriver(testCustomer1, restaurant, date);
        Delivery delivery2 = waltService.createOrderAndAssignDriver(testCustomer1, restaurant, date);

        List<Delivery> deliveries = (List<Delivery>) deliveryRepository.findAll();
        deliveries.size();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            waltService.createOrderAndAssignDriver(testCustomer1, restaurant,
                    date);
            List<Delivery> deliveries2 = (List<Delivery>) deliveryRepository.findAll();
            deliveries2.size();

        });

        String expectedMessage = "There is no driver available";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateOrder() {
        Restaurant restaurant = restaurantRepository.findByName("meat");
        Customer customer = customerRepository.findByName("Mozart");
        Date date = new Date(2021, 06, 8, 05, 00);
        Delivery delivery = waltService.createOrderAndAssignDriver(customer, restaurant, date);
        assertEquals(driverRepository.findByName("Robert").getId(), delivery.getDriver().getId());
    }

    @Test
    void testRankByCityTlv() {
        Driver maryDriver = driverRepository.findByName("Mary"); //tlv
        Driver patriciaDriver = driverRepository.findByName("Patricia"); //tlv

        List<DriverDistance> l = waltService.getDriverRankReportByCity(cityRepository.findByName("Tel-Aviv"));
        assertEquals(2, l.size());
        assertEquals(l.get(0).getDriver().getId(), maryDriver.getId());
        assertEquals(l.get(0).getTotalDistance(), new Long(25));
        assertEquals(l.get(1).getDriver().getId(), patriciaDriver.getId());
        assertEquals(l.get(1).getTotalDistance(), new Long(15));

    }

    @Test
    void testRankByCityBash() {
        Driver driverBash = driverRepository.findByName("James");
        List<DriverDistance> l = waltService.getDriverRankReportByCity(cityRepository.findByName("Beer-Sheva"));
        assertEquals(1, l.size());
        assertEquals(l.get(0).getDriver().getId(), driverBash.getId());
        assertEquals(l.get(0).getTotalDistance(), new Long(1));
    }

    @Test
    void testRankByCityWithNoOrders() {

        List<DriverDistance> l = waltService.getDriverRankReportByCity(cityRepository.findByName("Jerusalem"));
        assertEquals(0, l.size());

    }


    @Test
    void testRankReport() {
        List<DriverDistance> l = waltService.getDriverRankReport();
        assertEquals(3,l.size());

        //check the list is sorted by descending order of the total distance.
        boolean isDescOrder = true;
        for (int i = 0; i < l.size() - 1; i++) {
            //each next total distance should be less then the current.
            if (l.get(i).getTotalDistance() < l.get(i + 1).getTotalDistance()) {
                isDescOrder = false;
            }
        }

        assertTrue(isDescOrder);
    }

    @Test
    void lessBusyDriver() {
        // by adding a delivery for john driver -> he is the most busy driver.
        Customer michalCustomer = customerRepository.findByName("Michal");
        Restaurant bashRestaurant = restaurantRepository.findByName("sushi"); //bash
        Driver johnDriver = driverRepository.findByName("John"); //bash
        Date date = new Date(2021, 06, 10, 12, 00);
        Delivery delivery = new Delivery(johnDriver, bashRestaurant, michalCustomer, date);
        delivery.setDistance(12);
        deliveryRepository.save(delivery);
        Date anotherDate = new Date(2021, 06, 11, 13, 00);

        Delivery deliverySameDate = waltService.createOrderAndAssignDriver(michalCustomer, bashRestaurant, anotherDate);
        assertEquals("James", deliverySameDate.getDriver().getName());


    }
}
