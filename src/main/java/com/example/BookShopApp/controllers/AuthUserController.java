package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.ChangePasswordForm;
import com.example.BookShopApp.data.ChangeUserDataForm;
import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.SmsCode;
import com.example.BookShopApp.data.services.BookstoreUserRegister;
import com.example.BookShopApp.data.services.MessageSenderService;
import com.example.BookShopApp.data.services.SmsService;
import com.example.BookShopApp.security.ContactConfirmationPayload;
import com.example.BookShopApp.security.ContactConfirmationResponse;
import com.example.BookShopApp.security.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.UUID;

@Controller
public class AuthUserController {
    private final BookstoreUserRegister bookstoreUserRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;
    private final MessageSenderService messageSenderService;

    //todo ask the question
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @Autowired
    public AuthUserController(BookstoreUserRegister bookstoreUserRegister, SmsService smsService, JavaMailSender javaMailSender, MessageSenderService messageSenderService) {
        this.bookstoreUserRegister = bookstoreUserRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
        this.messageSenderService = messageSenderService;
    }

    @GetMapping("/signin")
    public String handleSingIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSingUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bookstore00@mail.ru");
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(smsService.generateCode(), 300);
        smsService.saveNewCode(smsCode);
        message.setSubject("Bookstore mail verification");
        message.setText("Verification code id " + smsCode.getCode());
        javaMailSender.send(message);
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        bookstoreUserRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse response) {
        ContactConfirmationResponse loginResponse = bookstoreUserRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        response.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        model.addAttribute("curUser", bookstoreUserRegister.getCurrentUser());
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUser", bookstoreUserRegister.getCurrentUser());
        model.addAttribute("changeUserDataForm", new ChangeUserDataForm());
        return "profile";
    }

    @GetMapping("/403")
    public String handle403() {
        return "403";
    }

    @GetMapping("/401")
    public String handle401() {
        return "401";
    }

    @GetMapping("/changepassword")
    public String handleGetChangePassword(Model model) {
        model.addAttribute("changePassForm", new ChangePasswordForm());
        return "changepassword";
    }


    @PostMapping("/changeUserData")
    public String handleChangeUserDataRequest(ChangeUserDataForm changeUserDataForm, RedirectAttributes redirectAttributes,
                                              Principal principal) {
        if (!changeUserDataForm.getPassword().equals(changeUserDataForm.getPasswordReply())) {
            redirectAttributes.addFlashAttribute("changedUserDataMessage", "Пароли не совпадают.");
            return "redirect:/";
        }
        String email = defineUserEmail(principal);
        String changeUuid = UUID.randomUUID().toString();

        bookstoreUserRegister.saveTempUserDataChanges(changeUserDataForm, changeUuid, email);
        messageSenderService.sendMessageViaEmail(email, "Confirm data changing", "Для подтверждения изменений перейдите по ссылке " +
                "http://localhost:8082/confirmchanges/" + changeUuid);

        redirectAttributes.addFlashAttribute("changedUserDataMessage", "Ссылка для подтверждения учетных данных отправлена на ваш email.");
        return "redirect:/";
    }

    @GetMapping("/confirmchanges/{uuid}")
    public String handleConfirmDataChangesPage(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        if (bookstoreUserRegister.applyUserDataChanges(uuid)) {
            redirectAttributes.addFlashAttribute("changedUserDataMessage", "Учетные данные изменены.");
        } else {
            redirectAttributes.addFlashAttribute("changedUserDataMessage", "Учетная запись не найдена.");
        }
        return "redirect:/";
    }

    private String defineUserEmail(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            return ((DefaultOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal()).getAttribute("email");
        } else {
            return principal.getName();
        }
    }

}
