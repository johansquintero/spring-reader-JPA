package com.reader.SpringReader.persistence.entites.repositories;

import com.reader.SpringReader.persistence.entites.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerCrudRepository extends JpaRepository<CustomerEntity,Long> {

}
