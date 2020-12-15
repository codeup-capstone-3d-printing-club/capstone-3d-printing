package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
  File findByOwner(User user);
  List<File> findAllByOwner_Id(long id);
  List<File> findAllByOwner(User user);
  List<File> findAllByisFlagged(boolean isFlagged);
}
