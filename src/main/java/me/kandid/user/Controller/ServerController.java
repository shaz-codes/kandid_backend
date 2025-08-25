package me.kandid.user.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/")
public class ServerController {
    @GetMapping("/")
    public ResponseEntity<?> serverStatus() {
        List<String> lyrics = List.of(
                "Hello from the other side.",
                "What you know about rollin' down in the deep?",
                "I’m gonna take my horse to the old town road.",
                "Never gonna give you up.",
                "I’ll tell you what I want, what I really really want.",
                "O-i-i-a-i-o, o-i-i-i-a-i",
                "Meoww meowwww meoww meow"
        );
        Map<String, String> map = new HashMap<>();
        map.put("docs", "/docs.html");
        map.put("status", "OK");
        map.put("message", "server do be servering");
        map.put("lyric of the message", lyrics.get((int) (Math.random() * (lyrics.size() - 1))));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
