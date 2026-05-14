import java.util.List;


public interface Problem {

    List<Clause> constructClauses();

    default StringBuffer toProlog() {
        List<Clause> clauses = constructClauses();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (Clause c : clauses)
            buffer.append("addone(").append(c.toProlog()).append(",M" + i + ",M" + (++i) + "),\n");
        return buffer;
    }

    default StringBuffer toDIMACS() {
        List<Clause> clauses = constructClauses();
        StringBuffer buffer = new StringBuffer();
        buffer.append("p cnf " + clauses.get(0).propsCount + " " + clauses.size()).append('\n');
        for (Clause c : clauses)
            buffer.append(c.toDIMACS() + " 0").append('\n');
        return buffer;
    }

}
