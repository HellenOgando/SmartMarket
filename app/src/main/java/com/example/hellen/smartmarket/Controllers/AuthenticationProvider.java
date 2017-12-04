package com.example.hellen.smartmarket.Controllers;

import com.amazonaws.auth.AWSAbstractCognitoDeveloperIdentityProvider;
import com.amazonaws.regions.Regions;

public class AuthenticationProvider extends AWSAbstractCognitoDeveloperIdentityProvider {

    private static final String developerProvider = "login.smartmarket";

    public AuthenticationProvider(String accountId, String identityPoolId, Regions region) {
        super(accountId, identityPoolId, region);
    }

    @Override
    public String getProviderName() {
        return null;
    }
}
