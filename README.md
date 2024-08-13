# kafka-springboot-data-pipeline
Real-Time Credit Card Transaction Processing System

Step1: Login to docker CLI
 
 Sept2: docker compose -f docker-compose.yml up -d
 
 Step3: Run these below DB scripts 
 
 CREATE TABLE Users (
       id SERIAL PRIMARY KEY,
       name VARCHAR(30) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       email VARCHAR(50) UNIQUE NOT NULL,
       roles VARCHAR(20) NOT NULL
     );
     
    CREATE TABLE authorization_log (
       id SERIAL PRIMARY KEY,
       transaction_id VARCHAR(20),
       status VARCHAR(50) NOT NULL,
       reason TEXT,
       timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
   
     CREATE TABLE transaction (
       id SERIAL PRIMARY KEY,
       transactionId VARCHAR(20) NOT NULL,
       cardNumber VARCHAR(16) NOT NULL,
       amount DECIMAL(19, 2) NOT NULL,
       merchantId VARCHAR(20) NOT NULL,
       timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     );
     
     CREATE TABLE account_details (
        id SERIAL,
        cardNumber VARCHAR(16) PRIMARY KEY,
        creditLimit DECIMAL(19, 2) NOT NULL,
        cvv         INT,
	expirationDate TIMESTAMP
     );
     
     insert into account_details values(1, '4321786534235656', '35000',354, now())

     select * from Users;

     select * from authorization_log;

     select * from transaction;

     select * from account_details; //Master data presents

Step4: Verify URLs

        Tomcat  : http://localhost:8080/welcome     http://localhost:8081/welcome
        Actuator: http://localhost:9090/actuator    http://localhost:9091/actuator

Step5: Rest API Endpoints

       a. User to create
       
		 POST http://localhost:8080/api/v1/user
		  
		  Body: {
			    "name": "sandeep",
			    "password": "password",
			    "email": "sandeep.k@gmail.com",
			    "roles": "ROLE_BUYER"
			}

       b. Token to generate
	
	   POST http://localhost:8080/api/v1/token
	   
       c. To get all users, Only ADMIN role
	
	   GET http://localhost:8080/api/v1/all
	  
       d. Transaction, Both ADMIN & BUYER roles
	
	   POST http://localhost:8080/api/v1/publish
	    
	    Generated Token Number
	    
	    Body:
		    {
		      "transactionId": "TRX675675675679",
		      "cardNumber": "4321786534235656",
		      "amount": 23000.00,
		      "merchantId": "676767676766"
		     }
	    
Step6: Production Frequency: Generate random transactions at a regular interval (e.g., every second). Only ADMIN role

      GET http://localhost:8080/api/v1/publish/bulkmessage    
