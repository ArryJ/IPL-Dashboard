package org.personal.ipldashboarddemo.repository;

import org.personal.ipldashboarddemo.entities.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable firstPage);
    // Pageable used to limit the number of matches fetched to count
    default List<Match> findLatesMatchesByTeamName(String teamName, int count){
        return findByTeam1OrTeam2OrderByDateDesc(teamName,teamName,
                PageRequest.of(0,count));
    }

    // Both below methods return the same results
    @Query("select m from Match m where (m.team1 = :teamName or m.team2 = :teamName) and " +
            "m.date between :startDate and :endDate order by m.date desc")
    List<Match> getMatchesForTeamByYear(
            @Param("teamName") String teamName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    List<Match> findByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(
        String team1, LocalDate date1, LocalDate date2,
        String team2, LocalDate date3, LocalDate date4
    );

}
