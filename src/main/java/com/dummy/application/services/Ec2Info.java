package com.dummy.application.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Ec2Info
{

    private final static String BASE_URL = "http://169.254.169.254/latest";
    private final static String API_TOKEN = BASE_URL + "/api/token";
    private final static String META_DATA_URL = BASE_URL + "/meta-data";
    private final static String IP_ADDRESS_URL = META_DATA_URL + "/public-ipv4";
    private final static String AZ_URL = META_DATA_URL + "/placement/availability-zone";


    /*
        Get the public IP address of the EC2 instance
        Returns a map with the key "public_ip" and the value as the public IP address
        Throws an IOException if there is an error getting the IP address

     */
    public static Map<String, String> getPublicInfo() throws IOException, InterruptedException {
        Map<String, String> ec2Info = new HashMap<>();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_TOKEN))
                .header("X-aws-ec2-metadata-token-ttl-seconds", "21600")
                .method("PUT", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> tokenResponse = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        String token = tokenResponse.body();

        HttpRequest publicIPRequest = HttpRequest.newBuilder()
                .uri(URI.create(IP_ADDRESS_URL))
                .header("X-aws-ec2-metadata-token", token)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> ipResponse = httpClient.send(publicIPRequest, HttpResponse.BodyHandlers.ofString());
        String ip = ipResponse.body();

        HttpRequest availabilityZoneRequest = HttpRequest.newBuilder()
                .uri(URI.create(AZ_URL))
                .header("X-aws-ec2-metadata-token", token)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> azResponse = httpClient.send(availabilityZoneRequest, HttpResponse.BodyHandlers.ofString());
        String az = azResponse.body();

        ec2Info.put("ip", ip);
        ec2Info.put("az", az);

        return ec2Info;
    }

}
