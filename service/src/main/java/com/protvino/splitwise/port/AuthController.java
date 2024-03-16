package com.protvino.splitwise.port;

import com.protvino.splitwise.domain.request.EditUserRequest;
import com.protvino.splitwise.service.UserService;
import com.protvino.splitwise.port.dto.UserDto;
import com.protvino.splitwise.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

 //   private final UserDto userDto;
    private UserService userService;
    private final RegistrationService aCase;

    @GetMapping("/register")
    public void registrationForm() {
    }

    @PostMapping("/registration")
    public  UserDto performRegistration(EditUserRequest userRequest){
            aCase.register(userRequest.getUserName(),userRequest.getPassword());
        return null;
    }
}
