package come.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private Timestamp sent_at;

}
