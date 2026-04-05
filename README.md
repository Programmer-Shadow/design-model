# Strategy Pattern Fullstack Demo (Java Backend)

这是一个最小可运行的前后端 demo，用来学习策略模式。

- 后端: 纯 Java `HttpServer`
- 前端: 内置 HTML + JS 页面
- 核心: 不同折扣策略通过策略模式解耦

## 目录结构

```text
src/
  Main.java
  FrontendPage.java
  pricing/
    DiscountStrategy.java
    DiscountContext.java
    Order.java
    NormalPriceStrategy.java
    VipDiscountStrategy.java
    NewUserDiscountStrategy.java
```

## 运行方式

### 方式 1: IDEA 里运行

直接运行 `Main.main()`，控制台看到:

```text
Server started at http://localhost:8080
```

然后浏览器访问:

```text
http://localhost:8080
```

### 方式 2: 命令行

```powershell
javac -encoding UTF-8 -d build src\Main.java src\FrontendPage.java src\pricing\*.java
java -cp build Main
```

## 策略模式说明

- `DiscountStrategy`: 策略接口
- `NormalPriceStrategy` / `VipDiscountStrategy` / `NewUserDiscountStrategy`: 具体策略
- `DiscountContext`: 按策略名选择具体策略并执行

新增策略时，只需新增一个实现类并在 `DiscountContext` 注册即可。
