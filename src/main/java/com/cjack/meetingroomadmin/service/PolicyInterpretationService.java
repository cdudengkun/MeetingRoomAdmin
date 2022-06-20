package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.EnterpriseServiceTypeDao;
import com.cjack.meetingroomadmin.dao.PolicyInterpretationDao;
import com.cjack.meetingroomadmin.dao.PolicyInterpretationFileDao;
import com.cjack.meetingroomadmin.dao.PolicyInterpretationVedioDao;
import com.cjack.meetingroomadmin.model.PolicyInterpretationFileModel;
import com.cjack.meetingroomadmin.model.PolicyInterpretationModel;
import com.cjack.meetingroomadmin.model.PolicyInterpretationVideoModel;
import com.cjack.meetingroomadmin.table.EnterpriseServiceTypeTable;
import com.cjack.meetingroomadmin.table.PolicyInterpretationFileTable;
import com.cjack.meetingroomadmin.table.PolicyInterpretationTable;
import com.cjack.meetingroomadmin.table.PolicyInterpretationVideoTable;
import com.cjack.meetingroomadmin.util.CustomerStringUtil;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyInterpretationService {

    @Autowired
    private PolicyInterpretationDao dao;
    @Autowired
    private PolicyInterpretationFileDao fileDao;
    @Autowired
    private PolicyInterpretationVedioDao vedioFileDao;
    @Autowired
    private EnterpriseServiceTypeDao typeDao;

    public void list( LayPage layPage, PolicyInterpretationModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<PolicyInterpretationTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<PolicyInterpretationTable> pageTable = dao.findAll( specification, pageable);
        List<PolicyInterpretationModel> datas = new ArrayList<>();
        for( PolicyInterpretationTable table : pageTable.getContent()){
            PolicyInterpretationModel data = ModelUtils.copySignModel( table, PolicyInterpretationModel.class);
            data.setFiles( ModelUtils.copyListModel( table.getFiles(), PolicyInterpretationFileModel.class));
            data.setVideos( ModelUtils.copyListModel( table.getVideos(), PolicyInterpretationVideoModel.class));
            data.setTypeId( table.getEnterpriseServiceTypeTable().getId());
            data.setTypeName( table.getEnterpriseServiceTypeTable().getName());
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    //将关联了这个type的数据的type设置为null
    public void delType( Long typeId){
        dao.updateType( typeId);
    }

    public void del( String ids){
        List<PolicyInterpretationTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            PolicyInterpretationTable table = dao.getOne( Long.valueOf( id));
            tables.add( table);
            vedioFileDao.delete( table.getVideos());
            fileDao.delete( table.getFiles());
        }
        dao.deleteInBatch( tables);
    }

    public void uploadImg( PolicyInterpretationModel model){
        PolicyInterpretationTable table = dao.findOne( model.getId());
        if( EmptyUtil.isNotEmpty( table.getImgs())){
            if( !table.getImgs().contains( model.getImg())){
                table.setImgs( table.getImgs() + "," + model.getImg());
            }
        }else{
            table.setImgs( model.getImg());
        }

        dao.save( table);
    }


    public void delImg( PolicyInterpretationModel model){
        PolicyInterpretationTable table = dao.findOne( model.getId());
        if( EmptyUtil.isNotEmpty( table.getImgs()) && EmptyUtil.isNotEmpty( model.getImgs())){
            for( String img : model.getImgs().split( ",")){
                table.setImgs( table.getImgs().replace( img + ",", ""));
            }
        }
        dao.save( table);
    }

    public void videoList( LayPage layPage, PolicyInterpretationVideoModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));
        Specification<PolicyInterpretationVideoTable> specification = handleConditon( model);

        Page<PolicyInterpretationVideoTable> pageTable = vedioFileDao.findAll( specification, pageable);
        List<PolicyInterpretationVideoModel> datas = new ArrayList<>();
        for( PolicyInterpretationVideoTable table : pageTable.getContent()){
            PolicyInterpretationVideoModel data = ModelUtils.copySignModel( table, PolicyInterpretationVideoModel.class);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void uploadVideoFile( PolicyInterpretationVideoModel model){
        PolicyInterpretationVideoTable table = ModelUtils.copySignModel( model, PolicyInterpretationVideoTable.class);
        table.setPolicyInterpretation( dao.getOne( model.getPolicyInterpretationId()));
        table.setCreateTime( System.currentTimeMillis());
        vedioFileDao.save( table);
    }

    public void delVideoFile( PolicyInterpretationVideoModel model){
        vedioFileDao.delete( model.getId());
    }

    public List<PolicyInterpretationFileModel> fileList( PolicyInterpretationFileModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<PolicyInterpretationFileTable> specification = handleConditon( model);

        List<PolicyInterpretationFileTable> tables = fileDao.findAll( specification);
        List<PolicyInterpretationFileModel> datas = new ArrayList<>();
        for( PolicyInterpretationFileTable table : tables){
            PolicyInterpretationFileModel data = ModelUtils.copySignModel( table, PolicyInterpretationFileModel.class);
            datas.add( data);
        }
        return datas;
    }

    public void uploadAttachment( PolicyInterpretationFileModel model){
        PolicyInterpretationFileTable table = ModelUtils.copySignModel( model, PolicyInterpretationFileTable.class);
        table.setPolicyInterpretation( dao.getOne( model.getPolicyInterpretationId()));
        table.setCreateTime( System.currentTimeMillis());
        fileDao.save( table);
    }

    public void delAttachment( PolicyInterpretationFileModel model){
        fileDao.delete( model.getId());
    }

    public void save( PolicyInterpretationModel model){
        PolicyInterpretationTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, PolicyInterpretationTable.class);
        }
        table.setEnterpriseServiceTypeTable( typeDao.getOne( model.getTypeId()));
        dao.save( table);
    }

    private Specification<PolicyInterpretationTable> handleConditon( PolicyInterpretationModel model){
        Specification< PolicyInterpretationTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTitle())){
                predicate.getExpressions().add( cb.like( root.get("title"), CustomerStringUtil.toLikeStr( model.getTitle())));
            }
            if( EmptyUtil.isNotEmpty( model.getType())){
                predicate.getExpressions().add( cb.equal( root.get("type"), model.getType()));
            }
            if( EmptyUtil.isNotEmpty( model.getTypeId())){
                Join<PolicyInterpretationTable, EnterpriseServiceTypeTable> join = root.join("enterpriseServiceTypeTable", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getTypeId()));
            }
            return predicate;
        };
        return specification;
    }

    private Specification<PolicyInterpretationVideoTable> handleConditon( PolicyInterpretationVideoModel model){
        Specification< PolicyInterpretationVideoTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTitle())){
                predicate.getExpressions().add( cb.equal( root.get("title"), CustomerStringUtil.toLikeStr( model.getTitle())));
            }
            Join<PolicyInterpretationVideoTable, PolicyInterpretationTable> join = root.join("policyInterpretation", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( join.get("id"), model.getPolicyInterpretationId()));
            return predicate;
        };
        return specification;
    }

    private Specification<PolicyInterpretationFileTable> handleConditon( PolicyInterpretationFileModel model){
        Specification< PolicyInterpretationFileTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.equal( root.get("name"), CustomerStringUtil.toLikeStr( model.getName())));
            }
            return predicate;
        };
        return specification;
    }
}
