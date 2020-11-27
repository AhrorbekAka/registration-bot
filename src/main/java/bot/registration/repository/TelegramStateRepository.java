package bot.registration.repository;

import bot.registration.entity.TelegramState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TelegramStateRepository extends JpaRepository<TelegramState, UUID> {

    void deleteByChatId(Long tgId);

    Optional<TelegramState> findByChatId(Long tgId);

}
