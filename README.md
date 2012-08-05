Connection Factory
===
A small project dedicated to providing a way to secure database credentials in envioronments that
would otherwise be stored in plain text.  To name a few, this would include Tomcat & Jboss, and Jetty.

Credits
---
Thanks to Murali, MICHEAL REMIJAN, the folks at The Apache Software Foundation, and the folks at The Legion of the Bouncy Castle for inspiring this small project.
* http://scribblejava.wordpress.com/2010/03/23/encrypt-username-and-password-for-jndi-in-tomcat-server-xml/trackback/
* http://java.sys-con.com/node/393364
* http://tomcat.apache.org/tomcat-5.5-doc/jndi-resources-howto.html#Adding%20Custom%20Resource%20Factories
* http://www.bouncycastle.org/java.html

Goals
---
In an effort to provide a working example, this project is a cumulation of previous works, with the following additions:
* added some dependency management (maven), unit tests, and a touch of personal polish
* provide a working example with other Providers and Ciphers
* use BouncyCastle for Base64 encoding/decoding
* provide an example of generating an encrypted string from the command line
* share it on github.com!

Build and Run:
---

	# example to build and provide the encrypted version of 'hello world'
	# - assumes that required jars are in the ./lib/* directory (and is marked as provided in the pom), and Maven & Java 1.6+ in the current $PATH
	# ls lib/
	# bcprov-jdk16-1.46.jar	commons-dbcp-1.2.2.jar	commons-pool-1.3.jar
	
	mvn clean package && \
 	 java -classpath "lib/*:target/*" com.voltsolutions.security.Encryptor "hello world"


Tomcat Setup:
===
Add the jars for Tomcat into Tomcat's lib directory:
---

	bcprov-jdk16-1.46.jar
	commons-dbcp-1.2.2.jar
	commons-pool-1.3.jar
	connection-factory-0.0.1-SNAPSHOT.jar

Update Tomcat's server.xml (or you may use your META-INF/context.xml): 
---

	<Context ...>
	 ... 
	    <Resource
     		name="jdbc/EmployeeDB" auth="Container"
	        type="javax.sql.DataSource"
			factory="com.voltsolutions.EncryptedDataSourceFactory"
	        username="Ql6u3CAl988=" password="CgrtRRVz6A3aaGAfDpRRuQ=="
	        driverClassName="oracle.jdbc.driver.OracleDriver" url="jdbc:oracle:thin:@server:1523:TEST"
	        maxActive="15" minIdle="2"
			logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"
	    />
	 ... 
	</Context>

Further Changes:
---
* make it your own, fork it or branch it, and change things (salt, algos, ciphers, pass phrase, iterations)
