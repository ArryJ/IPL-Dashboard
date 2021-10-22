package org.personal.ipldashboarddemo.controller;

import org.personal.ipldashboarddemo.entities.Match;
import org.personal.ipldashboarddemo.entities.Team;
import org.personal.ipldashboarddemo.repository.MatchRepository;
import org.personal.ipldashboarddemo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
public class TeamController {
    private TeamRepository teamRepository;
    private MatchRepository matchRepository;
    // Constructor Injection
    @Autowired
    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team")
    public Iterable<Team> getTeamList(){
        return this.teamRepository.findAll();
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName){
        Team team = this.teamRepository.findByTeamName(teamName);
        team.setMatches(this.matchRepository.findLatesMatchesByTeamName(teamName,4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year){
        LocalDate startDate = LocalDate.of(year, 1,1);
        LocalDate endDate = LocalDate.of(year+1, 1,1);

        return this.matchRepository.getMatchesForTeamByYear(teamName, startDate, endDate);
    }


}
