package id.my.hendisantika.postgresonk8ssample.repository;

import id.my.hendisantika.postgresonk8ssample.entity.Value;
import org.springframework.data.repository.CrudRepository;

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
public interface ValueRepository extends CrudRepository<Value, Long> {
    List<Value> findByValue(String value);
}
