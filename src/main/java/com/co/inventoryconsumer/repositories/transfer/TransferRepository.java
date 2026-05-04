package com.co.inventoryconsumer.repositories.transfer;

import com.co.inventoryconsumer.domain.transfer.TransferStatus;
import com.co.inventoryconsumer.domain.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByEstado(TransferStatus estado);
    List<Transfer> findBySedeDestino(String sedeDestino);
    List<Transfer> findBySedeDestinoAndEstado(String sedeDestino, TransferStatus estado);
}
