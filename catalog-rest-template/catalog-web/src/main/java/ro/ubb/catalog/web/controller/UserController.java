package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.model.User;
import ro.ubb.catalog.core.service.StudentService;
import ro.ubb.catalog.core.service.UserService;
import ro.ubb.catalog.web.converter.ConversionFactory;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.converter.StudentConverter;
import ro.ubb.catalog.web.dto.*;

@RestController
public class UserController {
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  CredentialsDto getTypeOfCurrentUser() {
    try{
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    org.springframework.security.core.userdetails.User currentUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    currentUser.getAuthorities().forEach(System.out::println);
    return new CredentialsDto(currentUser.getAuthorities().toArray()[0].toString());
    } catch(Exception e) {
      return new CredentialsDto("NONE");
    }
  }

}
