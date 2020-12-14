package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.Comment;
import com.codeup.capstone3dprinting.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
