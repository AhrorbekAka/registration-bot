package bot.registration.entity;

import bot.registration.entity.enums.CityDistrict;
import bot.registration.entity.enums.ClassNumber;
import bot.registration.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant extends AbsEntity {
    private String lastName;
    private String firstName;
    private String passport;
    private String phoneNumber;
    private Integer userId;

    @Column(unique = true)
    private Long chatId;

    @Enumerated(value = EnumType.STRING)
    private CityDistrict cityDistrict;
    private Integer schoolNumber;
    @Enumerated(value = EnumType.STRING)
    private ClassNumber classNumber;
}
