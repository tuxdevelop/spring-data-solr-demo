package org.tuxdevelop.spring.data.solr.demo.model;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.tuxdevelop.spring.data.solr.demo.domain.StarbucksStore;

@Data
public class CityGroupModel {

    private Integer currentPage;
    private Page<GroupEntry<StarbucksStore>> groupEntries;

    public Integer getTotalPages() {
        return getGroupEntries().getTotalPages();
    }

    public void increaseCurrentPage() {
        if (hasNext()) {
            currentPage++;
        }
    }

    public void decreaseCurrentPage() {
        if (hasPrev()) {
            currentPage--;
        }
    }

    public Boolean hasNext() {
        return currentPage < getTotalPages();
    }

    public Boolean hasPrev() {
        return currentPage > 0;
    }

    public Boolean isNotEmpty() {
        return groupEntries != null && groupEntries.getContent().size() > 0;
    }
}
