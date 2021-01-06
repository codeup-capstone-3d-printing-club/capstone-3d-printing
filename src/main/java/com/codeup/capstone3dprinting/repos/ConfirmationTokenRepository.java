package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.ConfirmationToken;
import com.codeup.capstone3dprinting.models.User;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    ConfirmationToken findByUser(User user);
}