package com.infy.EcommerceUserManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

@SpringBootApplication
@ComponentScan({"com.infy.EcommerceUserManagement"})
public class EcommerceUserManagementApplication {
 public static Properties PROP;
    public static void main(String[] args) {
        PROP = readPropertiesFromS3();
        if (PROP == null) {
            SpringApplication.run(EcommerceUserManagementApplication.class, args);
        } else {
            SpringApplication app = new SpringApplication(EcommerceUserManagementApplication.class);
            app.setAdditionalProfiles("aws");
            app.run(args);
        }
        
      
    }
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


	
	   public static Properties readPropertiesFromS3() {

        String key_name = "application.properties";
        String bucket_name = "propertiesbucket";
        Properties prop = new Properties();

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-2").build();
        S3Object object = s3.getObject(new GetObjectRequest(bucket_name, key_name));
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arrOfStr = line.split("=");
                prop.put(arrOfStr[0].trim(), arrOfStr[1].trim());
            }

            object.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;

    }
}
