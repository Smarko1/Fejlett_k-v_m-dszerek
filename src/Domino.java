import java.util.ArrayList;
import java.util.List;

public class Domino implements Problem {

    private final int helyezesek = 5;
    private final int kategoria_elemek = 5;
    private final int osszes_valtozo = 75;

    private int vezeteknev(int helyezes, int nevIndex) {
        return helyezes * kategoria_elemek + nevIndex;
    }

    private int keresztnev(int helyezes, int nevIndex) {
        return 25 + helyezes * kategoria_elemek + nevIndex;
    }

    private int klub(int helyezes, int klubIndex) {
        return 50 + helyezes * kategoria_elemek + klubIndex;
    }

    private void kizarjaEgymast(List<Clause> clauses, int valtozo1, int valtozo2) {
        Clause clause = new Clause(osszes_valtozo);
        clause.set(valtozo1, false);
        clause.set(valtozo2, false);
        clauses.add(clause);
    }

    private void pontosanEgyIgaz(List<Clause> clauses, int[] valtozok) {
        Clause legalabbEgyIgaz = new Clause(osszes_valtozo);
        for (int valtozo : valtozok) {
            legalabbEgyIgaz.set(valtozo, true);
        }
        clauses.add(legalabbEgyIgaz);

        for (int i = 0; i < valtozok.length; i++) {
            for (int j = i + 1; j < valtozok.length; j++) {
                kizarjaEgymast(clauses, valtozok[i], valtozok[j]);
            }
        }
    }

    private void elotteVegzett(List<Clause> clauses, int[] A, int[] B) {
        for (int i = 0; i < helyezesek; i++) {
            for (int j = 0; j <= i; j++) {
                kizarjaEgymast(clauses, A[i], B[j]);
            }
        }
    }

    private void kozvetlenulElotteVegzett(List<Clause> clauses, int[] A, int[] B) {
        for (int i = 0; i < helyezesek; i++) {
            for (int j = 0; j < helyezesek; j++) {
                if (j != i + 1) {
                    kizarjaEgymast(clauses, A[i], B[j]);
                }
            }
        }
    }

    private int[] kategoriaValtozoi(int kategoria, int elemIndex) {
        int[] valtozok = new int[helyezesek];
        for (int hely = 0; hely < helyezesek; hely++) {
            if (kategoria == 0) valtozok[hely] = vezeteknev(hely, elemIndex);
            else if (kategoria == 1) valtozok[hely] = keresztnev(hely, elemIndex);
            else valtozok[hely] = klub(hely, elemIndex);
        }
        return valtozok;
    }

    @Override
    public List<Clause> constructClauses() {
        List<Clause> clauses = new ArrayList<>();

        for (int hely = 0; hely < helyezesek; hely++) {
            pontosanEgyIgaz(clauses,
                    new int[]{
                            vezeteknev(hely, 0),
                            vezeteknev(hely, 1),
                            vezeteknev(hely, 2),
                            vezeteknev(hely, 3),
                            vezeteknev(hely, 4)});

            pontosanEgyIgaz(clauses,
                    new int[]{
                            keresztnev(hely, 0),
                            keresztnev(hely, 1),
                            keresztnev(hely, 2),
                            keresztnev(hely, 3),
                            keresztnev(hely, 4)});

            pontosanEgyIgaz(clauses,
                    new int[]{
                            klub(hely, 0),
                            klub(hely, 1),
                            klub(hely, 2),
                            klub(hely, 3),
                            klub(hely, 4)});
        }

        for (int elem = 0; elem < kategoria_elemek; elem++) {
            pontosanEgyIgaz(clauses, kategoriaValtozoi(0, elem));
            pontosanEgyIgaz(clauses, kategoriaValtozoi(1, elem));
            pontosanEgyIgaz(clauses, kategoriaValtozoi(2, elem));
        }

        int[] dombos = kategoriaValtozoi(0, 0);
        int[] gombos = kategoriaValtozoi(0, 1);
        int[] kabos = kategoriaValtozoi(0, 2);
        int[] lombos = kategoriaValtozoi(0, 3);
        int[] zabos = kategoriaValtozoi(0, 4);

        int[] amalia = kategoriaValtozoi(1, 0);
        int[] cecilia = kategoriaValtozoi(1, 1);
        int[] emilia = kategoriaValtozoi(1, 2);
        int[] otilia = kategoriaValtozoi(1, 3);
        int[] rozalia = kategoriaValtozoi(1, 4);

        int[] anyokak = kategoriaValtozoi(2, 0);
        int[] mamikak = kategoriaValtozoi(2, 1);
        int[] nagyiKlub = kategoriaValtozoi(2, 2);
        int[] oregNenek = kategoriaValtozoi(2, 3);
        int[] sztarKlub = kategoriaValtozoi(2, 4);

        elotteVegzett(clauses, gombos, cecilia);
        elotteVegzett(clauses, gombos, sztarKlub);
        elotteVegzett(clauses, zabos, gombos);

        kozvetlenulElotteVegzett(clauses, anyokak, emilia);
        kozvetlenulElotteVegzett(clauses, emilia, dombos);

        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, keresztnev(hely, 4), klub(hely, 2));
        }

        elotteVegzett(clauses, lombos, amalia);
        elotteVegzett(clauses, amalia, oregNenek);

        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, vezeteknev(hely, 2), keresztnev(hely, 1));
            kizarjaEgymast(clauses, vezeteknev(hely, 2), keresztnev(hely, 4));
        }

        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, vezeteknev(hely, 3), keresztnev(hely, 2));
        }

        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, vezeteknev(hely, 1), klub(hely, 3));
        }

        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, keresztnev(hely, 2), klub(hely, 4));
        }

        return clauses;
    }

    public static void main(String[] args) {
        System.out.println(new Domino().toDIMACS());
    }
}