package com.org.os.service;

import com.org.os.persistance.entity.Items;
import com.org.os.persistance.repository.ItemsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemsService {

    private ItemsRepository itemsRepository;

    public void saveItem(Items items) {
        itemsRepository.save(items);
    }
}
