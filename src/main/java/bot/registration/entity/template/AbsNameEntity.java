package bot.registration.entity.template;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class AbsNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nameUz;

    @Column(nullable = false)
    private String nameRu;

    @Column(nullable = false)
    private String nameEn;

}
