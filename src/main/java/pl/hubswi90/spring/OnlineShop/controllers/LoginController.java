package pl.hubswi90.spring.OnlineShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.hubswi90.spring.OnlineShop.domain.User;
import pl.hubswi90.spring.OnlineShop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(){

        return new ModelAndView("login");
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(HttpSession session){
        System.out.println("jjajajajj");
        ModelAndView modelAndView = new ModelAndView("admin/home");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

       session.setAttribute("userSessionAtribute", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
       session.setAttribute("adminMessage","Content Available Only for Users with Admin Role");

        return modelAndView;
    }

    @RequestMapping(value="/user/home", method = RequestMethod.GET)
    public ModelAndView Userhome(HttpSession session){
        ModelAndView modelAndView = new ModelAndView("user/home");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        session.setAttribute("userSessionAtribute", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        session.setAttribute("adminMessage","Content Available Only for Users with User Role");
        session.setAttribute("userId", user.getId());
        session.setAttribute("usernameEmail", user.getEmail());

        return modelAndView;
    }
}
