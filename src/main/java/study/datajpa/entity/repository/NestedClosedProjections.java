package study.datajpa.entity.repository;

public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();
    interface TeamInfo{
        String getName();
    }
}
