package chain.handler;

import chain.model.HandleResult;
import chain.model.OrderRequest;

// 第四关：最终审批
public class ApprovalHandler extends OrderHandler {

    @Override
    public String handlerName() { return "ApprovalHandler"; }

    @Override
    protected HandleResult doHandle(OrderRequest request) {
        return HandleResult.pass(handlerName(),
                "订单已批准 — " + request.getProduct()
                        + " × " + request.getQuantity()
                        + "，总计 " + request.totalAmount() + " 元");
    }
}
