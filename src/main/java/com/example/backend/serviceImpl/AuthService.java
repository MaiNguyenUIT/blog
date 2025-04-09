package com.example.backend.serviceImpl;

import com.example.backend.config.JwtProvider;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.exception.BadRequestException;
import com.example.backend.mapper.UserAccountMapper;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.ValidationAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthService implements com.example.backend.service.AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserDetailService customerUserDetailService;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public AuthResponse signIn(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticate(email, password);

        User user = userRepository.findByemail(email);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String jwt =  jwtProvider.generatedToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        return authResponse;
    }

    @Override
    public void signUp(RegisterRequest registerRequest) {
        User user = userRepository.findByemail(registerRequest.getEmail());
        if(user != null){
            throw new BadRequestException("User is already existed with email " + registerRequest.getEmail());
        }
        if(!ValidationAccount.isValidEmail(registerRequest.getEmail()) || !ValidationAccount.isValidPassword(registerRequest.getPassword())){
            throw new BadRequestException("Email or password is incorrect");
        }
        User createdUser = UserAccountMapper.INSTANCE.toEntity(registerRequest);
        userRepository.save(createdUser);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadRequestException("Invalid username....");
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadRequestException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
    }
}
