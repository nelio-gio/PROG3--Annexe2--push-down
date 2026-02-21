CREATE USER  nelio_election WITH PASSWORD 'postgres';

GRANT ALL PRIVILEGES ON TABLE candidate TO nelio_election;
GRANT ALL PRIVILEGES ON TABLE voter     TO nelio_election;
GRANT ALL PRIVILEGES ON TABLE vote      TO nelio_election;

GRANT ALL PRIVILEGES ON SEQUENCE candidate_id_seq TO nelio_election;
GRANT ALL PRIVILEGES ON SEQUENCE voter_id_seq     TO nelio_election;
GRANT ALL PRIVILEGES ON SEQUENCE vote_id_seq      TO nelio_election;