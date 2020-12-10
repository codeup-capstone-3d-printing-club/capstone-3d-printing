package come.codeup.capstone3dprinting.models;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private File file_id;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private Timestamp created_at;

}
