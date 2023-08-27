package dev.jgerard.ngsgrocerylist;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AngularController {
    /**
     * Serve index.html to all paths without a file extension.
     * This handler uses a legacy Ant-style path pattern.
     */
    @GetMapping("**/{path:[^.]*}")
    public String serveAngularApp() {
        return "forward:/";
    }
}