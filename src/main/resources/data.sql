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
-- Seed some users
-- ==============================
INSERT INTO users (email, password) VALUES
                                        ('alice@example.com', 'secret123'),
                                        ('bob@example.com',   'password'),
                                        ('admin@example.com', 'adminpass');

-- ==============================
-- Seed roles for each user
-- ==============================
INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'USER'),
                                           (2, 'USER'),
                                           (3, 'ADMIN');
