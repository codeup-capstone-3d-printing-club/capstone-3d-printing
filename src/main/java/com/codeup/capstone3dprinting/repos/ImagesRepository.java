package com.codeup.capstone3dprinting.repos;
import com.codeup.capstone3dprinting.models.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagesRepository extends JpaRepository <Images, Long>{
    List<Images> getAllByFile_Id(long file_id);
    Images getByFile_Id(long file_id);
}
