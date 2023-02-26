package com.example.springdatastart.base;

import org.hibernate.Hibernate;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({"unchecked"})
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, QuerydslPredicateExecutor<T> {

    @Modifying
    @Query("delete from #{#entityName} e where  e.id in(:ids)")
    @javax.transaction.Transactional
    void deleteByIds(Iterable<Long> ids);

    @Override
    @NonNull
    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    List<T> findAllById(@NonNull Iterable<ID> ids);

    @Override
    @NonNull
    //打开查询缓存
    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "false")})
    List<T> findAll(Specification<T> specification);

    @Override
    @NonNull
    //打开查询缓存
    @QueryHints({@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "false")})
    Page<T> findAll(Specification<T> specification, @NonNull Pageable pageable);


    //当开启二级缓存，从缓存获取数据时候会丢失lazy属性
    @Transactional(readOnly = true)
    default Optional<T> findByIdWithLazyField(ID id, Function<T, Object>... functions) {
        final Optional<T> optional = this.findById(id);
        if (!optional.isPresent()) {
            return optional;
        }
        final T t = optional.get();
        for (Function<T, Object> function : functions) {
            Hibernate.initialize(function.apply(t));
        }
        return Optional.of(t);
    }


    @Transactional(readOnly = true)
    default List<T> findAll(BaseQuery query) {
        final Class<T> entityType = getEntityType();
        final Specification<T> specification = query.getSpecification(entityType, false);
        final List<T> list = this.findAll(specification);
        final List<Method> fetchFields = query.getFetchFields(entityType);
        if (fetchFields.size() > 0) {
            for (T t : list) {
                for (Method method : fetchFields) {
                    try {
                        Hibernate.initialize(method.invoke(t));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("all")
    default Page<T> findPage(BaseQuery query) {
        final Class<T> entityType = getEntityType();
        final Specification<T> specification = query.getSpecification(entityType, true);
        final Page<T> page = this.findAll(specification, query.getPageable());
        final List<Method> fetchFields = query.getFetchFields(entityType);
        if (fetchFields.size() > 0) {
            for (T t : page.getContent()) {
                for (Method method : fetchFields) {
                    try {
                        Hibernate.initialize(method.invoke(t));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return page;
    }


    //获取泛型类型
    default Class<T> getEntityType() {
        return (Class<T>) ResolvableType.forClass(getClass()).as(BaseRepository.class).getGeneric(0).resolve();
    }
}
