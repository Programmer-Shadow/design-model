package chain.handler;

import chain.model.HandleResult;
import chain.model.OrderRequest;

// 第二关：库存校验（模拟：库存上限 200）
public class StockCheckHandler extends OrderHandler {

    private static final int MAX_STOCK = 200;

    @Override
    public String handlerName() { return "StockCheckHandler"; }

    @Override
    protected HandleResult doHandle(OrderRequest request) {
        if (request.getQuantity() > MAX_STOCK) {
            return HandleResult.reject(handlerName(),
                    "库存不足，当前库存 " + MAX_STOCK + "，下单数量 " + request.getQuantity());
        }
        return HandleResult.pass(handlerName(), "库存充足（剩余 " + (MAX_STOCK - request.getQuantity()) + "）");
    }
}
