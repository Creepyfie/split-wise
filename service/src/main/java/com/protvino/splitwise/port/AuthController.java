package com.protvino.splitwise.port;

import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.service.UserService;
import com.protvino.splitwise.port.dto.UserDto;
import com.protvino.splitwise.service.AcceptRegistrationInAppCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

 //   private final UserDto userDto;
    private UserService userService;
    private final AcceptRegistrationInAppCase aCase;

    @GetMapping("/register")
    public void registrationForm() {
    }

    @PostMapping("/registration")
    public  UserDto performRegistration(EditUserRequest userRequest){
            aCase.acceptRegistration(userRequest.getUserName(),userRequest.getPassword());
        return null;
    }
}
