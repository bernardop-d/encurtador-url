package com.portfolio.urlshortener;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final Map<String, String> urlStore = new ConcurrentHashMap<>();

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody String originalUrl) {

        if (originalUrl == null || originalUrl.isBlank()) {
            return ResponseEntity.badRequest().body("URL inválida");
        }

        String code = UUID.randomUUID().toString().substring(0, 6);
        urlStore.put(code, originalUrl);

        return ResponseEntity.ok("http://localhost:8080/api/r/" + code);
    }

    @GetMapping("/r/{code}")
    public void redirect(@PathVariable String code,
                         HttpServletResponse response) throws IOException {

        String originalUrl = urlStore.get(code);

        if (originalUrl != null) {
            response.sendRedirect(originalUrl);
        } else {
            response.sendError(404);
        }
    }
}