package run.antleg.sharp;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class TestBeans {

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        var factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setHttpClient(HttpClientBuilder.create()
                .disableAuthCaching()
                .disableAutomaticRetries()
                .disableCookieManagement()
                .useSystemProperties()
                .build());
        return factory;
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(ClientHttpRequestFactory clientHttpRequestFactor) {
        return new RestTemplateBuilder().requestFactory(() -> clientHttpRequestFactor);
    }
}
