import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DataRetriever {


    // Q1 — Nombre total de votes
    public long countAllVotes() {
        String sql = "SELECT COUNT(*) AS total_votes FROM vote";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("total_votes");
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR Q1] " + e.getMessage());
        }
        return 0L;
    }


    // Q2 — Nombre de votes par type
    public List<VoteTypeCount> countVotesByType() {
        String sql = """
                SELECT vote_type::TEXT AS vote_type,
                       COUNT(*)        AS count
                FROM   vote
                GROUP  BY vote_type
                ORDER  BY count DESC
                """;

        List<VoteTypeCount> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new VoteTypeCount(
                        rs.getString("vote_type"),
                        rs.getLong("count")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR Q2] " + e.getMessage());
        }
        return result;
    }


    // Q3 — Nombre de votes valides par candidat
    public List<CandidateVoteCount> countValidVotesByCandidate() {
        String sql = """
                SELECT c.name                                              AS candidate_name,
                       COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END)  AS valid_vote_count
                FROM   candidate c
                LEFT   JOIN vote v ON c.id = v.candidate_id
                GROUP  BY c.name
                ORDER  BY valid_vote_count DESC
                """;

        List<CandidateVoteCount> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new CandidateVoteCount(
                        rs.getString("candidate_name"),
                        rs.getLong("valid_vote_count")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR Q3] " + e.getMessage());
        }
        return result;
    }


    // Q4 — Synthèse globale (une seule ligne SQL)
    public VoteSummary computeVoteSummary() {
        String sql = """
                SELECT COUNT(CASE WHEN vote_type = 'VALID' THEN 1 END) AS valid_count,
                       COUNT(CASE WHEN vote_type = 'BLANK' THEN 1 END) AS blank_count,
                       COUNT(CASE WHEN vote_type = 'NULL'  THEN 1 END) AS null_count
                FROM   vote
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new VoteSummary(
                        rs.getLong("valid_count"),
                        rs.getLong("blank_count"),
                        rs.getLong("null_count")
                );
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR Q4] " + e.getMessage());
        }
        return new VoteSummary(0, 0, 0);
    }


    // Q5 — Taux de participation
    public double computeTurnoutRate() {
        String sql = """
                SELECT COUNT(DISTINCT voter_id)::float
                       / (SELECT COUNT(*) FROM voter) AS turnout_rate
                FROM   vote
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("turnout_rate");
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR Q5] " + e.getMessage());
        }
        return 0.0;
    }


    // Q6 — Candidat gagnant
    public ElectionResult findWinner() {
        String sql = """
                SELECT c.name   AS candidate_name,
                       COUNT(*) AS valid_vote_count
                FROM   candidate c
                JOIN   vote v ON c.id = v.candidate_id
                WHERE  v.vote_type = 'VALID'
                GROUP  BY c.name
                ORDER  BY valid_vote_count DESC, c.name ASC
                LIMIT  1
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new ElectionResult(
                        rs.getString("candidate_name"),
                        rs.getLong("valid_vote_count")
                );
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR Q6] " + e.getMessage());
        }
        return null;
    }
}