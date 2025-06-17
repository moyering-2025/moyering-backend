package com.dev.moyering.host.repository;

import java.util.List;

import com.dev.moyering.common.entity.User;
import com.dev.moyering.host.entity.HostClass;

public interface HostClassRepositoryCustom {
    List<HostClass> findRecommendClassesForUser(User user) throws Exception;
}
