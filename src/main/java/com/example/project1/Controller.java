package com.example.project1;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private volatile boolean dbReady = false;

    @EventListener(ContextRefreshedEvent.class)
    public void connectToDb() {
        new Thread(() -> {
            try {
                System.out.println("[INFO] Начало подключения к базе данных...");
                Thread.sleep(20000); // Спим 20 секунд (имитация долгого коннекта)

                dbReady = true; // База готова!
                System.out.println("[INFO] Успешно подключились к базе данных!");
            } catch (InterruptedException e) {
                System.err.println("[ERROR] Подключение прервано");
            }
        }).start();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        if (dbReady) {
            response.put("status", "ok");
            return ResponseEntity.ok(response); // 200 OK
        } else {
            response.put("status", "starting");
            System.out.println("[INFO] Вызов /health: база ещё не готова, отдаем 503");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response); // 503
        }
    }

    @GetMapping("/")
    public Map<String, String> sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Java inside Kubernetes!");
        response.put("status", "OK");
        return response;
    }

    @PostMapping("/data")
    public Map<String, Object> handlePost(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("receivedData", payload);
        response.put("result", "Success");
        return response;
    }
}
