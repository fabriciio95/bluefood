package br.com.bluefood.infrastructure.web.controller;

import org.springframework.ui.Model;

public class ControllerHelper {

	public static void setEditeMode(Model model, boolean isEdit) {
		model.addAttribute("editMode", isEdit);
	}
}
