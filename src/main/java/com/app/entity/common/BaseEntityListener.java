package com.app.entity.common;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    public void prePersist(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalDateTime time = LocalDateTime.now(ZoneId.of("UTC"));
        baseEntity.insertDateTime= time;
        baseEntity.lastUpdateDateTime= time;

        if(authentication != null && !authentication.getName().equals("anonymousUser")){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            baseEntity.insertUserId= principal.getId();
            baseEntity.lastUpdateUserId= principal.getId();
        }
    }

    @PreUpdate
    public void preUpdate(BaseEntity baseEntity) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.lastUpdateDateTime = LocalDateTime.now(ZoneId.of("UTC"));

        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            baseEntity.lastUpdateUserId = principal.getId();
        }
    }



}