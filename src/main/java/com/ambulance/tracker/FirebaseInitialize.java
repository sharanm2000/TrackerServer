package com.ambulance.tracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseInitialize {

	@PostConstruct
	public void initilaize()
	{
		
		try
		
		{
File fff = new File("../key/ambulancetracker-1c2e4.json");
			
			FileInputStream serviceAccount =
					  new FileInputStream(fff.getAbsoluteFile());

					FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .setDatabaseUrl("https://ambulancetracker-1c2e4.firebaseio.com")
					  .build();

					FirebaseApp.initializeApp(options);
					System.out.println("Found Key");
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			
			
		}
		
		
	}
	
}
