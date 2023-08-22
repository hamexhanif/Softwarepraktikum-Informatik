package pizzashop.personal;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@PreAuthorize("isAuthenticated()")
class PersonalController {

	private final PersonalManagement personalManagement;

	PersonalController(PersonalManagement personalManagement) {

		Assert.notNull(personalManagement, "PersonalManagement must not be null!");

		this.personalManagement = personalManagement;
	}

	
	@PostMapping("/personal/register")	
	String registerNew(@Valid PersonalRegistrationForm form, BindingResult result,
					   Errors errors, RedirectAttributes attributes) {
		if (personalManagement.containsUsername(form.getName())){
			result.addError(new ObjectError("global", "Dieser Name existiert bereits."));
		}
		if (errors.hasErrors() || result.hasErrors()) {
			return "register_personal";
		}
		
		personalManagement.createPersonal(form);
		attributes.addFlashAttribute("erfolgreich", "personal.erfolgreich");
		
		return "redirect:/personal";
	}

	@PreAuthorize("hasRole('BOSS')")
	@GetMapping("/personal/register")
	String register(Model model, PersonalRegistrationForm form) {
		return "register_personal";
	}

	@GetMapping("/personal")
	@PreAuthorize("hasRole('BOSS')")
	String personal(Model model) {

		model.addAttribute("personalList", personalManagement.findAll());

		return "personal";
	}
	

	@PreAuthorize("hasRole('BOSS')")
	@PostMapping("/personal/{id}")
	String deletePersonal(@PathVariable long id) {
		String role = personalManagement.findById(id).getJob();
		personalManagement.deletePersonal(id);


		return "redirect:/personal";
	}

}
