package com.voyagerss.persist.service;

import com.voyagerss.persist.dto.TranslationsDTO;
import com.voyagerss.persist.entity.Translations;
import com.voyagerss.persist.repository.TranslationsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TranslationsService {

    @Autowired
    private TranslationsRepository translationsRepository;

    public Long save(TranslationsDTO vO) {
        Translations bean = new Translations();
        BeanUtils.copyProperties(vO, bean);
        bean = translationsRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        translationsRepository.deleteById(id);
    }

    public void update(Long id, TranslationsDTO vO) {
        Translations bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        translationsRepository.save(bean);
    }

    public TranslationsDTO getById(Long id) {
        Translations original = requireOne(id);
        return toDTO(original);
    }

    public Page<TranslationsDTO> query(TranslationsDTO vO) {
        throw new UnsupportedOperationException();
    }

    private TranslationsDTO toDTO(Translations original) {
        TranslationsDTO bean = new TranslationsDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private Translations requireOne(Long id) {
        return translationsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
