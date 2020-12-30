package com.codeup.capstone3dprinting.repos;
import com.codeup.capstone3dprinting.models.FileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagesRepository extends JpaRepository <FileImage, Long>{
    List<FileImage> getAllByFile_Id(long file_id);
}
