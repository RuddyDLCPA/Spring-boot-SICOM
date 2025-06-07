package com.api.sicom.api.service;

import com.api.sicom.api.model.Article;
import com.api.sicom.api.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Integer id) {
        return articleRepository.findById(id);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Integer id, Article articleDetails) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setDescripcion(articleDetails.getDescripcion());
            existingArticle.setExistencia(articleDetails.getExistencia());
            existingArticle.setBrand(articleDetails.getBrand()); // Asume que Brand ya existe
            existingArticle.setUnit(articleDetails.getUnit());   // Asume que Unit ya existe
            existingArticle.setIsActive(articleDetails.getIsActive());
            return articleRepository.save(existingArticle);
        }
        return null; // O lanzar una excepción
    }

    public boolean deleteArticle(Integer id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}