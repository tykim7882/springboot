package org.example.board.springboot.web;

import lombok.RequiredArgsConstructor;
import org.example.board.springboot.config.auth.LoginUser;
import org.example.board.springboot.config.auth.dto.SessionUser;
import org.example.board.springboot.service.PostsService;
import org.example.board.springboot.web.dto.PostsResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        model.addAttribute("posts",postsService.findAllDesc());

        if (user != null) {
            //model.addAttribute("userName", user.getName());
            model.addAttribute("user", user);
        }

        Math.random();
        System.out.println();

        return "index";
    }



    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }

}
