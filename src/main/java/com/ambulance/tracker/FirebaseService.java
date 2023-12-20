package com.ambulance.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.*;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseService {

    public String saveUserDetails(Vehicles vehicles) throws InterruptedException, ExecutionException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("vehicles").document(vehicles.getName()).set(vehicles);
        return collectionsApiFuture.get().getUpdateTime().toString();

    }

    public String saveAmbulance(Ambulance ambulance) throws InterruptedException, ExecutionException {
        System.out.println("saveAmbulance");
        System.out.println(ambulance.getAmbulance_model());
        System.out.println(ambulance.getAmbulance_name());
        System.out.println(ambulance.getCity());
        System.out.println(ambulance.getRegistration_number());
        System.out.println(ambulance.getRegistration_number());
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("ambulance").document(ambulance.getRegistration_number()).set(ambulance);

        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("message", "Ambulance Added");
        return jsonObject.toString();
        //return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Vehicles getUserDetails(String name) throws InterruptedException, ExecutionException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("vehicles").document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot documentSnapshot = future.get();
        Vehicles vehicles = null;

        if (documentSnapshot.exists()) {

            vehicles = documentSnapshot.toObject(Vehicles.class);
            return vehicles;

        } else {
            return vehicles;

        }

    }

    public List<Vehicles> getVehiclesupdate() throws InterruptedException, ExecutionException {
        List<Vehicles> vehiclesList = new ArrayList<Vehicles>();
        Vehicles vehicles = new Vehicles();
        Firestore dbFirestore = FirestoreClient.getFirestore();


        ApiFuture<QuerySnapshot> query = dbFirestore.collection("vehicles").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            vehicles = document.toObject(Vehicles.class);
            vehiclesList.add(vehicles);
        }
        System.out.println(vehiclesList.size());
        return vehiclesList;
    }

    public List<VehicleName> getAllVehicles() throws InterruptedException, ExecutionException {
        List<VehicleName> vehicleNamesList = new ArrayList<VehicleName>();
        VehicleName vehicleName;
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = dbFirestore.collection("vehicles").get();

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            vehicleName = new VehicleName();
            System.out.println(document.get("name"));
            vehicleName.setName(document.get("name").toString());
            vehicleNamesList.add(vehicleName);

        }


        System.out.println("");
        return vehicleNamesList;
    }

    public List<BeaconPojo> getAllBeacons(String amb_lat, String amb_lon, String name) throws InterruptedException, ExecutionException {
        List<BeaconPojo> vehicleNamesList = new ArrayList<BeaconPojo>();
        BeaconPojo beaconPojo;

        Firestore dbFirestore = FirestoreClient.getFirestore();


        ApiFuture<QuerySnapshot> query = dbFirestore.collection("beacon").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            beaconPojo = new BeaconPojo();
            beaconPojo.setName((document.get("name").toString()));
            beaconPojo.setLatitude((document.get("lat").toString()));
            beaconPojo.setLongitude((document.get("lon").toString()));


            double latt = Double.parseDouble(document.get("lat").toString());
            double laonn = Double.parseDouble(document.get("lon").toString());

            double amb_lat_dou = Double.parseDouble(amb_lat);
            double amb_lon_dou = Double.parseDouble(amb_lon);


            double result = distance(latt, laonn, amb_lat_dou, amb_lon_dou, "K");

            if (result <= 1) {

                System.out.println("Ambulance " + name + "was detected inside circle" + (document.get("name").toString()));
                updateBeaconStatus((document.get("name").toString()),true);
                System.out.println("DISTANCE :" + result);
            } else {
                System.out.println("Not in range");
                System.out.println("DISTANCE :" + result);
                updateBeaconStatus((document.get("name").toString()),false);
            }
            beaconPojo.setResult(String.valueOf(result));

            vehicleNamesList.add(beaconPojo);

        }


        System.out.println("");
        return vehicleNamesList;
    }

    private void updateBeaconStatus(String name,boolean status) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docref = db.collection("beacon").document(name);
        ApiFuture<WriteResult> future = docref.update("status", status);
        WriteResult result = future.get();
        System.out.println("Write result: " + result);

    }


    public List<BeaconPojo> getAllBeacons() throws InterruptedException, ExecutionException {
        List<BeaconPojo> vehicleNamesList = new ArrayList<BeaconPojo>();
        BeaconPojo beaconPojo;

        Firestore dbFirestore = FirestoreClient.getFirestore();


        ApiFuture<QuerySnapshot> query = dbFirestore.collection("beacon").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            beaconPojo = new BeaconPojo();
            beaconPojo.setName((document.get("name").toString()));
            beaconPojo.setLatitude((document.get("lat").toString()));
            beaconPojo.setLongitude((document.get("lon").toString()));

            vehicleNamesList.add(beaconPojo);

        }


        System.out.println("");
        return vehicleNamesList;
    }


    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    public List<Ambulance> getAllAmbulanceData() throws ExecutionException, InterruptedException {
        List<Ambulance> ambulances = new ArrayList<>();
        Ambulance ambulance;
        Firestore dbFirestore = FirestoreClient.getFirestore();


        ApiFuture<QuerySnapshot> query = dbFirestore.collection("ambulance").get();
        QuerySnapshot snapshots = query.get();
        for (QueryDocumentSnapshot documentSnapshot : snapshots) {
            ambulance = new Ambulance();
            ambulance.setAmbulance_name(documentSnapshot.get("ambulance_name").toString());
            ambulance.setCity(documentSnapshot.get("city").toString());
            ambulance.setMisc(documentSnapshot.get("misc").toString());
            ambulance.setAmbulance_model(documentSnapshot.get("ambulance_model").toString());
            ambulance.setRegistration_number(documentSnapshot.get("registration_number").toString());
            System.out.println(documentSnapshot.get("ambulance_name").toString());
            ambulances.add(ambulance);
        }
        System.out.println(ambulances.size());
        System.out.println(snapshots.size());
        return ambulances;
    }

    public String saveBeacon(Beacon beacon) throws ExecutionException, InterruptedException {


        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = db.collection("beacon").document(beacon.getName()).set(beacon);
        //ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("ambulance").document(ambulance.getRegistration_number()).set(ambulance);
        return apiFuture.get().getUpdateTime().toString();

    }

    public String setAddDriver(Drivers driver) throws ExecutionException, InterruptedException {

        System.out.println(driver.getDriver_name());
        System.out.println(driver.getMisc());
        System.out.println(driver.getPassword());
        System.out.println(driver.getMisc());
        System.out.println(driver.getAmbulance_model());
        System.out.println(driver.getMobile());

        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = db.collection("driver").document(driver.getDriver_name()).set(driver);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "Driver Added Successful");
        String s = apiFuture.get().getUpdateTime().toString();
        System.out.println(s);
        return jsonObject.toString();
    }

    public List<Drivers> getAllDrivers() throws ExecutionException, InterruptedException {

        List<Drivers> drivers_list = new ArrayList<>();
        Drivers driver;
        Firestore dbFirestore = FirestoreClient.getFirestore();


        ApiFuture<QuerySnapshot> query = dbFirestore.collection("driver").get();
        QuerySnapshot snapshots = query.get();
        for (QueryDocumentSnapshot documentSnapshot : snapshots) {
            driver = new Drivers();

            driver.setDriver_name(documentSnapshot.get("driver_name").toString());
            driver.setAmbulance_model(documentSnapshot.get("ambulance_model").toString());
            driver.setMisc(documentSnapshot.get("misc").toString());
            driver.setReg_num(documentSnapshot.get("reg_num").toString());
            driver.setPassword(documentSnapshot.get("password").toString());
            driver.setMobile(documentSnapshot.get("mobile").toString());
//            driver.setMisc(documentSnapshot.get("misc").toString());
//            driver.setAmbulance_model(documentSnapshot.get("ambulance_model").toString());
//            driver.setRegistration_number(documentSnapshot.get("registration_number").toString());
//            System.out.println(documentSnapshot.get("ambulance_name").toString());

            System.out.println(driver.getDriver_name());
            System.out.println(driver.getPassword());
            System.out.println(driver.getReg_num());
            System.out.println(driver.getMisc());
            System.out.println(driver.getAmbulance_model());
            System.out.println(driver.getPassword());
            drivers_list.add(driver);
        }
        System.out.println(drivers_list.size());
        System.out.println(snapshots.size());
        return drivers_list;
    }

    public String getBeaconsStatus(String name) throws ExecutionException, InterruptedException
    {

        BeaconPojo beaconPojo;

        Firestore dbFirestore = FirestoreClient.getFirestore();
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference apiFuture = db.collection("beacon").document(name);

      // ApiFuture<QuerySnapshot> query = dbFirestore.collection("beacon").document(name).;
        // ...
        // query.get() blocks on response
        ApiFuture<DocumentSnapshot> querySnapshot = apiFuture.get();
        Boolean status = querySnapshot.get().getBoolean("status");
        System.out.println(status.toString());

        return status.toString();
        }


}
