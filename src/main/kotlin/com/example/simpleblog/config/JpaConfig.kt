package com.example.simpleblog.config

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.P6SpyOptions
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import jakarta.annotation.PostConstruct
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.text.SimpleDateFormat
import java.util.*


@Configuration
@EnableJpaAuditing
class JpaConfig {

    @Configuration
    class P6spyConfig {
        @PostConstruct
        fun setLogMessageFormat() {
            P6SpyOptions.getActiveInstance().logMessageFormat = P6spyPrettySqlFormatter::class.java.name
        }
    }
}

class P6spyPrettySqlFormatter : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int
        , now: String
        , elapsed: Long
        , category: String
        , prepared: String
        , sql: String
        , url: String
    ): String {
        var sqlQuery: String? = sql
        sqlQuery = formatSql(category, sqlQuery)
        val currentDate = Date()
        val simpleDateFormat = SimpleDateFormat("yy.MM.dd HH:mm:ss")
        //return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + sql;
        return simpleDateFormat.format(currentDate) + " | " + "OperationTime : " + elapsed + "ms" + sqlQuery
    }

    private fun formatSql(category: String, sql: String?): String? {
        var sqlQuery = sql
        if (sqlQuery == null || sqlQuery.trim { it <= ' ' } == "") return sqlQuery

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.name.equals(category)) {
            val tempSql = sqlQuery.trim { it <= ' ' }.lowercase()
            sqlQuery =
                if (tempSql.startsWith("create") || tempSql.startsWith("alter") || tempSql.startsWith("comment")) {
                    FormatStyle.DDL.formatter.format(sqlQuery)
                } else {
                    FormatStyle.BASIC.formatter.format(sqlQuery)
                }
            sqlQuery = "|\nHeFormatSql(P6Spy sql,Hibernate format):$sqlQuery"
        }
        return sqlQuery
    }
}

