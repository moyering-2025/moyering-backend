package com.dev.moyering.host.repository;

import java.util.List;

import com.dev.moyering.host.entity.HostClass;
import com.dev.moyering.user.entity.User;

public interface HostClassRepositoryCustom {
    List<HostClass> findRecommendClassesForUser(User user) throws Exception;
}
