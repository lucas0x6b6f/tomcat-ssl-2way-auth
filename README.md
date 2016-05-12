Steps of Creating KeyStore & TrustStore
----------------------------
Step 0 :
	mkdir /tmp/keystores/

	cd /tmp/keystores/

Step 1: Create Server KeyStore

	sudo $JAVA_HOME/bin/keytool -genkey -v -alias tomcat -keyalg RSA -validity 10000 -keystore ./tomcat.keystore -dname "CN=localhost,OU=lucas,O=lucas,L=Taipei,ST=Taiwan,c=TW" -storepass changeit -keypass changeit

Step 2: Create Client KeyStore

	sudo $JAVA_HOME/bin/keytool -genkey -v -alias client -keyalg RSA -validity 10000 -keystore ./client.keystore -dname "CN=localhost,OU=lucas,O=lucas,L=Taipei,ST=Taiwan,c=TW" -storepass changeit -keypass changeit

Step 3: Export Client cer File From Client KeyStore 

	sudo $JAVA_HOME/bin/keytool -export -alias client -keystore ./client.keystore  -storepass changeit -rfc -file ./client.cer

Step 4: Import Client cer File To Server KeyStore 

	sudo $JAVA_HOME/bin/keytool -import -file ./client.cer -keystore ./tomcat.keystore -storepass changeit

Step 5: Export Server cer File From Server KeyStore 

	sudo $JAVA_HOME/bin/keytool -export -alias tomcat -keystore ./tomcat.keystore -storepass changeit -rfc -file ./tomcat.cer

Step 6: Import Server cer File To Client TrustStore 

	sudo $JAVA_HOME/bin/keytool -import -alias tomcat -file ./tomcat.cer -keystore ./client.truststore -storepass changeit




The Setting of the Tomcat's server.xml
----------------------------
``` XML
	<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol"
    		   maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
               clientAuth="true" sslProtocol="TLS" 
               keystoreFile="/tmp/keystores/tomcat.keystore" keystorePass="changeit"
               truststoreFile="/tmp/keystores/tomcat.keystore" truststorePass="changeit"
                />
    
```


CustomizedSSLContext.java: init KeyStore
----------------------------
``` JAVA
	
	String path_keystore = "/tmp/keystores/client.keystore";
	String path_truststore = "tmp/keystores/client.truststore";

	FileInputStream keyStoreStream = null;
	String password_keystore = "changeit";
	
	keyStoreStream = new FileInputStream(new File(path_keystore));
	KeyStore keyStore = KeyStore.getInstance("JKS");
	keyStore.load(keyStoreStream, password_keystore.toCharArray());
	
	KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	kmf.init(keyStore, password_keystore.toCharArray());
	KeyManager[] kms = kmf.getKeyManagers();
    
```


CustomizedSSLContext.java: init TrustStore
----------------------------
``` JAVA
	
	KeyStore trustStore = KeyStore.getInstance("JKS");
	trustStoreStream = new FileInputStream(new File(path_truststore));
	trustStore.load(trustStoreStream, password_truststore.toCharArray());

	TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	tmf.init(trustStore);
	TrustManager[] tms = tmf.getTrustManagers();
	
```

To Run Main.java
----------------------------
``` JAVA

	// Init SSL
	SSLContext sslContext = new CustomizedSSLContext().getSslContext() ;
	HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

	// Construct the connection.
	URL url = new URL("https://localhost:8443");
	HttpsURLConnection httpConnection = (HttpsURLConnection) url.openConnection();
	
```
