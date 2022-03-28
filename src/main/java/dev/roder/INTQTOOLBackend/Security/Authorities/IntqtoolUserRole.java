package dev.roder.INTQTOOLBackend.Security.Authorities;

import com.google.common.collect.Sets;

import java.util.Set;

public enum IntqtoolUserRole {
    STUDENT(Sets.newHashSet(IntqtoolUserPermission.COURSE_READ,
            IntqtoolUserPermission.STUDENT_READ,
            IntqtoolUserPermission.STUDENT_WRITE)),

    TEACHER(Sets.newHashSet(IntqtoolUserPermission.COURSE_READ,
            IntqtoolUserPermission.STUDENT_READ,
            IntqtoolUserPermission.STUDENT_WRITE,
            IntqtoolUserPermission.COURSE_WRITE)),
    ADMIN(Sets.newHashSet(IntqtoolUserPermission.COURSE_READ,
            IntqtoolUserPermission.STUDENT_READ,
            IntqtoolUserPermission.STUDENT_WRITE,
            IntqtoolUserPermission.COURSE_WRITE));

    private final Set<IntqtoolUserPermission> permissions;

    IntqtoolUserRole(Set<IntqtoolUserPermission> permissions){
        this.permissions = permissions;
    }


}
