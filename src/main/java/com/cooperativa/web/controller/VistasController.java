package com.cooperativa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistasController {

	@GetMapping("/init")
	public String vistaInicio() {
		return "index";
	}
	
	@GetMapping("/cuenta")
	public String vistaCuenta() {
		return "cuenta";
	}
}
