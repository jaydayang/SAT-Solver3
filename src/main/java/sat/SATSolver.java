package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    static Clause IndividualClause=null;
    static Literal a;

    public static Environment solve(Formula formula) {
        Environment env = new Environment();
        ImList<Clause> clsList = formula.getClauses();
        System.out.println("solve" + env.toString());
        System.out.println("solve1" + clsList.toString());
        IndividualClause=clsList.first();
        for (int i = 0; i < clsList.size()-1; i++) {
//            if (IndividualClause == null) {
//                IndividualClause = clsList.first();
//                clsList = clsList.rest();
//            } else if (clsList.first() == null) {
//                clsList = clsList.rest();
//            } else {

                clsList = clsList.rest();
//                System.out.println("solve" + i + IndividualClause.toString());
                if ((IndividualClause.size() > clsList.first().size())) {
                    IndividualClause = clsList.first();
                }
            }
//        }
            a = IndividualClause.chooseLiteral();
            System.out.println("chooseLiteral" + a.toString());
            if (IndividualClause.isUnit()) {
                env = env.putTrue(IndividualClause.chooseLiteral().getVariable());
            } else {
                env = env.putTrue(IndividualClause.chooseLiteral().getVariable());
                if (solve(substitute(formula.getClauses(), a), env) == null) {
                    env = env.putFalse(IndividualClause.chooseLiteral().getVariable());
                    a = a.getNegation();
                }
            }
            System.out.println("solve" + substitute(formula.getClauses(), a).toString());
            return solve(substitute(formula.getClauses(), a), env);

            // TODO: implement this.
        }


    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
          System.out.println("solve2"+env.toString());
        System.out.println("solve2"+clauses.toString());
        ImList<Clause>newClauses=clauses;
//        IndividualClause=null;
        if (clauses.isEmpty()) {
            System.out.println("finish");
            return env;
        } else if (SATSolver.ifEmpty(clauses)) {
            return null;
        } else {
            IndividualClause=newClauses.first();
                for (int i = 0; i < newClauses.size()-1; i++) {
//                if(IndividualClause==null){

//                newClauses=newClauses.rest();
//            }else if(newClauses.first()==null){
//                newClauses=newClauses.rest();
//            }else{
                    newClauses = newClauses.rest();

//                    System.out.println("solve2" + i + IndividualClause.toString());
                    if (IndividualClause.size() > newClauses.first().size()) {
                        IndividualClause = newClauses.first();
                    }

                }


//            }
//            if(IndividualClause==null){
//                return env;}else {
//                a = IndividualClause.chooseLiteral();
//            }
            a = IndividualClause.chooseLiteral();
            System.out.println("solve2choose"+a.toString());
                if (IndividualClause.isUnit()) {
                    env = env.putTrue(IndividualClause.chooseLiteral().getVariable());
                } else {
                    env = env.putTrue(IndividualClause.chooseLiteral().getVariable());
                    if (solve(substitute(clauses, a), env) == null) {
                        env = env.putFalse(IndividualClause.chooseLiteral().getVariable());
                        a = a.getNegation();
                    }
                }
            System.out.println(substitute(clauses,a));
                return solve(substitute(clauses, a), env);

            }
        }






    public static boolean ifEmpty(ImList<Clause> clause){
        for(int i=0;i<clause.size();i++){
        if(clause.first()!=null) {
            if (clause.first().isEmpty()) {
                return true;
            } else {
                clause = clause.rest();
            }
        }
        }return false;
    }



    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        ImList<Clause> clausesRest = new EmptyImList<>();
        while (!clauses.isEmpty()) {
//            if(clauses.first()!=null) {
                if (clauses.first().contains(l)) {
                    if(clauses.first().reduce(l)!=null){
                    clausesRest = clausesRest.add(clauses.first().reduce(l));}
                }
        else if (clauses.first().contains(l.getNegation())) {
                    Clause x = new Clause();
                    for (Literal n : clauses.first()) {
                        if (!n.equals(l.getNegation())) {
                            x = x.add(n);
                        }
                    }
                    clausesRest = clausesRest.add(x);
                } else {
                    clausesRest = clausesRest.add(clauses.first());
                }
//            }
            clauses=clauses.rest();
        }
        return clausesRest;
    }
}

