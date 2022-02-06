package test.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import test.DTO.UserDTO;
import test.Service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 아무런 URL이 없는 (localhost:8080) 경우는 loginPage로 Redirect.
    @GetMapping
    public String loginRedirect(){
        return "redirect:/loginPage";
    }

    // 로그인 페이지 이동
    @GetMapping("/loginPage")
    public String loginPage(){
        return "loginpage";
    }

    // 회원가입 페이지 이동
    @GetMapping("/registerPage")
    public String RegisterPage(){
        return "registerpage";
    }

    // 회원가입 진행
    @PostMapping("/register_action")
    public String registerAction(UserDTO userDTO){
        userService.saveUserData(userDTO);
        return "redirect:/loginPage";
    }

    // 로그인 실패
    @GetMapping("/login_fail")
    public String loginFail(){
        return "loginfail";
    }

    // 로그인 성공
    // Authentication는 인증된 사용자의 이름, 권한 등을 불러오는 역할을 한다.
    @GetMapping("/login_success")
    public String loginSuccess(Model model, Authentication authentication){
        UserDTO userDTO = (UserDTO)authentication.getPrincipal();
        model.addAttribute("info","아이디 : "+userDTO.getUserId()+", 이름 : "
                +userDTO.getUserName()+"님 환영합니다.");
        return "boardPage";
    }
}
