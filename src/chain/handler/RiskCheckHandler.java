package chain.handler;

import chain.model.HandleResult;
import chain.model.OrderRequest;

// 第三关：风控校验（模拟：单笔金额上限 50000）
public class RiskCheckHandler extends OrderHandler {

    private static final double MAX_AMOUNT = 50000;

    @Override
    public String handlerName() { return "RiskCheckHandler"; }

    @Override
    protected HandleResult doHandle(OrderRequest request) {
        double total = request.totalAmount();
        if (total > MAX_AMOUNT) {
            return HandleResult.reject(handlerName(),
                    "触发风控，订单金额 " + total + " 超过上限 " + MAX_AMOUNT);
        }
        return HandleResult.pass(handlerName(), "风控通过（金额 " + total + "，上限 " + MAX_AMOUNT + "）");
    }
}
