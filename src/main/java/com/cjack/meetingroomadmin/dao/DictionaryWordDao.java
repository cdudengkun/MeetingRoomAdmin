package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.DictionaryWordTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by root on 4/20/19.
 */
public interface DictionaryWordDao extends JpaRepository<DictionaryWordTable, Long>, JpaSpecificationExecutor<DictionaryWordTable> {

    DictionaryWordTable getByWord(String word);

    List<DictionaryWordTable> findAllByWordAudioUrlIsNullOrUsaAudioUrlIsNull();

}
