版本说明
==

## 1.0.0-beta1

### Feature

1. 多数据源支持，详见说明文档`多数据源支持`章节

   > **IMPORTANT** 原`Dew.ds.xx`接口弃用，改为`Dew.ds().xx`，如需要使用其它数据源请使用`Dew.ds(<DS Name>).xx`

### Improvement

1. 新增`mybatisplus-example`
1. 改善`Swagger`文档支持
1. 新增销毁时间支持：`boolean tryLock(long waitMillSec, long leaseMillSec)`
1. 锁的等待、销毁时间单位由原来的`秒`改成`毫秒`

### Fixed

1. 修正`tryLock`锁（`Redis`实现），锁被其它线程或JVM占用时等待时间的计算错误



