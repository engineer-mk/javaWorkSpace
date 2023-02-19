package com.example.springdatastart.account.service;

import com.example.springdatastart.account.entity.*;
import com.example.springdatastart.account.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author makui
 * @created on  2022/11/6
 **/
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;
    private final Session session;

    @Transactional
    public User getOne(Long id) {
        final User one = userRepository.getOne(id);
        final Set<Resource> resourceList = one.getResourceList();
        final Set<Menu> menuList = one.getMenuList();
        for (Resource resource : resourceList) {
            System.out.println(resource.getId());
        }
        for (Menu menu : menuList) {
            System.out.println(menu.getId());
        }
        return one;
    }


    /**
     * EntityManager createQuery(String qlString, Class<T> resultClass);
     *
     * @param username
     * @return
     */
    public List<User> sqlByEm(String username, String roleName, String sourceName) {
        return entityManager.createQuery("select u from User u where u.username=:uname", User.class)
                .setParameter("uname", username)
                .getResultList();
    }

    /**
     * session createQuery(String queryString, Class<T> resultType);
     *
     * @param username
     * @return
     */
    public List<User> sqlByHb(String username, String roleName, String sourceName) {
        return session.createQuery("select u from User u where u.username=:uname", User.class)
                .setCacheable(true)
                .setParameter("uname", username)
                .getResultList();
    }

    /**
     * EntityManager createQuery(CriteriaQuery<T> criteriaQuery);
     * 查询指定字段
     *
     * @param username
     * @param roleName
     * @param sourceName
     * @return
     */
    @Transactional
    public Page<User> byEmCriteriaQuery(String username, String roleName, String sourceName, Integer page, Integer size) {
        page = page == null ? 0 : Math.max(page - 1, 0);
        size = size == null ? 10 : size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<User> countRoot = countQuery.from(User.class);
        final Root<User> root = criteriaQuery.from(User.class);
        final EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
        entityGraph.addSubgraph(User_.menuList)
                .addSubgraph(Menu_.P_MENU);
        entityGraph.addSubgraph(User_.resourceList)
                .addSubgraph(Resource_.P_RESOURCE);
        entityGraph.addSubgraph(User_.pUser);
        final ArrayList<Predicate> predicates = new ArrayList<>();
        final ArrayList<Predicate> countPredicates = new ArrayList<>();
        final SetJoin<User, Menu> menuSetJoin = root.join(User_.menuList, JoinType.LEFT);
        menuSetJoin.join(Menu_.P_MENU, JoinType.LEFT);
        final SetJoin<User, Resource> resourceSetJoin = root.join(User_.resourceList, JoinType.LEFT);
        resourceSetJoin.join(Resource_.pResource, JoinType.LEFT);

        final SetJoin<User, Menu> countMenuSetJoin = countRoot.join(User_.menuList, JoinType.LEFT);
        countMenuSetJoin.join(Menu_.P_MENU, JoinType.LEFT);
        final SetJoin<User, Resource> countResourceSetJoin = countRoot.join(User_.resourceList, JoinType.LEFT);
        countResourceSetJoin.join(Resource_.pResource, JoinType.LEFT);
        if (StringUtils.hasLength(username)) {
            predicates.add(cb.equal(root.get(User_.username), username));
            countPredicates.add(cb.equal(countRoot.get(User_.username), username));
        }
        if (StringUtils.hasLength(roleName)) {
            predicates.add(cb.equal(menuSetJoin.get(Menu_.name), roleName));
            countPredicates.add(cb.equal(countMenuSetJoin.get(Menu_.name), roleName));
        }
        if (StringUtils.hasLength(sourceName)) {
            predicates.add(cb.equal(resourceSetJoin.get(Resource_.name), sourceName));
            countPredicates.add(cb.equal(countResourceSetJoin.get(Resource_.name), sourceName));
        }

        countQuery
                .where(countPredicates.toArray(new Predicate[0]))
                .select(cb.count(countRoot))
                .groupBy(countRoot.get(User_.id))
                .distinct(true);
        final Long count = entityManager.createQuery(countQuery)
                .getSingleResult();
        criteriaQuery.where(predicates.toArray(new Predicate[0]))
                .distinct(false);
        final TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .setHint(QueryHints.HINT_LOADGRAPH, entityGraph);
        final List<User> users = typedQuery.getResultList();
        return new PageImpl<>(users, pageRequest, count);
    }


    @Transactional
    public List<User> byQueryDsl(String username, String roleName, String sourceName) {
        final QUser qUser = QUser.user;
        final QMenu qMenu = QMenu.menu;
        final QResource qResource = QResource.resource;
        final ArrayList<com.querydsl.core.types.Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasLength(username)) {
            predicates.add(qUser.username.eq(username));
        }
        if (StringUtils.hasLength(roleName)) {
            predicates.add(qMenu.name.eq(roleName));
        }
        if (StringUtils.hasLength(sourceName)) {
            predicates.add(qResource.name.eq(sourceName));
        }
        final JPAQuery<User> query = queryFactory.selectFrom(qUser)
                .leftJoin(qUser.menuList, qMenu)
                .fetchJoin()
                .leftJoin(qUser.resourceList, qResource)
                .fetchJoin()
                .leftJoin(qUser.pUser)
                .fetchJoin()
                .where(predicates.toArray(new com.querydsl.core.types.Predicate[0]))
                .orderBy(qUser.id.desc())
                .distinct();
        return query.fetch();
    }

    @SuppressWarnings("unchecked")
    public Page<User> bySpecification(String username, String roleName, String sourceName,String pName) {
        final PageRequest pageRequest = PageRequest.of(0, 10);
        return userRepository.findAll((Specification<User>) (root, query, cb) -> {

            final SetJoin<User, Menu> menuFetch = (SetJoin<User, Menu>) root.fetch(User_.menuList, JoinType.LEFT);
            final SetJoin<User, Resource> resourceFetch = (SetJoin<User, Resource>) root.fetch(User_.resourceList, JoinType.LEFT);
            root.fetch(User_.pUser, JoinType.LEFT);
            final ArrayList<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasLength(username)) {
                predicates.add(cb.equal(root.get(User_.username), username));
            }
            if (StringUtils.hasLength(roleName)) {
                predicates.add(cb.equal(menuFetch.get(Menu_.name), roleName));
            }
            if (StringUtils.hasLength(sourceName)) {
                predicates.add(cb.equal(resourceFetch.get(Resource_.name), sourceName));
            }
            if (StringUtils.hasLength(pName)) {
                predicates.add(cb.equal(root.get(User_.pUser).get(User_.username), pName));
            }
            query.where(cb.and(predicates.toArray(new Predicate[0])))
                    .distinct(true);
            return query.getRestriction();
        }, pageRequest);
    }

    public List<String> usernames() {
        return session.createQuery("select u.username from User u", String.class)
                .setCacheable(true)
                .getResultList();
    }

}
