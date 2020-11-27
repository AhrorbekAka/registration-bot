package bot.registration.repository;

import bot.registration.entity.TelegramMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TelegramMessageRepository extends JpaRepository<TelegramMessage, UUID> {
    TelegramMessage findByChatId(Long chatId);

    Optional<List<TelegramMessage>> findAllByChatId(Long chatId);

    void deleteAllByChatId(Long chatId);
}
