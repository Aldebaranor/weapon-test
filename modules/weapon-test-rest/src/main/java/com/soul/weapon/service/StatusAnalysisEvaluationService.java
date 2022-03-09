package com.soul.weapon.service;

import com.soul.weapon.model.StatusAnalysisEvaluation;

import java.util.List;
import java.util.Map;

public interface StatusAnalysisEvaluationService {

    Map<String, List<StatusAnalysisEvaluation>> getStatusAnglysisAll();
}
