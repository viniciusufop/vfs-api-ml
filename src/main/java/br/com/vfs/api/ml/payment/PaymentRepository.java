package br.com.vfs.api.ml.payment;

import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
   boolean existsByCodeAndStatus(final String code, final PaymentStatus status);
}
