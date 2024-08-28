package com.nitin.metro.configs;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    // Inject the Stripe API key from application.properties
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    // This method is called after the bean is initialized
    @PostConstruct
    public void init() {
        // Set the Stripe API key globally for the application
        Stripe.apiKey = stripeApiKey;
    }
}
