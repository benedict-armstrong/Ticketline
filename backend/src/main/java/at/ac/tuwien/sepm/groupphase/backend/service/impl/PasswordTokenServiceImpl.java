package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.PasswordResetToken;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PasswordTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PasswordTokenService;
import org.springframework.stereotype.Service;

@Service
public class PasswordTokenServiceImpl implements PasswordTokenService {

    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordTokenServiceImpl(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordTokenRepository.save(passwordResetToken);
    }

    @Override
    public ApplicationUser findUserByToken(String token) {
        PasswordResetToken passwordResetToken = passwordTokenRepository.findOneByToken(token);

        if (passwordResetToken == null) {
            throw new NotFoundException("Not a valid change password link");
        }

        ApplicationUser user = passwordResetToken.getUser();
        passwordTokenRepository.delete(passwordResetToken);
        return user;
    }
}
