package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.*;

/**
 * @author makui
 * @created 2024/6/24
 **/
@Slf4j
public class AppendedFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.info("AppendedFilter#invoke before {}", invoker.getUrl());
        Result result = invoker.invoke(invocation);
        log.info("AppendedFilter#invoke after {}", invoker.getUrl());
        return result;
    }
}
