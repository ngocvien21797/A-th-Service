package com.example.auth.service;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Đăng ký
    public String register(User user) {
        // 1. Kiểm tra trùng
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        // 2. Kiểm tra password có gửi lên không
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password không được để trống");
        }

        // 3. Mã hoá trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 4. Gán role mặc định nếu chưa có
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        // 5. Lưu DB
        userRepo.save(user);

        return "Đăng ký thành công";
    }

    // Đăng nhập
    public String login(User req) {
        // 1. Tìm user theo username
        var user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Sai tài khoản hoặc mật khẩu"));

        // 2. So sánh password
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
        }

        // 3. Sinh token
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}
