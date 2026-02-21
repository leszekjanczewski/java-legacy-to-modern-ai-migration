-- Seed data - Boot 1.5 automatycznie wykonuje data.sql przy starcie dla embedded datasource
-- Boot 2.5+ wymaga spring.sql.init.mode=always
-- Sekwencja ID zaczyna się od 1 (GenerationType.AUTO z H2 używa hibernate_sequence)
-- UWAGA: kolejność INSERT musi pasować do ddl-auto=create-drop (tabele tworzone przez Hibernate)

INSERT INTO customer (id, first_name, last_name, age, monthly_income, employment_years, credit_history, created_at)
VALUES (1, 'Jan', 'Kowalski', 35, 6000.00, 8, 'GOOD', NOW());

INSERT INTO customer (id, first_name, last_name, age, monthly_income, employment_years, credit_history, created_at)
VALUES (2, 'Anna', 'Nowak', 22, 2500.00, 1, 'NONE', NOW());

INSERT INTO customer (id, first_name, last_name, age, monthly_income, employment_years, credit_history, created_at)
VALUES (3, 'Robert', 'Wiśniewski', 45, 1500.00, 3, 'BAD', NOW());
