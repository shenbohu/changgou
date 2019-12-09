package com.changgou.interceptor;

        import feign.RequestInterceptor;
        import feign.RequestTemplate;
        import org.springframework.stereotype.Component;
        import org.springframework.web.context.request.RequestAttributes;
        import org.springframework.web.context.request.RequestContextHolder;
        import org.springframework.web.context.request.ServletRequestAttributes;

        import javax.servlet.http.HttpServletRequest;
        import java.util.Enumeration;

@Component  //声明ben
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 传递令牌
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if (request != null) {
                // 所有请求头的名称
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    // 获取每一个
                    String headerName = headerNames.nextElement();
                    if ("authorization".equals(headerName)) {
                        String headerValue = request.getHeader(headerName); //Bearer jwt
                        // 传递令牌
                        requestTemplate.header(headerName, headerValue);
                    }
                }
            }
        }
    }
}
