package com.lucasko;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Main {

	public static void main(String[] args) {

		try {
			// Init SSL
			SSLContext sslContext = new CustomizedSSLContext().getSslContext() ;
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			// Construct the connection.
			URL url = new URL("https://localhost:8443");
			HttpsURLConnection httpConnection = (HttpsURLConnection) url.openConnection();

			System.out.println("========= URL Content ========");
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

			String line;
			while ((line = bufferedreader.readLine()) != null) {
				System.out.println(line);
			}
			bufferedreader.close();

		} catch (Exception e) {
			System.out.print(e.getMessage());
		}

	}

}
