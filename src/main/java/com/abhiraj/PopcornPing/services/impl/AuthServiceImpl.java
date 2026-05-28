package com.abhiraj.PopcornPing.services.impl;

import com.abhiraj.PopcornPing.domain.dtos.requests.LoginRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.RegisterRequestDto;
import com.abhiraj.PopcornPing.domain.dtos.requests.VerifyUserDto;
import com.abhiraj.PopcornPing.domain.dtos.response.JwtResponseDto;
import com.abhiraj.PopcornPing.domain.dtos.response.UserResponseDto;
import com.abhiraj.PopcornPing.domain.entities.User;
import com.abhiraj.PopcornPing.jwt.JwtService;
import com.abhiraj.PopcornPing.mappings.UserMapping;
import com.abhiraj.PopcornPing.repositories.UserMovieTrackerRepository;
import com.abhiraj.PopcornPing.repositories.UserRepository;
import com.abhiraj.PopcornPing.services.AuthService;
import com.abhiraj.PopcornPing.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMovieTrackerRepository userMovieTrackerRepository;
    private final UserMapping userMapping;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public UserResponseDto addNewUser(RegisterRequestDto registerRequestDto) {
        User user = userMapping.registerRequestToUser(registerRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setIsEnabled(false);
        sendVerificationEmail(user);
        User savedUser = userRepository.save(user);
        Long movieCount = userMovieTrackerRepository.countByUserId(savedUser.getId());
        return userMapping.userToResponse(savedUser, movieCount);
    }

    @Override
    public JwtResponseDto authenticate(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() ->
                    new UsernameNotFoundException("No user with email "+loginRequestDto.getEmail())
                );

        if(!user.getIsEnabled()){
            throw new RuntimeException("Account not verified. Please verify your account to continue");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()
        ));
        if(!authentication.isAuthenticated()){
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(user);
    }

    @Override
    public void verifyUser(VerifyUserDto verifyUserDto) {
        Optional<User> optionalUser = userRepository.findByEmail(verifyUserDto.getEmail());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code is expired!!");
            }
            if(user.getVerificationCode().equals(verifyUserDto.getVerificationCode())){
                user.setIsEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            }else{
                throw new RuntimeException("Invalid verification code");
            }
        }else{
            throw new UsernameNotFoundException("No user with email "+verifyUserDto.getEmail());
        }
    }

    @Override
    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getIsEnabled()){
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));
            userRepository.save(user);
            sendVerificationEmail(user);
        }else{
            throw new UsernameNotFoundException("No user with this email");
        }
    }

    private void sendVerificationEmail(User user){
        String subject = "Account verification for PopcornPing";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<!DOCTYPE html>"
                + "<html><body style='font-family:Arial,sans-serif;background:#f4f4f4;padding:20px'>"
                + "<div style='max-width:500px;margin:auto;background:#fff;padding:30px;border-radius:10px;text-align:center'>"
                + "<h1 style='color:#ff6b6b'>PopcornPing</h1>"
                + "<p>Your verification code is:</p>"
                + "<h2 style='color:#ff6b6b;letter-spacing:5px'>" + verificationCode + "</h2>"
                + "<p>This code expires in <b>10 minutes</b>.</p>"
                + "<p style='color:gray;font-size:12px'>If you didn't request this, ignore this email.</p>"
                + "</div>"
                + "</body></html>";

        try{
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        }catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email");
        }
    }

    private String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000)+100000;
        return String.valueOf(code);
    }
}
