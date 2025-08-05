package me.kandid.user.Configurations;

import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Configuration
public class AWSOpenSearchClient {

    @Value("${os.username}")
    private String user;

    @Value("${os.password}")
    private String pass;

    @Value("${os.hostname}")
    private String hostname;

    @Value("${os.port}")
    private int port;

    @Value("${os.https}")
    private boolean https;

    @Bean
    public OpenSearchClient openSearchClient() {
        String auth = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + auth;

        final var hosts = new HttpHost[]{new HttpHost(https ? "https" : "http", hostname, port)};
        final var transport = ApacheHttpClient5TransportBuilder
                .builder(hosts)
                .setMapper(new JacksonJsonpMapper())
                .setHttpClientConfigCallback(h -> h.setConnectionManagerShared(true)
                                                   .setDefaultHeaders(List.of(new BasicHeader(HttpHeaders.AUTHORIZATION,
                                                           authHeader))))
                .build();

        return new OpenSearchClient(transport);
    }
}