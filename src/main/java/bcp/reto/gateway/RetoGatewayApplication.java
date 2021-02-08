package bcp.reto.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableZuulProxy
@EnableFeignClients
@SpringBootApplication
public class RetoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetoGatewayApplication.class, args);
	}

}
