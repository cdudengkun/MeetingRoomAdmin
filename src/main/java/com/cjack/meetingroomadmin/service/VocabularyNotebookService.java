package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.VocabularyNotebookDao;
import com.cjack.meetingroomadmin.model.VocabularyNotebookModel;
import com.cjack.meetingroomadmin.table.VocabularyNotebookTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class VocabularyNotebookService {

    @Autowired
    private VocabularyNotebookDao dao;

    public List<VocabularyNotebookModel> list(VocabularyNotebookModel model){
        return ModelUtils.copyListModel( dao.findAll(), VocabularyNotebookModel.class);
    }

    public void del( String ids){
        List<VocabularyNotebookTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            VocabularyNotebookTable table = new VocabularyNotebookTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( VocabularyNotebookModel model){
        dao.save( ModelUtils.copySignModel( model, VocabularyNotebookTable.class));
    }

    public VocabularyNotebookModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), VocabularyNotebookModel.class);
    }
}
