package tacos.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.persistence.OrderRepository;
import tacos.persistence.TacoRepository;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tacos", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
public class TacoController {

    private final TacoRepository tacoRepository;

    private final OrderRepository orderRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Taco> getTacoById(@PathVariable("id") final Long id) {
        final Optional<Taco> taco = tacoRepository.findById(id);

        return taco.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping(params = "recent")
    public Iterable<Taco> recentTacos() {
        final PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody final Taco taco) {
        return tacoRepository.save(taco);
    }

    @PutMapping(path = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TacoOrder putOrder(@PathVariable("orderId") final Long orderId,
                              @RequestBody final TacoOrder tacoOrder) {

        tacoOrder.setId(orderId);
        return orderRepository.save(tacoOrder);
    }

    @PatchMapping(path = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TacoOrder patchOrder(@PathVariable("orderId") final Long orderId,
                                @RequestBody final TacoOrder tacoOrder) {

        final TacoOrder order = orderRepository.findById(orderId).get();

        if (tacoOrder.getDeliveryName() != null) {
            order.setDeliveryName(tacoOrder.getDeliveryName());
        }

        if (tacoOrder.getDeliveryStreet() != null) {
            order.setDeliveryStreet(tacoOrder.getDeliveryStreet());
        }

        if (tacoOrder.getDeliveryCity() != null) {
            order.setDeliveryCity(tacoOrder.getDeliveryCity());
        }

        if (tacoOrder.getDeliveryState() != null) {
            order.setDeliveryState(tacoOrder.getDeliveryState());
        }

        if (tacoOrder.getDeliveryZip() != null) {
            order.setDeliveryZip(tacoOrder.getDeliveryZip());
        }

        if (tacoOrder.getCcNumber() != null) {
            order.setCcNumber(tacoOrder.getCcNumber());
        }

        if (tacoOrder.getCcExpiration() != null) {
            order.setCcExpiration(tacoOrder.getCcExpiration());
        }

        if (tacoOrder.getCcCVV() != null) {
            order.setCcCVV(tacoOrder.getCcCVV());
        }

        return orderRepository.save(tacoOrder);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") final Long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException e) {

        }
    }
}
