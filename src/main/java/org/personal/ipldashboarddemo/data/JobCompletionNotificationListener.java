package org.personal.ipldashboarddemo.data;

import org.personal.ipldashboarddemo.entities.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

//    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    // this is constructor injection
    public JobCompletionNotificationListener(EntityManager entityManager){
        this.entityManager = entityManager;
    }
//    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

        Map<String, Team> teamData = new HashMap<>();

        entityManager.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
                     .getResultList()
                     .stream()
                     .map(object -> new Team((String) object[0], (Long) object[1]))
                     .forEach(team -> teamData.put(team.getTeamName(), team));

        entityManager.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
                     .getResultList()
                     .stream()
                     .forEach(object -> {
                         Team team = teamData.get((String) object[0]);
                         team.setTotalMatches(team.getTotalMatches() + (long) object[1]);
                     });

        entityManager.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
                     .getResultList()
                     .stream()
                     .forEach(object -> {
                        Team team = teamData.get((String) object[0]);
                        if(team != null) team.setTotalWins((long) object[1]);
                    });

        // Team table gets populated here.
        teamData.values().forEach(team -> entityManager.persist(team));
        teamData.values().forEach(team -> System.out.println(team.toString()));
//            jdbcTemplate.query("SELECT team1, team2, date FROM match",
//                    (rs, row) -> "Team 1 "+rs.getString(1)+" Team 2 "+ rs.getString(2)+
//                    " Date "+rs.getString(3)
//            ).forEach(str -> System.out.println(str));
        }
    }
}
