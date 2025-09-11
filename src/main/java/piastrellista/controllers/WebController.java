package piastrellista.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bettellipavimenti")
public class WebController {

    @GetMapping
    public String home() {
        return "src/page/index";
    }

    @GetMapping("/blog")
    public String blog() {
        return "src/page/Blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetail(@PathVariable String id) {
        return "src/page/BlogDetail";
    }

    @GetMapping("/chisiamo")
    public String chiSiamo() {
        return "src/page/ChiSiamo";
    }

    @GetMapping("/contatti")
    public String contatti() {
        return "src/page/Contatti";
    }

    @GetMapping("/servizi")
    public String servizi() {
        return "src/page/Service";
    }

    @GetMapping("/materiali")
    public String materiali() {
        return "src/page/Material";
    }

    @GetMapping("/admin")
    public String admin() {
        return "src/page/Admin";
    }
}