package com.app.repository;

import com.app.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompanyRepository extends JpaRepository<Company, Long> {

//    Company getCompanyIdByLoggedInUser(String loggedInUserId);
}
