package com.example.springdatastart.account.repository;

import com.example.springdatastart.account.entity.User;
import com.example.springdatastart.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author makui
 * @created on  2022/11/6
 **/
@Repository
public interface UserRepository extends BaseRepository<User,Long> {

    @EntityGraph("user.rm")//加载lazy属性
    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "false")})
    List<User> findByUsername(String username);

    @Override
    @EntityGraph("user.rm")
    Page<User> findAll(Pageable pageable);
}
