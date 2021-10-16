package com.example.BookShopApp.data.model;

import com.example.BookShopApp.data.ChangeUserDataForm;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
public class UserDataEdition {

    @Id
    private UUID id;
    private String name;
    private String mail;
    private String phone;
    private String password;

    private String userDetailEmail;

    private LocalDateTime expiredTime;

    public UserDataEdition(ChangeUserDataForm changeUserDataForm, String uuid, String email) {
        this.id = UUID.fromString(uuid);
        this.userDetailEmail = email;
        if (changeUserDataForm.getName() != null && StringUtils.hasText(changeUserDataForm.getName())) {
            this.name = changeUserDataForm.getName();
        }
        if (changeUserDataForm.getMail() != null && StringUtils.hasText(changeUserDataForm.getMail())) {
            this.mail = changeUserDataForm.getMail();
        }
        if (changeUserDataForm.getPassword() != null && StringUtils.hasText(changeUserDataForm.getPassword()) &&
                changeUserDataForm.getPassword().equals(changeUserDataForm.getPasswordReply())) {
            this.password = changeUserDataForm.getPassword();
        }
        if (changeUserDataForm.getPhone() != null && StringUtils.hasText(changeUserDataForm.getPhone())) {
            this.phone = changeUserDataForm.getPhone();
        }
        this.expiredTime = LocalDateTime.now().plusMinutes(10);
    }

    public UserDataEdition() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserDetailEmail() {
        return userDetailEmail;
    }

    public void setUserDetailEmail(String userDetailEmail) {
        this.userDetailEmail = userDetailEmail;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
}
