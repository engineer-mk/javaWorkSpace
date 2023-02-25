package xmg.example.springdatastart.base;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 普通条件查询示例：username_equal=wang
 * many to one  关联查询示例：pUser.username_equal=wang
 * many to many 关联查询示例 user.menuList.name_equal=wang
 */
@Log4j2
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 4936607744752177043L;
    public static final String GREATER_THAN = "_gt";
    public static final String LESS_THAN = "_lt";
    public static final String LIKE = "_like";
    public static final String EQUAL = "_equal";
    public static final String CONTAIN = "_contain";
    public static final String END = "_end";
    public static final String START = "_start";
    public static final String DESC = "_desc";
    public static final String ASC = "_asc";
    public static final String FETCH = "_fetch";
    public static final String IS_NULL = "_isNull";
    public static final String NOT_NULL = "_notNull";

    public static final String PAGE = "page";
    public static final String SIZE = "size";
    private static final List<String> SUFFIX_LIST = new ArrayList<>();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        SUFFIX_LIST.add(GREATER_THAN);
        SUFFIX_LIST.add(LESS_THAN);
        SUFFIX_LIST.add(LIKE);
        SUFFIX_LIST.add(EQUAL);
        SUFFIX_LIST.add(CONTAIN);
        SUFFIX_LIST.add(END);
        SUFFIX_LIST.add(START);
        SUFFIX_LIST.add(DESC);
        SUFFIX_LIST.add(ASC);
        SUFFIX_LIST.add(FETCH);
        SUFFIX_LIST.add(IS_NULL);
        SUFFIX_LIST.add(NOT_NULL);
    }

    private Integer page;
    private Integer size;
    private final Map<String, Object> conditions;

    private BaseQuery() {
        this.conditions = new HashMap<>();
    }

    public static BaseQuery of() {
        return new BaseQuery();
    }

    public static BaseQuery of(HttpServletRequest request) {
        return new BaseQuery(request);
    }

    private BaseQuery(HttpServletRequest request) {
        this.conditions = new HashMap<>();
        this.conditions.putAll(request.getParameterMap());
        setPageParam();
    }


    private void setPageParam() {
        if (this.conditions.containsKey(PAGE)) {
            final Object obj = this.conditions.get(PAGE);
            this.page = resolveValue(obj, Integer.class);
        }
        if (this.conditions.containsKey(SIZE)) {
            final Object obj = this.conditions.get(SIZE);
            this.size = Integer.min(resolveValue(obj, Integer.class), 100);
        }
    }

    public <T> T resolveValue(Object obj, Class<T> type) {
        try {
            if (obj instanceof String[]) {
                return TypeUtils.cast(((String[]) obj)[0], type, ParserConfig.getGlobalInstance());
            }
            return TypeUtils.cast(obj, type, ParserConfig.getGlobalInstance());
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            throw new RuntimeException("参数解析错误:" + e.getLocalizedMessage());
        }
    }

    public void put(String field, String suffix, Object obj) {
        this.conditions.put(field + suffix, obj);
    }

    public void put(String field, String suffix) {
        this.conditions.put(field + suffix, null);
    }


    public PageRequest getPageable() {
        int page = this.page == null ? 0 : Math.max(this.page - 1, 0);
        int size = this.size == null ? 10 : this.size;
        return PageRequest.of(page, size);
    }


    public List<Method> getFetchFields(Class<?> entityType) {
        List<Method> results = new ArrayList<>();
        if (entityType == null) {
            return results;
        }
        for (String key : conditions.keySet()) {
            if (key.endsWith(FETCH)) {
                final String methodName = getMethodName(key.replaceAll(FETCH, ""));
                try {
                    results.add(entityType.getMethod(methodName));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }


    public <T> Specification<T> getSpecification(Class<T> tClass, boolean selectPage) {
        return (root, query, cb) -> {
            final boolean isSelectCount = query.getResultType().isAssignableFrom(Long.class);
            List<Predicate> predicates = new ArrayList<>();
            List<Order> orders = new ArrayList<>();
            Set<String> fetchFields = new HashSet<>();
            final List<String> queryParams = new ArrayList<>(conditions.keySet());
            //FETCH 排到最前边
            queryParams.sort((s1, s2) -> (FETCH.equals(getSuffix(s2)) ? 1 : 0) - (FETCH.equals(getSuffix(s1)) ? 1 : 0));
            for (String key : queryParams) {
                final String suffix = getSuffix(key);
                String fieldName = removeSuffix(key);

                if (!SUFFIX_LIST.contains(suffix)) {
                    continue;
                }

                String childFieldName = null;
                if (fieldName.contains(".")) {
                    final String[] split = fieldName.split("\\.");
                    if (split.length < 2) {
                        throw new RuntimeException("参数格式错误");
                    }
                    fieldName = split[0];
                    childFieldName = split[1];
                }

                Object value = conditions.getOrDefault(key, null);
                Class<?> fieldType;
                //分页查询 禁止多对多直接fetch 使用n+1方式
                boolean disableFetch;
                try {
                    final Field field = tClass.getDeclaredField(fieldName);
                    fieldType = field.getType();
                    final boolean isCollection = Collection.class.isAssignableFrom(fieldType);
                    disableFetch = selectPage && isCollection;
                    if (childFieldName != null) {
                        if (isCollection) {
                            //集合则获取范型类型
                            final Type childFieldType = ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments()[0];
                            //获取范型类型字段childFieldName 的类型
                            fieldType = ((Class<?>) childFieldType).getDeclaredField(childFieldName).getType();
                        } else {
                            //非集合直接获取 该类型的字段childFieldName 的类型
                            fieldType = field.getType().getDeclaredField(childFieldName).getType();
                        }
                    }
                } catch (NoSuchFieldException e) {
                    log.warn("field not exist: " + e.getLocalizedMessage());
                    continue;
                }
                Path path = root.get(fieldName);
                if (childFieldName != null) {
                    boolean useFetch = !isSelectCount && conditions.containsKey(fieldName + FETCH) && !disableFetch;
                    final Join<Object, Object> join = !useFetch ? root.join(fieldName, JoinType.LEFT) : (Join<Object, Object>) root.fetch(fieldName, JoinType.LEFT);
                    if (useFetch) {
                        fetchFields.remove(fieldName);
                    }
                    path = join.get(childFieldName);
                }
                switch (suffix) {
                    case GREATER_THAN:
                        value = resolveValue(value, fieldType);
                        predicates.add(cb.greaterThan(path, (Comparable) value));
                        break;
                    case LESS_THAN:
                        value = resolveValue(value, fieldType);
                        predicates.add(cb.lessThan(path, (Comparable) value));
                        break;
                    case LIKE:
                        if (fieldType == String.class) {
                            value = resolveValue(value, fieldType);
                            predicates.add(cb.like(path, (String) value));
                        }
                        break;
                    case CONTAIN:
                        if (fieldType == String.class) {
                            value = resolveValue(value, fieldType);
                            predicates.add(cb.like(path, "%" + value + "%"));
                        }
                        break;
                    case START:
                        if (fieldType == String.class) {
                            value = resolveValue(value, fieldType);
                            predicates.add(cb.like(path, value + "%"));
                        }
                        break;
                    case END:
                        if (fieldType == String.class) {
                            value = resolveValue(value, fieldType);
                            predicates.add(cb.like(path, "%" + value));
                        }
                        break;
                    case IS_NULL:
                        predicates.add(cb.isNull(path));
                        break;
                    case NOT_NULL:
                        predicates.add(cb.isNotNull(path));
                        break;
                    case DESC:
                        orders.add(cb.desc(path));
                        break;
                    case ASC:
                        orders.add(cb.asc(path));
                        break;
                    case FETCH:
                        if (!disableFetch) {
                            fetchFields.add(fieldName);
                        }
                        break;
                    case EQUAL:
                        value = resolveValue(value, fieldType);
                        predicates.add(cb.equal(path, value));
                }

            }
            query.where(cb.and(predicates.toArray(new Predicate[0])))
                    .distinct(true)
                    .orderBy(orders);
            if (!isSelectCount) {
                for (String field : fetchFields) {
                    root.fetch(field, JoinType.LEFT);
                }
            }
            return query.getRestriction();
        };
    }


    private String getSuffix(String fieldName) {
        final String[] s = fieldName.split("_");
        if (s.length > 1) {
            return "_" + s[1];
        }
        return "";
    }

    private String removeSuffix(String fieldName) {
        for (String s : SUFFIX_LIST) {
            fieldName = fieldName.replace(s, "");
        }
        return fieldName;
    }

    private String getMethodName(String fieldName) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        return "get" + firstLetter + fieldName.substring(1);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        for (final String s : set) {
            if (s.equals("b")) {
                set.remove("d");
            }
            System.out.println(s);
        }
    }
}
