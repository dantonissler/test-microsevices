package br.com.blinkdev.auth.endpoint.controller;

import br.com.blinkdev.core.model.ApplicationUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("user")
@Api(value = "Endpooint to manage user's infomation")
public class UserInfoController {

    @GetMapping(path = "info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Will etrieve the infomation from the user available in the token", response = ApplicationUser.class)
    public ResponseEntity<ApplicationUser> getUseInfo(Principal principal) {
        ApplicationUser applicationUser = (ApplicationUser) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return new ResponseEntity<>(applicationUser, HttpStatus.OK);
    }
}
