import java.util.List;

/**
 * Main — point d'entrée du programme.
 * Lance toutes les requêtes Q1 à Q6 et affiche les résultats.
 */
public class Main {

    public static void main(String[] args) {

        DataRetriever retriever = new DataRetriever();

        // ── Q1 ──────────────────────────────────────────────────────
        long totalVote = retriever.countAllVotes();
        System.out.println("Q1 → totalVote=" + totalVote);
        // Attendu : totalVote=6

        // ── Q2 ──────────────────────────────────────────────────────
        List<VoteTypeCount> votesByType = retriever.countVotesByType();
        System.out.println("Q2 → " + votesByType);
        // Attendu : [VoteTypeCount(voteType=VALID, count=3), VoteTypeCount(voteType=BLANK, count=2), VoteTypeCount(voteType=NULL, count=1)]

        // ── Q3 ──────────────────────────────────────────────────────
        List<CandidateVoteCount> validByCandidate = retriever.countValidVotesByCandidate();
        System.out.println("Q3 → " + validByCandidate);
        // Attendu : [Alice=2, Bob=1, Charlie=0]

        // ── Q4 ──────────────────────────────────────────────────────
        VoteSummary summary = retriever.computeVoteSummary();
        System.out.println("Q4 → " + summary);
        // Attendu : VoteSummary(validCount=3, blankCount=2, nullCount=1)

        // ── Q5 ──────────────────────────────────────────────────────
        double turnoutRate = retriever.computeTurnoutRate();
        System.out.printf("Q5 → turnoutRate=%.0f%%%n", turnoutRate * 100);
        // Attendu : turnoutRate=100%

        // ── Q6 ──────────────────────────────────────────────────────
        ElectionResult winner = retriever.findWinner();
        System.out.println("Q6 → " + winner);
        // Attendu : ElectionResult(candidateName=Alice, validVoteCount=2)
    }
}