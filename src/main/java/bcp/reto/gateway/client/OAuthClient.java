package bcp.reto.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oauthClient", url="${app.security.check-token}")
public interface OAuthClient {

    @PostMapping(value = "/oauth/check_token",
    headers = {
        "Content-Type=application/x-www-form-urlencoded",
        "authorization=Basic ${app.security.authorization}"
    })
    void checkToken(@RequestParam("token") String token);

}
