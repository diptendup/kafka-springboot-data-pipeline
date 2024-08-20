# kafka-springboot-data-pipeline
Real-Time Credit Card Transaction Processing System

Prerequisite

1. DBeaver / PgAdmin - Postgress
2. Offset Explorer   - Kafka Messaging UI
3. Postman           - API Testing

Step1: Login to docker CLI
 
 Sept2: docker compose -f docker-compose.yml up -d
 
 Step3: Run these below DB scripts 

 Ping or mailid me dipendu.parida@gmail.com, will prodive all these DB scripts

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
