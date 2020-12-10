package come.codeup.capstone3dprinting.repos;

import come.codeup.capstone3dprinting.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {


}

