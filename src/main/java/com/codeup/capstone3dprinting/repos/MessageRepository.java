package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipientEquals(User user);
    List<Message> findBySenderEquals(User user);
    Message findMessageById(Long id);
    Message findByMessage(String message);
}

