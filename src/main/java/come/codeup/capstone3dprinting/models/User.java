package come.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(nullable = false, length = 45)
        private String username;

        @Column(length = 45)
        private String first_name;

        @Column(length = 45)
        private String last_name;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private boolean is_verified;

        @Column(nullable = false)
        private Timestamp joined_at;

        @Column(nullable = false)
        private boolean is_admin;

        @Column
        private String avatar_url;

}
