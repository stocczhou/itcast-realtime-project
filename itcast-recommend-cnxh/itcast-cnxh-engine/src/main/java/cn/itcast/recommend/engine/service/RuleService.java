package cn.itcast.recommend.engine.service;

import cn.itcast.recommend.engine.domain.Template;

public interface RuleService {
    Template getTemplateByAdId(String adId);

    boolean isExist(String adId);
}
