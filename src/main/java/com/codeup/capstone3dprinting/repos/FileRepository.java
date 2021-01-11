package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.Category;
import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.Rating;
import com.codeup.capstone3dprinting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
  List<File> findAllByOwner_Id(long id);
  List<File> findAllByOwner(User user);
  List<File> findAllByisFlagged(boolean isFlagged);
  List<File> findByCategories(Category category);
  List<File> findAllByDescriptionIsLike(String searchTerm);
  List<File> findAllByTitleIsLike(String searchTerm);
  File findByTitle(String title);

  @Query(value = "SELECT * FROM files ORDER BY average_rating DESC LIMIT 5",
          nativeQuery = true)
  List<File> getTop5();

}
