-- ==============================================
-- 1) Movies
-- ==============================================
INSERT INTO movies (title, year, poster_path, average_rating) VALUES
                                                                  ('Inception',       2010, '/uploads/inception.jpg',    0.0),
                                                                  ('The Godfather',   1972, '/uploads/godfather.jpg',    0.0),
                                                                  ('Parasite',        2019, '/uploads/parasite.jpg',     0.0),
                                                                  ('Genreless Movie', 2021, '/uploads/genreless.jpg',    0.0);

-- ==============================================
-- 2) Genres
-- ==============================================
INSERT INTO movie_genres (movie_id, genre) VALUES
                                               (1, 'Sci-Fi'),
                                               (1, 'Thriller'),
                                               (2, 'Crime'),
                                               (2, 'Drama'),
                                               (3, 'Drama'),
                                               (3, 'Thriller'),
                                               (3, 'Comedy');

-- ==============================================
-- 3) Users
--    (alice@example.com → secret123
--     bob@example.com   → password
--     admin@example.com → adminpass)
-- ==============================================
INSERT INTO users (email, password) VALUES
                                        ('alice@example.com', '$2b$12$G1aCua8AGhtWmu6SuRWYsOON2rtwW7RFy2oMe7hy7k4L5YMrbQCF.'),
                                        ('bob@example.com',   '$2b$12$HD0U/3GqMdIWU4kPluVQZOzXFGIMVi4155qhvKJHqRUyLh3AL45Xe'),
                                        ('admin@example.com', '$2b$12$j/IzWPOhAsgCrFwwu.sbMupUPu5TWO2SOEpSuYPeTipVPBqjtfid.');

-- ==============================================
-- 4) Roles
-- ==============================================
INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'USER'),
                                           (2, 'USER'),
                                           (3, 'ADMIN');

-- ==============================================
-- 5) Watchlist items
--    (can test GET /watchlist, POST /users/{id}/watchlist, DELETE /watchlist/{id})
-- ==============================================
INSERT INTO watchlist_items (user_id, movie_id) VALUES
                                                    (1, 1),  -- Alice has Inception
                                                    (1, 2),  -- Alice has The Godfather
                                                    (2, 3),  -- Bob has Parasite
                                                    (3, 1);  -- Admin has Inception

-- ==============================================
-- 6) Reviews
--    (can test GET /reviews, POST/PUT/DELETE /reviews)
-- ==============================================
INSERT INTO reviews (rating, created_at, movie_id, user_id, comment) VALUES
                                                                         (5, '2025-06-02T10:00:00', 1, 1, 'Mind-bending masterpiece!'),
                                                                         (4, '2025-06-03T11:30:00', 2, 2, 'A true classic.'),
                                                                         (3, '2025-06-04T12:45:00', 3, 1, 'Entertaining but uneven.');
