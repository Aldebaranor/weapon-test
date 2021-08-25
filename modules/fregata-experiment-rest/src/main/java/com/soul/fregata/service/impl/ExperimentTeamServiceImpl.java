package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.entity.Person;
import com.egova.entity.codes.DictionaryIntro;
import com.egova.exception.ExceptionUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.ExperimentTeamCondition;
import com.soul.fregata.domain.ExperimentRepository;
import com.soul.fregata.domain.ExperimentTeamRepository;
import com.soul.fregata.entity.Experiment;
import com.soul.fregata.entity.ExperimentTeam;
import com.soul.fregata.service.ExperimentTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class ExperimentTeamServiceImpl extends TemplateService<ExperimentTeam, String> implements ExperimentTeamService {

    private final ExperimentTeamRepository experimentTeamRepository;
    private final ExperimentRepository experimentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insert(ExperimentTeam experimentTeam) {
        experimentTeam.setCreator(UserContext.username());
        experimentTeam.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(experimentTeam);
    }

    @Override
    protected AbstractRepositoryBase<ExperimentTeam, String> getRepository() {
        return experimentTeamRepository;
    }

    @Override
    public PageResult<ExperimentTeam> page(QueryModel<ExperimentTeamCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ExperimentTeam> list(ExperimentTeamCondition condition) {
        //降序 等级高的数值大
        return seek(condition).stream().sorted(Comparator.comparing(ExperimentTeam::getVisitorType).reversed()).collect(Collectors.toList());
    }

    @Override
    public boolean checkAuthorization(String experimentId) {
        Experiment experiment = Optional.ofNullable(experimentRepository.getById(experimentId))
                .orElseThrow(() -> ExceptionUtils.api("试验不存在"));
        // 试验创建者
        if (UserContext.userId().equals(experiment.getCreator())) {
            return true;
        }

        String personId = UserContext.personId();
        if (StringUtils.isBlank(personId)) {
            throw ExceptionUtils.api("当前账号未绑定用户");
        }
        List<ExperimentTeam> teams = experimentTeamRepository.query(SingleClause.equal("personId", personId));
        if (CollectionUtils.isEmpty(teams)) {
            return false;
        }
        for (ExperimentTeam team : teams) {
            String text = DictionaryIntro.getItemText(team.getVisitorType());
            if ("项目负责人".equals(text) || "创建者".equals(text)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Person> getByExperimentId(String experimentId) {
        return experimentTeamRepository.getByExperimentId(experimentId);
    }
}
