package org.personal.ipldashboarddemo.repository;

import org.personal.ipldashboarddemo.entities.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    public Team findByTeamName(String teamName);
}
