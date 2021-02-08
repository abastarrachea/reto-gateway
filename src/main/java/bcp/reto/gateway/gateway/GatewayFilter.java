package bcp.reto.gateway.gateway;

import bcp.reto.gateway.client.OAuthClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class GatewayFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(GatewayFilter.class);

    private static final String EMPTY_STRING = "";
    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "authorization";

    private final OAuthClient oAuthClient;

    public GatewayFilter(final OAuthClient oAuthClient) { this.oAuthClient=oAuthClient;}

    @Override
    public String filterType() {return "pre";}

    @Override
    public int filterOrder() { return 0;}

    @Override
    public boolean shouldFilter() {
        return true;
    }

    public Object run() throws ZuulException {
        final RequestContext currentContext = RequestContext.getCurrentContext();
        final String authorization = currentContext.getRequest().getHeader(AUTHORIZATION);

        Boolean isValid;
        Integer statusCode;

        if(authorization != null && !!authorization.isEmpty() && authorization.contains(BEARER)) {
            String token = authorization.replaceAll(BEARER, EMPTY_STRING).trim();
            try {
                oAuthClient.checkToken(token);
                isValid = true;
                statusCode = null;
                logger.info("[ValidationTokenFilter] [checkToken] token OK");
            } catch (FeignException e) {
                logger.error("[ValidationTokenFilter] [checkToken] token ERROR", e);
                isValid = false;
                if(e.status() == HttpStatus.BAD_REQUEST.value()) {
                    statusCode = HttpStatus.UNAUTHORIZED.value();
                } else {
                    statusCode = e.status();
                }
            }
        } else {
            logger.warn("[ValidationTokenFilter] not exits token");
            isValid = false;
            statusCode = HttpStatus.UNAUTHORIZED.value();
        }

        if(!isValid) {
            currentContext.setSendZuulResponse(isValid);
            currentContext.setResponseStatusCode(statusCode);
        }
        return null;
    }

}
