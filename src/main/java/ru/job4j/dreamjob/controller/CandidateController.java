package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

@ThreadSafe
@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidatesService;

    private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidatesService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("candidates", candidatesService.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {

        model.addAttribute("cities", cityService.findAll());
        return "candidates/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate) {
        candidatesService.save(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var candidateOptional = candidatesService.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute("message", "Резюме с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("candidate", candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, Model model) {
        var isUpdated = candidatesService.update(candidate);
        if (!isUpdated) {
            model.addAttribute("message", "Резюме с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/candidates";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = candidatesService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Резюме с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/candidates";
    }
}
