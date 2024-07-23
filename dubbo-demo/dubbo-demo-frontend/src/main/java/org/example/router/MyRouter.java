package org.example.router;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.config.configcenter.ConfigChangedEvent;
import org.apache.dubbo.common.config.configcenter.ConfigurationListener;
import org.apache.dubbo.common.utils.Holder;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.RouterSnapshotNode;
import org.apache.dubbo.rpc.cluster.router.state.AbstractStateRouter;
import org.apache.dubbo.rpc.cluster.router.state.BitList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author makui
 * @created 2024/7/18
 **/
@Slf4j
public class MyRouter<T> extends AbstractStateRouter<T> implements ConfigurationListener {

    public MyRouter(URL url) {
        super(url);
    }

    @Override
    protected BitList<Invoker<T>> doRoute(BitList<Invoker<T>> invokers, URL url, Invocation invocation,
                                          boolean needToPrintMessage,
                                          Holder<RouterSnapshotNode<T>> routerSnapshotNodeHolder,
                                          Holder<String> messageHolder) throws RpcException {
        final String s = messageHolder.get();
        final RouterSnapshotNode<T> node = routerSnapshotNodeHolder.get();
        final Invoker<?> invoker = invocation.getInvoker();
        final BitList<Invoker<T>> copyInvokers = invokers.clone();
        return null;
    }


    @Override
    public void process(ConfigChangedEvent event) {
        log.info("MyRouter#process {}", event);
    }
}
