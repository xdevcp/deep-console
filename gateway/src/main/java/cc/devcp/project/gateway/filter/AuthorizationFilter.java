package cc.devcp.project.gateway.filter;

import cc.devcp.project.common.enums.CommEnum;
import cc.devcp.project.common.model.dto.AuthInfo;
import cc.devcp.project.common.model.result.ResEntity;
import cc.devcp.project.gateway.config.WhiteUrlProps;
import cc.devcp.project.gateway.utils.JwtHelper;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <pre>
 *     description: 系统鉴权全局过滤
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-06
 */
@Slf4j
@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private WhiteUrlProps whiteUrlProps;

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 鉴权流程
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //获取请求的url地址
        String path = request.getURI().getPath();
        log.info("request-path: {}", path);
        log.info("white urls　：{}", String.join(",", whiteUrlProps.getWhiteUrls()));
        //白名单内容的地址
        if (whiteUrlProps.getWhiteUrls().contains(path)) {
            log.info("white url list contains the path");
            //直接放行
            return chain.filter(exchange);
        }

        for (String whiteUrl : whiteUrlProps.getWhiteUrls()) {
            if (antPathMatcher.match(whiteUrl, path)) {
                log.info("white url list ant match the path");
                //直接放行
                return chain.filter(exchange);
            }
        }
        String token = "";
        AuthInfo authInfo = null;
        try {
            //尝试从header中获取 Authorization, 即 jwt
            List<String> headers = request.getHeaders().get("Authorization");
            if (CollectionUtils.isEmpty(headers)) {
                //如果header中没有 jwt, 则返回没有token，信息给到前端
                return this.writeResponse(response, ResEntity.fail(ResponseEnum.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            token = headers.get(0).replaceFirst("Bearer ", "");

            if (StringUtils.isEmpty(token)) {
                //如果header中没有 jwt, 则返回没有token，信息给到前端
                return this.writeResponse(response, ResEntity.fail(ResponseEnum.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            authInfo = jwtHelper.getAuthentication(token);
        } catch (Exception e) {
            log.info("jwt parse error, jwt :{}", token);
            return this.writeResponse(response, ResEntity.fail(ResponseEnum.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        //以下是鉴权成功处理逻辑
        ServerHttpRequest.Builder requestBuilder = request.mutate();
        ServerHttpRequest mutateRequest = requestBuilder.build();
        return chain.filter(exchange.mutate().request(mutateRequest).build());
    }

    //todo 修改优先级
    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, ResEntity resEntity, HttpStatus httpStatus) {
        byte[] bytes = JSONObject.toJSONString(resEntity).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.setStatusCode(httpStatus);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return response.writeWith(Flux.just(buffer));
    }


    public enum ResponseEnum implements CommEnum<Integer> {
        /**
         * ResponseEnum
         */
        UNAUTHORIZED(40100, "无效的令牌"),
        FORBIDDEN(40300, "禁止访问"),
        AUTH_SERVER_ERROR(40100, "鉴权系统暂不可用"),
        ;

        private Integer code;
        private String content;

        ResponseEnum(Integer code, String content) {
            this.code = code;
            this.content = content;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getContent() {
            return content;
        }
    }
}
