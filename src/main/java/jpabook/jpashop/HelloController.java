package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        /**
         * spring boot의 thymleaf가 자동으로 resources > templates 내, 해당하는 html 소스를 찾아준다.
         */
        return "hello";
    }
}
