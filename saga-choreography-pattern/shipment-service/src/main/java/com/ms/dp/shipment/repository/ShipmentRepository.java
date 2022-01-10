package com.ms.dp.shipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.dp.shipment.entity.ShipmentDetails;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentDetails, Integer> {

}
