package com.example.hellen.smartmarket.Controllers;

import android.content.Context;

import com.amazonaws.auth.AWSAbstractCognitoIdentityProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;


public class ManagerClass {

    protected static CognitoCachingCredentialsProvider credentialsProvider = null;
    private static CognitoSyncManager syncClient;
    AmazonS3Client amazonS3Client;
    TransferUtility transferUtility;
    protected static AWSAbstractCognitoIdentityProvider developerIdentityProvider;

    public static AmazonDynamoDBClient dynamoDBClient = null;
    public static DynamoDBMapper dynamoDBMapper = null;

    public CognitoCachingCredentialsProvider getCredentials(Context context){

        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-west-2:e2cdcb7c-8a9a-4d61-a3cb-2c62eec53bac", // Identity pool ID
                Regions.US_WEST_2 // Region
        );

         syncClient = new CognitoSyncManager(
                context,
                Regions.US_WEST_2, // Region
                credentialsProvider);

        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback());

        return credentialsProvider;
    }

    public AmazonS3Client inS3Client (Context context){
        if(credentialsProvider == null){
            getCredentials(context);
            amazonS3Client = new AmazonS3Client(credentialsProvider);
            amazonS3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        }
        return amazonS3Client;
    }

    public TransferUtility checkTransfer(AmazonS3Client a3Client, Context context){

        if(transferUtility == null){
            transferUtility = new TransferUtility(a3Client, context);
        }
        return  transferUtility;
    }

    public DynamoDBMapper initDynamoClient(CognitoCachingCredentialsProvider credentialsProvider){
        if(dynamoDBClient == null){
            dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
            dynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
            dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        }
        return dynamoDBMapper;
    }

    public static void initSync(Context context){


    }
}
