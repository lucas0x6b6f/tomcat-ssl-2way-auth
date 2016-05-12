package com.lucasko;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class CustomizedSSLContext {

	SSLContext sslContext = null ;
	String path_keystore = "/tmp/keystores/client.keystore";
	String path_truststore = "tmp/keystores/client.truststore";

	FileInputStream keyStoreStream = null;
	FileInputStream trustStoreStream = null;

	String password_keystore = "changeit";
	String password_truststore = "changeit";
	
	CustomizedSSLContext (){
		try {
			// Create and load the keyStore (clientStore)
			keyStoreStream = new FileInputStream(new File(path_keystore));
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(keyStoreStream, password_keystore.toCharArray());

			// Create KeyManagerFactory for managing keyStore
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, password_keystore.toCharArray());
			KeyManager[] kms = kmf.getKeyManagers();

			// Create and load the trustStore
			// Certificates stored in trustStore to verify identity of Server.
			KeyStore trustStore = KeyStore.getInstance("JKS");
			trustStoreStream = new FileInputStream(new File(path_truststore));
			trustStore.load(trustStoreStream, password_truststore.toCharArray());

			// Create TrustManagerFactory for managing truststore
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore);
			TrustManager[] tms = tmf.getTrustManagers();

			// Setting ssl connect
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kms, tms, new SecureRandom());
			this.sslContext = sslContext ;
			
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}
	
	public SSLContext getSslContext (){
		return this.sslContext ;
	}
	
	
	
}
