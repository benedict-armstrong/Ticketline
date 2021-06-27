package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.PasswordResetToken;

public interface PasswordTokenService {

    /**
     * save a password reset token.
     *
     * @param passwordResetToken to be saved
     * @return the saved password reset token
     */
    PasswordResetToken save(PasswordResetToken passwordResetToken);

    /**
     * find user by its corresponding token and delete the token.
     *
     * @param token used to identify the user
     * @return the correct user
     */
    ApplicationUser findUserByToken(String token);

    /**
     * checks for expires tokens every half hour and deletes them.
     */
    void deleteExpiredTokens();
}
