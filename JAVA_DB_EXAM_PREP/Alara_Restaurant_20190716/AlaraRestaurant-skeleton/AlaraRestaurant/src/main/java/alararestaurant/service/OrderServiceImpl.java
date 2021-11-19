package alararestaurant.service;

import alararestaurant.domain.dtos.seed.xml.OrderItemsSeedDto;
import alararestaurant.domain.dtos.seed.xml.OrderSeedDto;
import alararestaurant.domain.dtos.seed.xml.OrderSeedRootDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Item;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.files.FilePaths;
import alararestaurant.util.files.FileUtil;
import alararestaurant.util.validator.ValidationUtil;
import alararestaurant.util.xmlParser.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemRepository;
    private final FileUtil fileUtil;
    private final XMLParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository,
                            EmployeeRepository employeeRepository, OrderItemRepository orderItemRepository, FileUtil fileUtil, XMLParser xmlParser,
                            ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.orderItemRepository = orderItemRepository;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public Boolean ordersAreImported() {
        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {
        return this.fileUtil.readFile(FilePaths.ORDERS_PATH);
    }

    @Override
    public String importOrders() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        OrderSeedRootDto orderSeedRootDto = this.xmlParser.parseXml(FilePaths.ORDERS_PATH, OrderSeedRootDto.class);
        orderLoop://!!!naming the whole loop
        for (OrderSeedDto orderSeedDto : orderSeedRootDto.getOrders()) {
            Order order = this.modelMapper.map(orderSeedDto, Order.class);
            Employee employee = this.employeeRepository.findByName(orderSeedDto.getEmployee());
            if (!this.validationUtil.isValid(orderSeedDto) || employee == null) {
                sb
                        .append("Invalid order")
                        .append(System.lineSeparator());
                continue;
            }

            order.setEmployee(employee);
            Set<OrderItem> orderItems = new HashSet<>();

            for (OrderItemsSeedDto orderItemsSeedDto : orderSeedDto.getOrderItems().getItems()) {
                Item item = this.itemRepository.findByName(orderItemsSeedDto.getName());
                if (item == null) {
                    continue orderLoop;
                }
                OrderItem orderItem = new OrderItem(orderItemsSeedDto.getQuantity(), item, order);
                orderItems.add(orderItem);
                this.orderItemRepository.save(orderItem);
            }
            order.setOrderItems(orderItems);
            this.orderRepository.save(order);
            sb
                    .append(getSuccessMessage(order))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        StringBuilder sb = new StringBuilder();
        List<Order> orders = this.orderRepository.findAllByEmployee_Position_Name("Burger Flipper");

        for (Order order : orders) {
            sb
                    .append(String.format("Name: %s", order.getEmployee().getName()))
                    .append(System.lineSeparator())
                    .append("Orders:")
                    .append(System.lineSeparator())
                    .append(String.format("   Customer: %s", order.getCustomer()))
                    .append(System.lineSeparator())
                    .append("   Items:")
                    .append(System.lineSeparator());
            Set<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Item item = orderItem.getItem();
                sb
                        .append(String.format("      Name: %s%n      Price: %.2f%n      Quantity: %d",
                                item.getName(), item.getPrice(), orderItem.getQuantity()))
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    private String getSuccessMessage(Order order) {
        return String.format("Order for %s on %s added",
                order.getCustomer(),
                order.getDateTime());
    }
}
