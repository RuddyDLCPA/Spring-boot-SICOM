package com.api.sicom.api.repository;

import com.api.sicom.api.model.RequestArticles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestArticlesRepository extends JpaRepository<RequestArticles, Integer> {
}