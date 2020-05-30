package ro.ubb.catalog.web.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{

  @Override
  public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
      throws IOException, ServletException {
    if (authentication != null && authentication.getDetails() != null) {
      try {
        httpServletRequest.getSession().invalidate();
        // you can add more codes here when the user successfully logs
        // out,
        // such as updating the database for last active.
      } catch (Exception e) {
        e.printStackTrace();
        e = null;
      }
    }

    httpServletResponse.setStatus(HttpServletResponse.SC_OK);

  }
}