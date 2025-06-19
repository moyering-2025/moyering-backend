package com.dev.moyering.admin.controller;

import com.dev.moyering.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminLoginController {

    @Autowired
    private UserService userService;

    // 로그인 처리 추가
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            System.out.println("로그인 요청 받음 - 사용자명: " + username);

            // 임시 하드코딩된 관리자 계정
            if ("admin".equals(username) && "manager".equals(password)) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", "temporary-jwt-token-" + System.currentTimeMillis());
                response.put("role", "ROLE_MG");
                response.put("message", "로그인 성공");

                System.out.println("로그인 성공!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "아이디 또는 비밀번호가 잘못되었습니다.");
                System.out.println("로그인 실패 - 잘못된 인증정보");
                return ResponseEntity.status(401).body(errorResponse);
            }

        } catch (Exception e) {
            System.err.println("로그인 처리 중 오류: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // 관리자 권한 체크 (로그인 후 /admin/verify 호출용)
    @GetMapping("/admin/verify")
    public ResponseEntity<?> verifyAdmin(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                // JWT 토큰에서 역할 확인
                // 여기에 JWT 검증 로직 추가 필요
                return ResponseEntity.ok().body(Map.of(
                        "status", "success",
                        "message", "관리자 권한 확인됨"
                ));
            }
            return ResponseEntity.status(401).body(Map.of("error", "권한이 없습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}