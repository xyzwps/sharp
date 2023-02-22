package run.antleg.sharp

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

import java.util.function.Supplier

@Configuration
class TestBeans {

    @Bean
    ClientHttpRequestFactory clientHttpRequestFactory() {
        def factory = new HttpComponentsClientHttpRequestFactory()

        factory.connectTimeout = 5000
        factory.httpClient = HttpClientBuilder.create()
                .disableAuthCaching()
                .disableAutomaticRetries()
                .disableCookieManagement()
                .useSystemProperties()
                .build()
        return factory
    }

    @Bean
    RestTemplateBuilder restTemplateBuilder(ClientHttpRequestFactory clientHttpRequestFactor) {
        Supplier<ClientHttpRequestFactory> requestFactorySupplier = { clientHttpRequestFactor }
        return new RestTemplateBuilder().requestFactory(requestFactorySupplier)
    }
}
