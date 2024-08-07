package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.*;
import sk.stuba.fei.uim.oop.entity.people.*;
import sk.stuba.fei.uim.oop.utility.*;

import java.util.*;

public abstract class Organization implements OrganizationInterface{
    private String name;
//    private int org_budget = Constants.COMPANY_INIT_OWN_RESOURCES;
    protected int org_budget = 0;
    private Map<PersonInterface, Integer> employees;
    private Map<ProjectInterface, Integer> projectsBudget;
    private Set<GrantInterface> grants ;
    private Set<ProjectInterface> projects;
    public Organization() {
        this.employees = new HashMap<>();
        this.projects = new HashSet<>();
        this.grants = new HashSet<>();
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addEmployee(PersonInterface p, int employment) {
        this.employees.put(p, employment);
    }
    @Override
    public Set<PersonInterface> getEmployees() {
        return this.employees.keySet();
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        return this.employees.get(p);
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return this.projects;
    }

    @Override
    public Set<ProjectInterface> getRunningProjects(int year) {
        Set<ProjectInterface> runningProjects = new HashSet<>();
        for (ProjectInterface project : this.projects) {
            if (project.getStartingYear() > year && year < project.getEndingYear()){
                runningProjects.add(project);
            }
        }
        return runningProjects;
    }

    @Override
    public void registerProjectInOrganization(ProjectInterface project) {
        this.projects.add(project);
    }

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        if (!this.projects.contains(pi)) {
            return 0;
        }
        return get_project_budget_internal(pi);
//        if (this instanceof University) {
//            return pi.getTotalBudget();
//        } else if (this instanceof Company) {
//            int companyContribution = Math.min(Constants.COMPANY_INIT_OWN_RESOURCES, pi.getTotalBudget());
//            this.org_budget += companyContribution;
//            if(this.org_budget > Constants.COMPANY_INIT_OWN_RESOURCES){
//                return pi.getTotalBudget();
//            }else
//                return pi.getTotalBudget() + companyContribution;
//        }
//
//        return 0;
    }
    public void addGrant(GrantInterface grant) {
        this.grants.add(grant);
    }
    public Set<GrantInterface> getGrants() {
        return this.grants;
    }
    @Override
    public int getBudgetForAllProjects() {
        int totalBudget = 0;
        for (ProjectInterface project : getAllProjects()) {
            totalBudget += project.getTotalBudget();
        }
        return totalBudget;
    }

    @Override
    public void projectBudgetUpdateNotification(ProjectInterface pi, int year, int budgetForYear) {
        if (this.projects.contains(pi)) {

            if (this instanceof Company) {
                int budget = budgetForYear*Constants.PROJECT_DURATION_IN_YEARS;
                int companyContribution = Math.min(Constants.COMPANY_INIT_OWN_RESOURCES, budget);
//                pi.setBudgetForYear(year, budgetForYear + companyContribution);
                this.org_budget += companyContribution;
                if(this.org_budget >= Constants.COMPANY_INIT_OWN_RESOURCES){
                    projectsBudget.put(pi, budget);
                }else
                    projectsBudget.put(pi, budget + companyContribution);
            }else
                pi.setBudgetForYear(year, budgetForYear);
        }
    }

    public abstract int get_project_budget_internal(ProjectInterface pi);

}
