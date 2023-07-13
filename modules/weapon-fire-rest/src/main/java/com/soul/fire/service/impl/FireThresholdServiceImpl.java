package com.soul.fire.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.redis.RedisUtils;
import com.flagwind.persistent.model.*;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.domain.FireThresholdRepository;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.entity.FireWeapon;
import com.soul.fire.service.FireThresholdService;
import com.soul.fire.service.FireWeaponService;
import com.soul.weapon.config.CommonConfig;
import com.soul.weapon.config.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
@CacheConfig(cacheNames = FireThreshold.NAME)
public class FireThresholdServiceImpl extends TemplateService<FireThreshold, String> implements FireThresholdService {

    private final FireThresholdRepository fireThresholdRepository;
    private final CommonConfig config;

    @Autowired
    private FireWeaponService fireWeaponService;


    @Override
    protected AbstractRepositoryBase<FireThreshold, String> getRepository() {
        return fireThresholdRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(FireThreshold fireThreshold) {
        fireThreshold.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(fireThreshold);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FireThreshold fireThreshold) {
//        FireThreshold oldFir = fireThresholdRepository.getById(fireThreshold.getId());
//        if(oldFir != null ){
//            fireThreshold.setCreateTime(oldFir.getCreateTime());
//        }
        RedisUtils.getService(config.getPumpDataBase()).delete(Constant.CHARGE_KEY);
        RedisUtils.getService(config.getPumpDataBase()).delete(Constant.CHARGEDETAIL_KEY);
        RedisUtils.getService(config.getPumpDataBase()).delete(Constant.PREDICT_KEY);
        RedisUtils.getService(config.getPumpDataBase()).delete(Constant.PREDICTDETAIL_KEY);
        fireThreshold.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(fireThreshold);
        upDate(fireThreshold.getId(),fireThreshold.getThresholdValue());
    }

    private void upDate(String Id,String data){
        //更新阈值之后，如果更新的是武器的位置信息要同步到武器表
        String[] datas = data.split("=");
        float[] pos = new float[datas.length-1];
        switch (Id){
            //11:舷外干扰,6座
            case "11":
                //解析字符串,逐个更新武器表
                for(int i=1;i<datas.length;i++){
                    //取值
                    String value = "";
                    for(int j=0;j<datas[i].length();j++){
                        if((datas[i].charAt(j)>=48&&datas[i].charAt(j)<=57)||datas[i].charAt(j)==45){
                            value+=datas[i].charAt(j);
                        }else{
                            break;
                        }
                    }
                    pos[i-1]=Float.valueOf(value);
                }
                for(int i=0;i<pos.length/2;i++){
                    FireWeapon fireWeapon = new FireWeapon();
                    fireWeapon=fireWeaponService.getById("3"+String.valueOf(i));
                    fireWeapon.setX(pos[2*i]);
                    fireWeapon.setY(pos[2*i+1]);
                    fireWeaponService.update(fireWeapon);
                }
                break;
            case "4":
                //解析字符串,逐个更新武器表
                for(int i=1;i<datas.length;i++){
                    //取值
                    String value = "";
                    for(int j=0;j<datas[i].length();j++){
                        if((datas[i].charAt(j)>=48&&datas[i].charAt(j)<=57)||datas[i].charAt(j)==45){
                            value+=datas[i].charAt(j);
                        }else{
                            break;
                        }
                    }
                    pos[i-1]=Float.valueOf(value);
                }
                for(int i=0;i<pos.length/2;i++){
                    FireWeapon fireWeapon = new FireWeapon();
                    fireWeapon=fireWeaponService.getById("4"+String.valueOf(i));
                    fireWeapon.setX(pos[2*i]);
                    fireWeapon.setY(pos[2*i+1]);
                    fireWeaponService.update(fireWeapon);
                }
                break;
            case "10":
                //解析字符串,逐个更新武器表
                for(int i=1;i<datas.length;i++){
                    //取值
                    String value = "";
                    for(int j=0;j<datas[i].length();j++){
                        if((datas[i].charAt(j)>=48&&datas[i].charAt(j)<=57)||datas[i].charAt(j)==45){
                            value+=datas[i].charAt(j);
                        }else{
                            break;
                        }
                    }
                    pos[i-1]=Float.valueOf(value);
                }
                for(int i=0;i<pos.length/2;i++){
                    FireWeapon fireWeapon = new FireWeapon();
                    fireWeapon=fireWeaponService.getById("5"+String.valueOf(i));
                    fireWeapon.setX(pos[2*i]);
                    fireWeapon.setY(pos[2*i+1]);
                    fireWeaponService.update(fireWeapon);
                }
                break;
            default:
                break;
        }

    }



    @Override
    public PageResult<FireThreshold> page(QueryModel<FireThresholdCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    /**
     * 列表查询全部
     *
     * @return 阈值表
     */
    @Override
    public List<FireThreshold> getAll() {
        FireThresholdCondition fireThresholdCondition = new FireThresholdCondition();
        fireThresholdCondition.setDisabled(false);
        return super.query(fireThresholdCondition);
    }

    @Override
    public List<FireThreshold> list(FireThresholdCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));
    }

}
