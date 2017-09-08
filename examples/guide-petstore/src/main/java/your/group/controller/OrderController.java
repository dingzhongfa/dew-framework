package your.group.controller;

import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.controller.CRUController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import your.group.entity.Order;
import your.group.service.OrderService;
import your.group.vo.BuyVO;

@RestController
@RequestMapping("order/")
@Api(description = "订单操作")
public class OrderController implements CRUController<OrderService, Integer, Order> {

    @PostMapping("buy")
    @ApiOperation(value = "获取记录分页列表")
    public Resp<Void> buy(@Validated @RequestBody BuyVO buyVO) {
        return getService().buy(buyVO.getPetId(), buyVO.getCustomerId());
    }

    @GetMapping("{type}/{pageNumber}/{pageSize}")
    @ApiOperation(value = "获取记录分页列表")
    public Resp<Page<Order>> findOrders(@PathVariable String type, @RequestParam int customerId, @PathVariable long pageNumber, @PathVariable int pageSize) {
        return getService().findOrders(customerId, type, pageNumber, pageSize);
    }

}
