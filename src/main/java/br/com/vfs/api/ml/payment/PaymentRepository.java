package br.com.vfs.api.ml.payment;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
    Optional<Payment> findByCodeAndPurchase_Id(final String code,final Long idPurchase);
}
