-- Seed some movies
INSERT INTO movies (title, year, poster_path, average_rating)
VALUES
    ('Inception',      2010, '/uploads/inception.jpg', 0.0),
    ('The Godfather',  1972, '/uploads/godfather.jpg', 0.0),
    ('Parasite',       2019, '/uploads/parasite.jpg', 0.0);

-- Seed genres for each movie
INSERT INTO movie_genres (movie_id, genre) VALUES
                                               (1, 'Sci-Fi'),
                                               (1, 'Thriller'),
                                               (2, 'Crime'),
                                               (2, 'Drama'),
                                               (3, 'Drama'),
                                               (3, 'Thriller'),
                                               (3, 'Comedy');


-- ==============================
-- Users (BCrypt‐encoded passwords)
-- ==============================
INSERT INTO users (email, password) VALUES
                                        ('alice@example.com', '$2b$12$G1aCua8AGhtWmu6SuRWYsOON2rtwW7RFy2oMe7hy7k4L5YMrbQCF.'),
                                        ('bob@example.com',   '$2b$12$HD0U/3GqMdIWU4kPluVQZOzXFGIMVi4155qhvKJHqRUyLh3AL45Xe'),
                                        ('admin@example.com', '$2b$12$j/IzWPOhAsgCrFwwu.sbMupUPu5TWO2SOEpSuYPeTipVPBqjtfid.');

-- ==============================
-- Seed roles for each user
-- ==============================
INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'USER'),
                                           (2, 'USER'),
                                           (3, 'ADMIN');


INSERT INTO watchlist_items (user_id, movie_id) VALUES
                                                    (1,1),
                                                    (2,2);

