package com.example.springdatastart.config;

import com.alibaba.fastjson.serializer.PropertyFilter;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 * fastJson过滤器
 * 拦截hibernate懒加载属性的序列化
 * 返回false属性将被忽略，true属性将被保留
 */
public class MyPropertyFilter implements PropertyFilter {
    private static boolean hasHibernateProxyClass;

    static {
        try {
            Class.forName("org.hibernate.proxy.HibernateProxy");
            hasHibernateProxyClass = true;
        } catch (ClassNotFoundException e) {
            hasHibernateProxyClass = false;
        }
    }

    @Override
    public boolean apply(Object object, String name, Object value) {
        if (!hasHibernateProxyClass) {
            return true;
        }
        if (value instanceof HibernateProxy) {//hibernate代理对象
            LazyInitializer initializer = ((HibernateProxy) value).getHibernateLazyInitializer();
            if (initializer.isUninitialized()) {
                return false;
            }
        } else if (value instanceof PersistentCollection) {//实体关联集合一对多等
            PersistentCollection collection = (PersistentCollection) value;
            if (!collection.wasInitialized()) {
                return false;
            }
            Object val = collection.getValue();
            if (val == null) {
                return false;
            }
        }
        return true;
    }
}
