package com.jjubull.authserver.config.interceptor;

import com.jjubull.authserver.util.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    private static final String KEY_PREFIX = "rl:cp:ip:";
    private static final int LIMIT = 300;
    private static final Duration TTL = Duration.ofSeconds(60);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = clientIp(request);
        String key = KEY_PREFIX + ip;

        if (!rateLimiter.allow(key, LIMIT, TTL)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=utf-8");
            response.setHeader("Retry-After", String.valueOf(TTL.getSeconds()));
            response.getWriter().write("Too Many Requests");
            return false;
        }

        return true;
    }

    public String clientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        String header = request.getHeader("X-Forwarded-For");
        String remoteAddr = request.getRemoteAddr();
        log.info("host={}, scheme={}, port={}",
                request.getHeader("Host"),
                request.getScheme(),
                request.getServerPort()
        );
        log.info("real:{}, xff:{}, remote:{}", ip, header, remoteAddr);
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
