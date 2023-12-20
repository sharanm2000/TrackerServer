package com.ambulance.tracker;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RestDemoController {
//	 public double lat1 = 13.1334203; 
//	  public double lon1 = 80.289173; 

    @Autowired
    FirebaseService firebase;


    @PostMapping("/getUserDetails")
    public Vehicles getExample(@RequestHeader() String name) throws InterruptedException, ExecutionException {
        System.out.println(name);
        return firebase.getUserDetails(name);
    }


    //http://192.168.29.72:8080/getVehiclesUpdate.
    //@CrossOrigin(origins = "http://127.0.0.1:4200/")
    @GetMapping("/getVehiclesUpdate")
    public List<Vehicles> getVehiclesUpdate() throws InterruptedException, ExecutionException {

        return firebase.getVehiclesupdate();
    }

    @PostMapping("/createUser")
    public String postExample(@RequestBody Vehicles person) throws InterruptedException, ExecutionException {
        return firebase.saveUserDetails(person);
    }

    @PostMapping("/AddAmbulance")
    public String addAmbulance(@RequestBody Ambulance ambulance) throws InterruptedException, ExecutionException {
        System.out.println("calling AddAmbulance");
        return firebase.saveAmbulance(ambulance);

    }

   // @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping("/AddDriver")
    public String addDriver(@RequestBody Drivers driver) throws InterruptedException, ExecutionException {
        return firebase.setAddDriver(driver);

    }

    @PutMapping("/updateUser")
    public String putExample(@RequestBody Vehicles person) throws InterruptedException, ExecutionException {
        return "Updated User" + person.getName();
    }

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/GetAllAmbulances")
    public List<Ambulance> getAllAmbulancesData() throws ExecutionException, InterruptedException {
        return firebase.getAllAmbulanceData();
    }
    @GetMapping("/GetAllDrivers")
    public List<Drivers> getAllDrivers() throws ExecutionException, InterruptedException {
        return firebase.getAllDrivers();
    }

    //@CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping("/getAllVehicles")
    public List<VehicleName> getall() throws InterruptedException, ExecutionException {
        return firebase.getAllVehicles();


    }

    @GetMapping("/GetBeaconStatus/{name}")
    public String GetBeaconStatus(@PathVariable("name") String name) throws ExecutionException, InterruptedException {
        System.out.println(name);
        return firebase.getBeaconsStatus(name);
    }

    @PostMapping("/AddBeacon")
    public ResponseEntity<String> addBeacon(@RequestBody Beacon beacon) throws ExecutionException, InterruptedException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "Beacon Added");
//		System.out.println(beacon.lat);
//		System.out.println(beacon.lon);
//		System.out.println(beacon.name);
        String s = firebase.saveBeacon(beacon);
        // System.out.println(s);
        return new ResponseEntity<>(jsonObject.toString()
                ,
                HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public String deleteExample(@RequestHeader String name) {
        return "Deleted User " + name;
    }


    @GetMapping("/range_finder")
    public String range(@RequestHeader String latlon) {

        double lat1 = 13.1334203;
        double lat2 = 0;
        double lon1 = 80.289173;

        String[] val = latlon.split("@@");
        String lat21 = val[0];
        String lon21 = val[1];
        // haversine =new Haversine(lat,lon);

        double dLat = Math.toRadians(Double.parseDouble(lat21) - lat1);
        double dLon = Math.toRadians(Double.parseDouble(lon21) - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return String.valueOf(rad * c);


        //return "Deleted User " + String.valueOf(lat);

    }

   // @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping("/getAllBeacons")
    public List<BeaconPojo> getAllBeacons(@RequestHeader String amb_lat, @RequestHeader String amb_lon, @RequestHeader String name) throws InterruptedException, ExecutionException {
        return firebase.getAllBeacons(amb_lat, amb_lon, name);


    }

//	@CrossOrigin(origins = "http://localhost:4200/")
//	@GetMapping("/getAllDrives")
//	public List<DriversPojo> getAllDrivers(@RequestHeader String amb_lat,@RequestHeader String amb_lon,@RequestHeader String name) throws InterruptedException, ExecutionException
//	{
//		return firebase.getAllDrivers();
//
//
//	}

   // @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping("/getAllBeaconsForServer")
    public List<BeaconPojo> getAllBeaconsForServer() throws InterruptedException, ExecutionException {
        return firebase.getAllBeacons();


    }

}