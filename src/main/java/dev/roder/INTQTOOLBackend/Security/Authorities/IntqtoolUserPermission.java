package dev.roder.INTQTOOLBackend.Security.Authorities;

public enum IntqtoolUserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String permission;

    IntqtoolUserPermission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return this.permission;
    }
}
