package org.example.prm392_groupprojectbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.accounts.AccountResponseDTO;
import org.example.prm392_groupprojectbe.dtos.auth.requests.*;
import org.example.prm392_groupprojectbe.dtos.auth.response.AuthResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.enums.AccountRoleEnum;
import org.example.prm392_groupprojectbe.enums.AccountStatusEnum;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.mappers.AccountMapper;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.services.AuthService;
import org.example.prm392_groupprojectbe.services.EmailService;
import org.example.prm392_groupprojectbe.utils.AccountUtils;
import org.example.prm392_groupprojectbe.utils.JwtUtil;
import org.example.prm392_groupprojectbe.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    @Lazy
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private static final int OTP_EXPIRY_MINUTES = 10;
    private static final int OTP_COOLDOWN_SECONDS = 30;

    @Override
    public Account register(RegisterRequestDTO requestDTO) {
        Optional<Account> existingAccount = accountRepository.findByEmail(requestDTO.getEmail());

        if (existingAccount.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Account account = Account.builder()
                .name(requestDTO.getName())
                .status(AccountStatusEnum.UNVERIFIED)
                .role(AccountRoleEnum.USER)
                .phoneNumber(requestDTO.getPhoneNumber())
                .gender(requestDTO.getGender())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .otp(RandomUtil.generateOtp(6))
                .otpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .otpCreatedAt(LocalDateTime.now())
                .build();
        Account savedAccount = accountRepository.save(account);

        //emailService.sendVerifyAccountEmail(account.getEmail(), account.getName(), account.getOtp());

        return savedAccount;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.WRONG_CREDENTIALS);
        }
        catch (DisabledException e) {
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
        catch (LockedException e) {
            throw new AppException(ErrorCode.ACCOUNT_BANNED);
        }

        Account account = accountRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
//        if (!AccountStatusEnum.VERIFIED.equals(account.getStatus())) {
//            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFIED);
//        }
        String jwt = jwtUtil.generateToken(account);
        return AuthResponseDTO.builder()
                .token(jwt)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void sendOtp(SendOtpRequestDTO requestDTO) {
        Account account = accountRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (account.getOtpCreatedAt() != null &&
                account.getOtpCreatedAt().plusSeconds(OTP_COOLDOWN_SECONDS).isAfter(LocalDateTime.now())) {
            throw new AppException(ErrorCode.OTP_REQUEST_TOO_FREQUENT);
        }

        String otp = RandomUtil.generateOtp(6);
        account.setOtp(otp);
        account.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        account.setOtpCreatedAt(LocalDateTime.now());

        accountRepository.save(account);

        emailService.sendVerifyAccountEmail(account.getEmail(), account.getName(), otp);
    }

    @Override
    public void verifyOtp(VerifyOtpRequestDTO request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!account.isOtpValid(request.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        account.setStatus(AccountStatusEnum.VERIFIED);
        account.setOtp(null);
        account.setOtpExpiry(null);
        accountRepository.save(account);
    }

    @Override
    public AccountResponseDTO updateProfile(UpdateProfileRequestDTO requestDTO) {
        Account currentAccount = AccountUtils.getCurrentAccount();

        if (currentAccount == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (requestDTO.getName() != null) {
            currentAccount.setName(requestDTO.getName());
        }
        if (requestDTO.getPhoneNumber() != null) {
            currentAccount.setPhoneNumber(requestDTO.getPhoneNumber());
        }
        if (requestDTO.getGender() != null) {
            currentAccount.setGender(requestDTO.getGender());
        }
        if (requestDTO.getAvatar() != null) {
            currentAccount.setAvatar(requestDTO.getAvatar());
        }

        Account result = accountRepository.save(currentAccount);
        return accountMapper.toDTO(result);
    }

    public void requestResetPassword() {

    }

    public void resetPassword() {

    }
}
