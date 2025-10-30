package com.example.auth.controller;

import com.example.auth.model.User;
import com.example.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // Cho phép gọi API từ file HTML local
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🟢 Đăng ký
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        String msg = authService.register(user);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", msg
        ));
    }
    

    // 🟢 Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        String token = authService.login(user);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "token", token
        ));
    }
    

    // 🧩 GET tạm cho phép mở URL trực tiếp test trên trình duyệt
    @GetMapping("/register")
    public ResponseEntity<String> getRegisterPage() {
        return ResponseEntity.ok("Hãy gửi POST JSON đến /auth/register để tạo tài khoản.");
    }

    @GetMapping("/login")
    public ResponseEntity<String> getLoginPage() {
        return ResponseEntity.ok("Hãy gửi POST JSON đến /auth/login để nhận JWT token.");
    }
}
