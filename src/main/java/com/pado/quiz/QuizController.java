package com.pado.quiz;

import com.pado.quiz.data.gad.QuizData;
import com.pado.quiz.dto.ResultData;
import com.pado.quiz.infrastructure.GADEntity;
import com.pado.quiz.infrastructure.GADRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizData quizData;
    private final GADRepository gadRepository;

    @GetMapping("/gad/intro")
    public String intro() {
        return "gad/index";
    }

    @GetMapping("/gad")
    public String quiz(Model model) {
        model.addAttribute("questions", quizData.getQuestions());
        return "gad/quiz";
    }

    @PostMapping("/gad")
    public String result(@RequestParam Map<String, String> params, Model model) {
        int total = params.entrySet().stream()
            .filter(e -> e.getKey().startsWith("q"))
            .mapToInt(e -> Integer.parseInt(e.getValue()))
            .sum();

        gadRepository.save(new GADEntity(total));

        ResultData result = quizData.getResult(total);

        model.addAttribute("score", total);
        model.addAttribute("result", result);
        model.addAttribute("average", gadRepository.getAverageScore());

        return "gad/result";
    }
}