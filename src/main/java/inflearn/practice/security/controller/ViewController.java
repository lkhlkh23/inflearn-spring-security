package inflearn.practice.security.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/")
	public String index(final Model model, final Principal principal) {
		model.addAttribute("message", principal == null ? "Hello Spring Security" : "Hello " + principal.getName());
		return "index";
	}

	@GetMapping("/info")
	public String info(final Model model) {
		model.addAttribute("message", "info");
		return "info";
	}

	@GetMapping("/dashboard")
	public String dashboard(final Model model, final Principal principal) {
		model.addAttribute("message", "Hello " + principal.getName());
		return "dashboard";
	}

	@GetMapping("/admin")
	public String admin(final Model model, final Principal principal) {
		model.addAttribute("message", "Hello Admin, " + principal.getName());
		return "admin";
	}

}
