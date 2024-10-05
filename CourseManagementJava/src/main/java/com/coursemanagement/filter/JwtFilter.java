//package com.coursemanagement.filter;
//
//import com.coursemanagement.util.JwtUtil;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class JwtFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String authHeader = httpRequest.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String username = JwtUtil.validateToken(token);
//
//            if (username != null) {
//                // 用户验证成功，可以在这里执行用户的身份验证逻辑
//                // 例如，设置用户身份信息到 request 或 session
//                request.setAttribute("username", username);
//            } else {
//                // JWT 无效
//                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//                return;
//            }
//        } else {
//            // 没有提供 JWT
//            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
//            return;
//        }
//
//        // 如果认证成功，继续处理请求
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void destroy() {
//    }
//}
