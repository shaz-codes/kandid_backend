package me.kandid.user.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Product.ProductFilter;
import org.apache.commons.codec.digest.DigestUtils;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.Query;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static long decodePhoneFromJWT(String token) {
        DecodedJWT jwt = JWT
//                .require(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")))
.require(Algorithm.HMAC256("i0vriteFm08yJZxXrmuWiY7hsDDZhIcW"))
.withIssuer("Kandid User")
.build().verify(token.replace("Bearer ", ""));

        return Long.parseLong(jwt.getSubject());
    }

    public static String generateHashPaymentsAPI(Map<String, String> params, String salt) {
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

    public static String generateHashVerifyAPI(Map<String, String> params, String salt) {
        StringBuilder hashString = new StringBuilder();
        hashString.append(params.get("key"));
        hashString.append("|");
        hashString.append(params.get("command"));
        hashString.append("|");
        hashString.append(params.get("var1"));
        hashString.append("|");
        hashString.append(salt);

        String hash = hashString.toString();

        return DigestUtils.sha512Hex(hash);
    }

    public static Map<String, String> decodeForm(String s) {
        String newString = URLDecoder.decode(s, StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        String[] pairs = newString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length >= 2) {
                map.put(keyValue[0], keyValue[1]);
            } else map.put(keyValue[0], "");
        }
        return map;
    }

    public static String encodeParams(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            encodedParams.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),
                    StandardCharsets.UTF_8)).append(
                    "&");
        }
        return encodedParams.toString();
    }

    public static Map<String, String> createHashMap(String merchantKey, String amount, String firstName,
                                                    String email, String phone, String productinfo, String surl,
                                                    String furl, String txnId) {
        Map<String, String> params = new HashMap<>();
        params.put("key", merchantKey);
        params.put("txnid", txnId);
        params.put("amount", amount);
        params.put("productinfo", productinfo);
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
        return params;
    }

    public static List<Query> createFilter(ProductFilter filter) {
        List<Query> filters = new ArrayList<>();

        if (filter.getAesthetic() != null) {
            filters.add(createFieldQuery("aesthetic", filter.getAesthetic()));
        }
        if (filter.getMaterial() != null) {
            filters.add(createFieldQuery("material", filter.getMaterial()));
        }
        if (filter.getColor() != null) {
            filters.add(createFieldQuery("color", filter.getColor()));
        }
        if (filter.getOccasion() != null) {
            filters.add(createFieldQuery("occasion", filter.getOccasion()));
        }
        if (filter.getSleeve() != null) {
            filters.add(createFieldQuery("sleeve", filter.getSleeve()));
        }
        if (filter.getFit() != null) {
            filters.add(createFieldQuery("fit", filter.getFit()));
        }
        if (filter.getFitType() != null) {
            filters.add(createFieldQuery("fitType", filter.getFitType()));
        }
        if (filter.getPattern() != null) {
            filters.add(createFieldQuery("pattern", filter.getPattern()));
        }
        if (filter.getNeckline() != null) {
            filters.add(createFieldQuery("neckline", filter.getNeckline()));
        }
        if (filter.getClosure() != null) {
            filters.add(createFieldQuery("closure", filter.getClosure()));
        }
        if (filter.getClosureType() != null) {
            filters.add(createFieldQuery("closureType", filter.getClosureType()));
        }
        if (filter.getRiseStyle() != null) {
            filters.add(createFieldQuery("riseStyle", filter.getRiseStyle()));
        }
        if (filter.getStyle() != null) {
            filters.add(createFieldQuery("style", filter.getStyle()));
        }
        if (filter.getTrend() != null) {
            filters.add(createFieldQuery("trend", filter.getTrend()));
        }
        if (filter.getCategory() != null) {
            filters.add(createFieldQuery("category", filter.getCategory()));
        }
        if (filter.getSubCategory() != null) {
            filters.add(createFieldQuery("subCategory", filter.getSubCategory()));
        }
        if (filter.getBrand() != null) {
            filters.add(createFieldQuery("brand", filter.getBrand()));
        }
        if (filter.getGender() != null) {
            filters.add(createFieldQuery("gender", filter.getGender()));
        }

        if (filter.getPriceTo() != null) {
            filters.add(Query.of(
                    q -> q.range(r -> r.field("sellingPrice").lte(JsonData.of(filter.getPriceTo())))
            ));
        }
        if (filter.getPriceFrom() != null) {
            filters.add(Query.of(
                    q -> q.range(r -> r.field("sellingPrice").gte(JsonData.of(filter.getPriceFrom())))
            ));
        }
        return filters;
    }


    private static Query createFieldQuery(String field, List<String> list) {
        return Query.of(
                q -> q
                        .terms(
                                t -> t.field(field)
                                      .terms(
                                              tt -> tt
                                                      .value(list.stream().map(FieldValue::of).toList()
                                                      )
                                      )
                        )
        );
    }


}
