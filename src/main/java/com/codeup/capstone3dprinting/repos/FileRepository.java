package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {


}
