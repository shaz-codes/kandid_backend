package me.kandid.user.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Model.Product.ProductVariant;
import me.kandid.user.Model.Responses.PayUTransactionDetailsResponse;
import me.kandid.user.Model.Responses.PayUVerifyResponse;
import me.kandid.user.Repository.Customer.CustomerOrdersRepository;
import me.kandid.user.Repository.ProductVariantRepository;
import me.kandid.user.Utils.Utils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayUServiceImpl implements PayUService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PayUServiceImpl.class);

    @Value("${payu.merchantkey}")
    private String merchantKey;

    @Value("${payu.salt}")
    private String salt;

    @Value("${payu.verify.url}")
    private String verifyUrl;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    ProductVariantRepository productVariantRepository;

    @Override
    public URL success(String s) throws IOException {
        Map<String, String> params = Utils.decodeForm(s);


        String txnid = params.get("txnid");
        Map<String, String> req = new HashMap<>();
        req.put("key", merchantKey);
        req.put("var1", txnid);
        req.put("command", "verify_payment");
        req.put("hash", Utils.generateHashVerifyAPI(req, salt));


        String apiEndpoint = verifyUrl;
        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build();
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(Utils.encodeParams(req), JSON);
        Request request = new Request.Builder().url(apiEndpoint).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String bodyStr = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                PayUVerifyResponse res = mapper.readValue(bodyStr, PayUVerifyResponse.class);
                if (res.getStatus() == 0) {
                    LOGGER.error(res.getMsg());
                }
                PayUTransactionDetailsResponse deets = res.getTransactionDetails().values().stream().toList()
                                                          .getFirst();
                if (deets.getStatus().equals("success")) {
                    CustomerOrder order = customerOrdersRepository.getCustomerOrderById(
                            Long.parseLong(deets.getTxnid().replace("ORD", "")));
                    order.setPaymentStatus(deets.getStatus());
                    order.setPaymentLink(null);
                    order.setPaymentMethod(deets.getPaymentSource());
                    order.setBankRefNumber(deets.getBankRefNum());
                    if (order.getStatus().equals("PENDING")) {
                        order.setStatus("PLACED");
                    }
                    customerOrdersRepository.save(order);
                    return URI.create(frontendUrl + deets
                            .getTxnid() +
                            "?status=success").toURL();
                } else {
                    CustomerOrder order = customerOrdersRepository.getCustomerOrderById(
                            Long.parseLong(deets.getTxnid().replace("ORD", "")));
                    order.setPaymentStatus(deets.getStatus());
                    order.setPaymentLink(null);
                    order.getItems().forEach(item -> {
                        ProductVariant v = productVariantRepository.findBySku(item.getSku());
                        v.setAvailableStock(v.getAvailableStock() + item.getQuantity());
                        productVariantRepository.save(v);
                    });
                    order.setStatus("FAILED");
                    customerOrdersRepository.save(order);
                    return URI.create(frontendUrl + deets
                            .getTxnid() +
                            "?status=failure").toURL();
                }
            } else {
                assert response.body() != null;
                throw new RuntimeException(response.body().string());
            }

        }

    }

    @Override
    public URL failure(String s) throws IOException {
//        TODO: add items back to the inventory
        Map<String, String> params = Utils.decodeForm(s);


        String txnid = params.get("txnid");
        Map<String, String> req = new HashMap<>();
        req.put("key", merchantKey);
        req.put("var1", txnid);
        req.put("command", "verify_payment");
        req.put("hash", Utils.generateHashVerifyAPI(req, salt));


        String apiEndpoint = verifyUrl;
        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build();
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(Utils.encodeParams(req), JSON);
        Request request = new Request.Builder().url(apiEndpoint).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                ObjectMapper mapper = new ObjectMapper();
                PayUVerifyResponse res = mapper.readValue(response.body().string(), PayUVerifyResponse.class);
                if (res.getStatus() == 0) {
                    LOGGER.error(res.getMsg());
                }
                PayUTransactionDetailsResponse deets = res.getTransactionDetails().values().stream().toList()
                                                          .getFirst();
                CustomerOrder order = customerOrdersRepository.getCustomerOrderById(
                        Long.parseLong(deets.getTxnid().replace("ORD", "")));
                order.setPaymentStatus(deets.getStatus());
                order.setPaymentLink(null);
                order.setStatus("FAILED");
                order.getItems().forEach(item -> {
                    ProductVariant v = productVariantRepository.findBySku(item.getSku());
                    v.setAvailableStock(v.getAvailableStock() + item.getQuantity());
                    productVariantRepository.save(v);
                });
                customerOrdersRepository.save(order);
                return URI.create(frontendUrl + deets
                        .getTxnid() +
                        "?status=failure").toURL();

            } else {
                assert response.body() != null;
                throw new RuntimeException(response.body().string());
            }

        }
    }


}
