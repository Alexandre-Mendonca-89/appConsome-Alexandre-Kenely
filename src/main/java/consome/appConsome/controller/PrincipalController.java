package consome.appConsome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrincipalController {
	
	@GetMapping("/")
	public ModelAndView informacep() {
		ModelAndView modelIndex = new ModelAndView("Index");
		modelIndex.addObject("varNaController", "teste teste teste");
		return modelIndex;
	}
	
}
