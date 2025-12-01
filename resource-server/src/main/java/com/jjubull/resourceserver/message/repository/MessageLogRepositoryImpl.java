package com.jjubull.resourceserver.message.repository;

import com.jjubull.resourceserver.message.domain.QMessageLog;
import com.jjubull.resourceserver.message.domain.Result;
import com.jjubull.resourceserver.message.dto.command.MessageLogDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MessageLogRepositoryImpl implements MessageLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MessageLogDto> findMessageLog(LocalDateTime from, LocalDateTime to, Result result, Pageable pageable) {

        List<MessageLogDto> mainQuery = queryFactory
                .select(Projections.constructor(MessageLogDto.class,
                        QMessageLog.messageLog.recipientPhone,
                        QMessageLog.messageLog.content,
                        QMessageLog.messageLog.timeStamp,
                        QMessageLog.messageLog.result))
                .from(QMessageLog.messageLog)
                .where(resultEq(result), fromGoe(from), toLoe(to))
                .orderBy(QMessageLog.messageLog.timeStamp.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(QMessageLog.messageLog.count())
                .from(QMessageLog.messageLog)
                .where(resultEq(result), fromGoe(from), toLoe(to));

        return PageableExecutionUtils.getPage(mainQuery, pageable, countQuery::fetchOne);
    }


    private BooleanExpression resultEq(Result result) {
        return result == null ? null : QMessageLog.messageLog.result.eq(result);
    }

    private BooleanExpression fromGoe(LocalDateTime from) {
        return from != null ? QMessageLog.messageLog.timeStamp.goe(from) : null;
    }

    private BooleanExpression toLoe(LocalDateTime to) {
        return to != null ? QMessageLog.messageLog.timeStamp.loe(to) : null;
    }
}
