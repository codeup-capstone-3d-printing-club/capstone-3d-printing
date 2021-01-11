package com.codeup.capstone3dprinting.repos;

import com.codeup.capstone3dprinting.models.File;
import com.codeup.capstone3dprinting.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> getAllByFile_Id(long id);

}

