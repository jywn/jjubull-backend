package com.jjubull.resourceserver.message.repository;

import com.jjubull.resourceserver.message.domain.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog,Long>, MessageLogRepositoryCustom {
}
