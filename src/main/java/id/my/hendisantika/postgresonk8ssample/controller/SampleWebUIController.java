package id.my.hendisantika.postgresonk8ssample.controller;

import id.my.hendisantika.postgresonk8ssample.entity.Value;
import id.my.hendisantika.postgresonk8ssample.repository.ValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-postgres-on-k8s-sample
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 08/05/25
 * Time: 06.53
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class SampleWebUIController {

    private final ValueRepository repository;

    @GetMapping
    public ModelAndView showValue() {
        List<Value> values = (List<Value>) repository.findAll();
        return new ModelAndView("index", "value", values.isEmpty() ? null : values.get(0).getValue());
    }

}
