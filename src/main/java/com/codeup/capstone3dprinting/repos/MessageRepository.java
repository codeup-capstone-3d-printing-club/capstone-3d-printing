package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {


}

