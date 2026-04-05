package chain.handler;

import chain.model.HandleResult;
import chain.model.OrderRequest;

// 第一关：参数校验
public class ParamCheckHandler extends OrderHandler {

    @Override
    public String handlerName() { return "ParamCheckHandler"; }

    @Override
    protected HandleResult doHandle(OrderRequest request) {
        if (request.getProduct() == null || request.getProduct().isEmpty()) {
            return HandleResult.reject(handlerName(), "商品名称不能为空");
        }
        if (request.getQuantity() <= 0) {
            return HandleResult.reject(handlerName(), "数量必须大于 0");
        }
        if (request.getUnitPrice() <= 0) {
            return HandleResult.reject(handlerName(), "单价必须大于 0");
        }
        return HandleResult.pass(handlerName(), "参数校验通过");
    }
}
