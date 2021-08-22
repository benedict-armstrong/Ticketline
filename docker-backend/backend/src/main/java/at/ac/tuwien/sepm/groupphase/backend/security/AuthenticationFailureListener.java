package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserService userService;

    @Autowired
    public AuthenticationFailureListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String userName = (String) event.getAuthentication().getPrincipal();

        ApplicationUser applicationUser = userService.findApplicationUserByEmail(userName, false);
        applicationUser.setPoints(applicationUser.getPoints() + 1);

        if (applicationUser.getPoints() >= 3) {
            applicationUser.setStatus(ApplicationUser.UserStatus.BANNED);
        }

        userService.updateUser(applicationUser, true);
    }

}