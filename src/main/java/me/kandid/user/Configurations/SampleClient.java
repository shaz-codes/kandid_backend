package me.kandid.user.Configurations;

import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class SampleClient {
    public static OpenSearchClient create() {
        String user = "admin";
        String pass = "Kandid@0908";

        final var hosts = new HttpHost[]{new HttpHost("https",
                "search-meoww-43xw3lvhigkm6jtw3icmires44.ap-south-1.es.amazonaws.com", 443)};


        String auth = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + auth;
        final var transport = ApacheHttpClient5TransportBuilder
                .builder(hosts)
                .setMapper(new JacksonJsonpMapper()).setHttpClientConfigCallback(
                        h -> h.setConnectionManagerShared(true).setDefaultHeaders(
                                List.of(new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader)

                                ))
                ).build();
        return new OpenSearchClient(transport);
    }
}