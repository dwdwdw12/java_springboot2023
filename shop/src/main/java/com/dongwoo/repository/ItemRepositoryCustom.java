package com.dongwoo.repository;

import com.dongwoo.dto.ItemSearchDto;
import com.dongwoo.dto.MainItemDto;
import com.dongwoo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
