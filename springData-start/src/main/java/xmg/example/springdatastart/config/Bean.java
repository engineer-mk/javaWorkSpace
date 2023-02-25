package xmg.example.springdatastart.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * @author makui
 * @created on  2022/11/6
 **/
@Configuration
public class Bean {
    @org.springframework.context.annotation.Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
