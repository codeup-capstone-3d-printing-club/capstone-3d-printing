package come.codeup.capstone3dprinting.repos;

import come.codeup.capstone3dprinting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
