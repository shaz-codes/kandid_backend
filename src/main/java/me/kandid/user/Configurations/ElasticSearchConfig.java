package me.kandid.user.Configurations;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${es.url}")
    String url;

    @Value("${es.SslCA}")
    String ca;

    @Value("${es.username}")
    String user;

    @Value("${es.password}")
    String password;

    @NotNull
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().connectedTo(url).usingSsl(ca)
                                  .withBasicAuth(user, password).build();
    }
}
