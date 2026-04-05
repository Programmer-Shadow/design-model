package chain.handler;

import chain.model.HandleResult;
import chain.model.OrderRequest;

import java.util.List;

/**
 * 责任链模式 — 抽象处理器
 *
 * 每个处理器持有下一个处理器的引用（next），形成链条。
 * handle() 是模板：先执行自身逻辑，通过则交给下一个，拒绝则终止。
 * doHandle() 是子类实现的具体校验逻辑。
 */
public abstract class OrderHandler {

    private OrderHandler next;

    // 链式设置，支持 a.setNext(b).setNext(c)
    public OrderHandler setNext(OrderHandler next) {
        this.next = next;
        return next;
    }

    // 执行处理，trace 记录每个节点的结果
    public final HandleResult handle(OrderRequest request, List<HandleResult> trace) {
        HandleResult result = doHandle(request);
        trace.add(result);

        // 通过且有下一个处理器 → 传递
        if (result.isPassed() && next != null) {
            return next.handle(request, trace);
        }
        return result;
    }

    protected abstract HandleResult doHandle(OrderRequest request);

    public abstract String handlerName();
}
