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

        // Gombos jobb helyezést ért el, mint Cecília és mint a Sztár-Klub versenyzője, de rosszabbat, mint Zabos.
        elotteVegzett(clauses, gombos, cecilia);
        elotteVegzett(clauses, gombos, sztarKlub);
        elotteVegzett(clauses, zabos, gombos);

        // Emiatt volt több megoldás
        // Nem volt előzőleg beleírva hogy Cecília ne legyen a Sztár-Klub tagja
        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, keresztnev(hely, 1), klub(hely, 4));
        }


        // Az Anyókák képviselője közvetlenül Emília előtt végzett, aki közvetlenül Dombos előtt végzett a bajnokságon.
        kozvetlenulElotteVegzett(clauses, anyokak, emilia);
        kozvetlenulElotteVegzett(clauses, emilia, dombos);


        // Rozália nem a Nagyi-Klub tagja.
        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, keresztnev(hely, 4), klub(hely, 2));
        }

        // Lombos valahol Amália előtt, aki valahol az Öreg Nénék versenyzője előtt végzett.
        elotteVegzett(clauses, lombos, amalia);
        elotteVegzett(clauses, amalia, oregNenek);

        // Kabos keresztneve nem Cecília és nem Rozália.
        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, vezeteknev(hely, 2), keresztnev(hely, 1));
            kizarjaEgymast(clauses, vezeteknev(hely, 2), keresztnev(hely, 4));
        }

        // Lombos keresztneve nem Emília.
        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, vezeteknev(hely, 3), keresztnev(hely, 2));
        }

        // Gombos nem az Öreg nénék képviseletében játszott.
        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, vezeteknev(hely, 1), klub(hely, 3));
        }

        // Emília nem a Sztár-Klub tagja.
        for (int hely = 0; hely < helyezesek; hely++) {
            kizarjaEgymast(clauses, keresztnev(hely, 2), klub(hely, 4));
        }

        return clauses;
    }

    // Megoldás kiírása
    public void megoldasKiirasa(String satOutput) {
        String[] vezeteknevek = {"Dombos", "Gombos", "Kabos", "Lombos", "Zabos"};
        String[] keresztnevek = {"Amália", "Cecília", "Emília", "Otília", "Rozália"};
        String[] klubok = {"Anyókák", "Mamikák", "Nagyi-Klub", "Öreg Nénék", "Sztár-Klub"};

        String[] eredmenyekVezeteknev = new String[5];
        String[] eredmenyekKeresztnev = new String[5];
        String[] eredmenyekKlub = new String[5];

        String[] tokens = satOutput.replace("SAT", "").trim().split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty() || token.equals("0")) continue;

            int val = Integer.parseInt(token);

            if (val > 0) {
                if (val >= 1 && val <= 25) {
                    int alap = val - 1;
                    int helyezes = alap / 5;
                    int nevIndex = alap % 5;
                    eredmenyekVezeteknev[helyezes] = vezeteknevek[nevIndex];
                } else if (val >= 26 && val <= 50) {
                    int alap = val - 26;
                    int helyezes = alap / 5;
                    int nevIndex = alap % 5;
                    eredmenyekKeresztnev[helyezes] = keresztnevek[nevIndex];
                } else if (val >= 51 && val <= 75) {
                    int alap = val - 51;
                    int helyezes = alap / 5;
                    int nevIndex = alap % 5;
                    eredmenyekKlub[helyezes] = klubok[nevIndex];
                }
            }
        }

        System.out.println("--- A BAJNOKSÁG VÉGEREDMÉNYE ---");
        for (int i = 0; i < 5; i++) {
            System.out.println((i + 1) + ". helyezett: "
                    + eredmenyekVezeteknev[i] + " "
                    + eredmenyekKeresztnev[i] + " ("
                    + eredmenyekKlub[i] + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println(new Domino().toDIMACS());

        Domino domino = new Domino();

        String solverEredmeny = "-1 -2 -3 4 -5 -6 -7 -8 -9 10 -11 12 -13 -14 -15 16 -17 -18 -19 -20 -21 -22 23 -24 -25 -26 -27 -28 -29 30 31 -32 -33 -34 -35 -36 -37 38 -39 -40 -41 42 -43 -44 -45 -46 -47 -48 49 -50 -51 52 -53 -54 -55 56 -57 -58 -59 -60 -61 -62 63 -64 -65 -66 -67 -68 69 -70 -71 -72 -73 -74 75";

        domino.megoldasKiirasa(solverEredmeny);
    }
}