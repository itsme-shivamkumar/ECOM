package in.zeta.ecom.services;

import com.google.gson.Gson;
import in.zeta.ecom.entity.ApiError;
import in.zeta.ecom.entity.User;
import in.zeta.ecom.exceptions.NotAuthorizedException;
import in.zeta.ecom.exceptions.UserNotFoundException;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class AuthFilter extends OncePerRequestFilter {


    private JwtUtils jwtUtils;
    @Autowired
    public AuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("signup") || request.getRequestURL().toString().contains("login") || request.getRequestURL().toString().contains("swagger") || request.getRequestURL().toString().contains("v2");
    }

    @Override
    protected void doFilterInternal(final @NotNull HttpServletRequest request, final @NotNull HttpServletResponse response, final @NotNull FilterChain filterChain) throws IOException, ServletException, NotAuthorizedException {
        try {
            String authorization = request.getHeader("Authorization");
            String token = "";
            if (authorization.length() > 7) {
                token = authorization.substring(7);
            }
            String email = request.getHeader("email");
            if (Boolean.TRUE.equals(jwtUtils.validateToken(token, email))) {

                Claims claims = jwtUtils.getAllClaimsFromToken(token);

                final String AUTHORIZATION_ROLE = claims.get("role").toString();

                if (!AUTHORIZATION_ROLE.equalsIgnoreCase("ADMIN")) {
                    String requestUrl = request.getRequestURL().toString();
                    if (requestUrl.contains("product-categories/new") ||
                            requestUrl.contains("product-categories/update") ||
                            requestUrl.contains("product-categories/delete") ||
                            requestUrl.contains("products/new") ||
                            requestUrl.contains("products/low-stock") ||
                            requestUrl.contains("products/update") ||
                            requestUrl.contains("products/delete") ||
                            requestUrl.contains("transactions/report"))
                    {
                        ApiError error = new ApiError(401, "You do not have the privilege access to the URL", new Date());
                        response.getWriter().write(new Gson().toJson(error));
                        response.sendError(401);
                        return;
                    }
                }
                filterChain.doFilter(request, response);
            } else {
                ApiError error = new ApiError(401, "Invalid token id!", new Date());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(new Gson().toJson(error));
            }
        } catch (Exception e) {
            System.out.println(e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiError error = new ApiError(401, "Token id could not be found", new Date());
            response.getWriter().write(new Gson().toJson(error));
        }
    }
}