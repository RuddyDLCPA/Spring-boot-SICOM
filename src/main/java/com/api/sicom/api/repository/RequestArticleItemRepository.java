package com.api.sicom.api.repository;

import com.api.sicom.api.model.RequestArticleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestArticleItemRepository extends JpaRepository<RequestArticleItem, Integer> {
}