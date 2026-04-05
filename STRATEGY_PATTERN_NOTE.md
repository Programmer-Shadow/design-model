# 策略模式学习笔记（基于当前 Demo）

## 1. 这个模式解决什么问题

当你有一组“可互换算法”时（比如多种折扣规则），如果直接写在业务代码里，通常会变成很多 `if-else`，后续扩展和维护都很痛苦。  
策略模式的核心目标是：

- 把“算法实现”独立出来
- 让“算法选择”与“算法执行”解耦
- 新增算法时尽量不改原有业务流程

---

## 2. 本项目中的角色映射

- `DiscountStrategy`：策略接口，定义统一方法 `calculate(Order order)`
- `NormalPriceStrategy` / `VipDiscountStrategy` / `NewUserDiscountStrategy`：具体策略
- `DiscountContext`：上下文，负责按策略名选择策略并执行
- `Main#handleQuote`：客户端请求入口，接收 `strategy` 参数并委托给 `DiscountContext`

一句话理解：  
前端传入策略名 -> 后端上下文选中对应策略 -> 调用统一接口计算结果。

---

## 3. 调用链（你可以对照代码看）

1. 前端点击“计算总价”，请求 `/api/quote?strategy=vip...`
2. `Main#handleQuote` 解析参数，构造 `Order`
3. 调用 `discountContext.calculateTotal(strategy, order)`
4. `DiscountContext` 从 `strategyMap` 中找到对应策略
5. 执行 `strategy.calculate(order)` 返回最终价格

---

## 4. 为什么这就是策略模式

- 统一行为入口：所有策略都实现 `DiscountStrategy`
- 运行时切换算法：由请求参数决定用哪个策略
- 避免条件分支扩散：新增折扣规则不需要改 `handleQuote` 的主流程

---

## 5. 如何新增一个策略（实战模板）

目标：新增“节日 8 折”策略，策略名 `festival`

1. 新建类 `FestivalDiscountStrategy implements DiscountStrategy`
2. 实现 `calculate(Order order)`，返回 `order.getOriginTotal() * 0.8`
3. 在 `DiscountContext` 构造方法中注册：
   `strategyMap.put("festival", new FestivalDiscountStrategy());`
4. 前端下拉框增加一个选项：
   `<option value='festival'>节日 8 折</option>`
5. 重启服务后测试请求：
   `/api/quote?unitPrice=100&quantity=2&strategy=festival`

---

## 6. 适用场景与注意点

适用场景：

- 同一业务动作有多种可替换实现
- 规则会持续增长（比如营销活动、支付渠道、排序规则）

注意点：

- 策略类会增多，这是正常成本
- 上下文要控制好默认策略和异常策略名
- 若策略有共享配置，可通过构造注入而不是写死在类里

---

## 7. 你可以继续练习的方向

- 把策略注册从硬编码改为配置化（比如读取配置文件）
- 增加“满减 + 折扣”组合策略（引出装饰器或规则链）
- 给每个策略补单元测试，验证边界值（0 元、负数输入、大数量）
