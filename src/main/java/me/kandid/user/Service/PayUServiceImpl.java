package me.kandid.user.Service;

import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Repository.Customer.CustomerOrdersRepository;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayUServiceImpl implements PayUService {
    @Autowired
    CustomerOrdersRepository customerOrdersRepository;

    @Override
    public String checkOut(CustomerOrder order, String surl, String furl) throws Exception {
        customerOrdersRepository.save(order);

        String apiEndpoint = "https://test.payu.in/_payment";

        String merchantKey = "HAHPlS";
        String salt = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfDPbILXkt0CgwI95pCW258yD0bAtM0wJLXWl7wsoFOhuDh6Mp2a4KqH/sZbx1/T/21AhnebyWCcjMDcTdaGmZBBzCPq+sD12PP3NatnnSW7uxY3JY1SYAPdyfKEY37x3xTeHxmy6DcvrvL8WHPqOBHmvWMd7lR755s3zpa4xmllrbHi9YqSEdl4hxb9ZcpCRfjjYjhm68B7iRXfUr2/8En6QvtdKBdaScPxuZdPZwdSI8jgzsjnjbVZOPOIr4eddv9g6dZ/8sw7JcdDr71VjfKozG77nZfoUZ7kQAc1koODhwOBFtokCb9pAKbFK8IGx29lOlqnSm5Nx/kDFRD571AgMBAAECggEAERcRhVztgnC1gMa55UtwEOUBC4J4jF64BmUZKoU5s+oUfsOaUqrjOp30cPBVmdAGiX0rEgNQskaG3vYzyx6nChwZAv49Xh2gjf3hZUPqPyJAPsVSyhzNIoWDKU7ojKYS75TQzGdCTjmSoRTfArNGWl9scu2U58oXu3f/2g0GXxdpTctvJMn/p/LQIMk6nUSQVwYbnkwMPuuUWAsojI3HrCc2av7a+ThMhTvk2YTWOxIV14/1JhAd5COo6nJSvo73wltPFNZ7ZjJbhsDCuyne/2cdDvhaIiO6O0mRPhnOA3uTyt+4EfCHtNC1QnwnS3fyI+lmW8AabPeGt0PW1BO4WQKBgQDcu6DOmOjX5KSoOcOvCyk1iC/+BLXGe/JNCRIpCOUTNsmpRiuSQFDDbzPPSI3RrGDlRW5HJ9SuYduoQKeGBJBWRjRw4YDrfPNN0l5MJOR7NFqeeWTgb1FIOFhmcdd7T/mqO8OuMM6IhJtiXH/vFBjK1GzFeEu9TKZelR25bmphewKBgQC4dmuH22kq1UA7n8/NN3sqVI10hlyGaBQjWTNGE3dQEaOS3I1OSLtkSHCAr6apdXjCGpy6t1/awGGrZNelmjPfMoPWAOrGvQ40E5A+h2DBaSdz70NpSITDWf6waxcHBcYIxZhFOOEJi+PKnA8UDm2nKnFVkdVwkfBqnqqqyHN+TwKBgFF5N+vJ6Pf3PweQ+rebiQRnVj+OgYHXsiHZHUjkLZmf+WNvsRl0f9sDKr5x61bfJ4y4yfGdtBUBrdA2vR7shFIz65tWwP0qy6uH4KJ+Y49OTbx81k05CWl80rRNhNnRNTgugxXCkhk1yRzPQ7F9W4FSUyu4MUyYBDiZFbWiGcYNAoGBAKm2wl5/v3XaODvjYGQ2XIpourDwAebjkUuogSVDOa4gXHf9lcDl6AtvHps3gMY+v3kO1Q5xMq7n+7Selk3V303Gg0d9FoT9YjkFmtv84EdywQrIoyQVxZiaj1qvXMNoS3i8m8lseJe47j72cGUKlVaNFTMedtsYWrMuAmYZATG/AoGAD1Orp9R1C9zsPlidiG3j2Oj8wF5RQoJ4e9WAq7R25J6vrQJgUDRKqLqZDmslGDVgr/hirDMCDu8zovZh7iPEMbmfjbYSd2w9Yms1mqFSBk9tLDURMMbRQEtLXmETGosRrWjSqqsp3qFyPUr1EdE6/ogTvhSqg4S7emM2C5F0Zko=";

        String amount = "100.00";
        String productInfo = "Test Product";
        String firstName = "John";
        String email = "samriddhorsam@gmail.com";
        String phone = "9161086557";
        String txnId = "TXN" + Instant.now().toEpochMilli();
        surl = surl.trim().isBlank() ? "https://www.kandid.me" : surl;
        furl = furl.trim().isEmpty() ? "https://samriddh.vercel.app" : furl;

        Map<String, String> params = new HashMap<>();
        params.put("key", merchantKey);
        params.put("txnid", txnId);
        params.put("amount", amount);
        params.put("productinfo", productInfo);
        params.put("firstname", firstName);
        params.put("email", email);
        params.put("phone", phone);
        params.put("surl", surl);
        params.put("furl", furl);
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");

        String hash = generateHash(params, salt);

        params.put("hash", hash);

        String encodedParams = encodeParams(params);

        URL url = new URI(apiEndpoint).toURL();

        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build();
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(JSON, encodedParams);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            throw new RuntimeException(response.body().string());
        }
        return response.header("Location");
    }

    private static String generateHash(Map<String, String> params, String salt) throws Exception {
        StringBuilder hashString = new StringBuilder();
        hashString.append(params.get("key"));
        hashString.append("|");
        hashString.append(params.get("txnid"));
        hashString.append("|");
        hashString.append(params.get("amount"));
        hashString.append("|");
        hashString.append(params.get("productinfo"));
        hashString.append("|");
        hashString.append(params.get("firstname"));
        hashString.append("|");
        hashString.append(params.get("email"));
        hashString.append("|");
        hashString.append(params.get("udf1"));
        hashString.append("|");
        hashString.append(params.get("udf2"));
        hashString.append("|");
        hashString.append(params.get("udf3"));
        hashString.append("|");
        hashString.append(params.get("udf4"));
        hashString.append("|");
        hashString.append(params.get("udf5"));
        hashString.append("||||||");
        hashString.append(salt);

        String hash = hashString.toString();

        return DigestUtils.sha512Hex(hash);
    }

    private static String encodeParams(Map<String, String> params) throws Exception {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            encodedParams.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),
                    StandardCharsets.UTF_8)).append(
                    "&");
        }
        return encodedParams.toString();
    }
}
