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

-- ==============================================
-- 7) Stored_files metadata
--    (can test GET /files/{file_name}, and teacher can upload new via POST /files/upload)
-- ==============================================
INSERT INTO stored_files (file_name, file_type, size, file_download_uri) VALUES
                                                                             ('example.txt',   'text/plain',    1024, '/api/v1/files/example.txt'),
                                                                             ('inception.jpg', 'image/jpeg', 204800, '/api/v1/files/inception.jpg'),
                                                                             ('godfather.jpg', 'image/jpeg', 256000, '/api/v1/files/godfather.jpg'),
                                                                             ('parasite.jpg',  'image/jpeg', 198400, '/api/v1/files/parasite.jpg');

-- ==============================================
-- 8) Images table (optional)
--    if your app uses this table for uploads
-- ==============================================
INSERT INTO images (filename, content_type, uploaded_by) VALUES
                                                             ('inception.jpg', 'image/jpeg', 3),
                                                             ('godfather.jpg', 'image/jpeg', 3),
                                                             ('parasite.jpg',  'image/jpeg', 3);



-- -- Seed some movies
-- INSERT INTO movies (title, year, poster_path, average_rating)
-- VALUES
--     ('Inception',      2010, '/uploads/inception.jpg', 0.0),
--     ('The Godfather',  1972, '/uploads/godfather.jpg', 0.0),
--     ('Parasite',       2019, '/uploads/parasite.jpg', 0.0);
--
-- -- Seed genres for each movie
-- INSERT INTO movie_genres (movie_id, genre) VALUES
--                                                (1, 'Sci-Fi'),
--                                                (1, 'Thriller'),
--                                                (2, 'Crime'),
--                                                (2, 'Drama'),
--                                                (3, 'Drama'),
--                                                (3, 'Thriller'),
--                                                (3, 'Comedy');
--
--
-- -- ==============================
-- -- Users (BCrypt‐encoded passwords)
-- -- Seed data (passwords are BCrypt‐hashed versions of the values shown here):
-- --   alice@example.com  → secret123
-- --   bob@example.com    → password
-- --   admin@example.com  → adminpass
-- -- ==============================
--
--
-- -- ==============================
-- -- Users (BCrypt‐encoded passwords) with explicit created_at
-- -- ==============================
-- INSERT INTO users (email, password) VALUES
--                                                     ('alice@example.com', '$2b$12$G1aCua8AGhtWmu6SuRWYsOON2rtwW7RFy2oMe7hy7k4L5YMrbQCF.'),
--                                                     ('bob@example.com',   '$2b$12$HD0U/3GqMdIWU4kPluVQZOzXFGIMVi4155qhvKJHqRUyLh3AL45Xe'),
--                                                     ('admin@example.com', '$2b$12$j/IzWPOhAsgCrFwwu.sbMupUPu5TWO2SOEpSuYPeTipVPBqjtfid.');
--
--
--
--
-- -- ==============================
-- -- Seed roles for each user
-- -- ==============================
-- INSERT INTO user_roles (user_id, role) VALUES
--                                            (1, 'USER'),
--                                            (2, 'USER'),
--                                            (3, 'ADMIN');
--
--
-- INSERT INTO watchlist_items (user_id, movie_id) VALUES
--                                                     (1,1),
--                                                     (2,2);
--
-- -- ==============================
-- -- Create an “images” table
-- -- ==============================
-- CREATE TABLE IF NOT EXISTS images (
--                                       id           BIGSERIAL    PRIMARY KEY,
--                                       filename     VARCHAR(255) NOT NULL,
--     content_type VARCHAR(100) NOT NULL,
--     uploaded_by  BIGINT       REFERENCES users(id),
--     uploaded_at  TIMESTAMP    DEFAULT now()
--     );
--
-- -- 1) Add a couple of reviews so you can test rating‐related endpoints
-- INSERT INTO reviews (rating, created_at, movie_id, user_id, comment) VALUES
--                                                                          (5, '2025-01-01T10:00:00', 1, 1, 'Absolutely loved it!'),
--                                                                          (4, '2025-01-02T14:30:00', 2, 2, 'A true classic.'),
--                                                                          (3, '2025-01-03T09:20:00', 3, 1, 'Good but a bit slow at first.');
--
-- -- 2) Pre‐populate stored_files so file‐download endpoints can return something
-- INSERT INTO stored_files (file_name, file_type, size, file_download_uri) VALUES
--     ('example.txt', 'text/plain', 1024, '/api/v1/files/download/example.txt');
--
-- -- 3) An “edge‐case” movie with no genres
-- INSERT INTO movies (title, year, poster_path, average_rating, created_at) VALUES
--     ('Genreless Movie', 2021, '/uploads/genreless.jpg', 0.0, '2025-01-05T12:00:00');
-- -- (no corresponding INSERT into movie_genres for this ID)
--
-- -- 4) A user with no roles (to test 403s for authenticated but unauthorized)
-- INSERT INTO users (email, password) VALUES
--     ('norole@example.com', '$2b$12$ABCDEFGHIJK1234567890abcdefghijklmnopqrstuv');
-- -- (no entry in user_roles for this new user)
--
-- -- 5) A user with multiple roles (to test ADMIN vs USER logic)
-- INSERT INTO users (email, password) VALUES
--     ('multirole@example.com', '$2b$12$ZYXWVUTSRQPONMLKJIHGFDcbazyxwvutsrqpo');
-- INSERT INTO user_roles (user_id, role) VALUES
--                                            ( (SELECT id FROM users WHERE email='multirole@example.com'), 'USER'),
--                                            ( (SELECT id FROM users WHERE email='multirole@example.com'), 'ADMIN');
--
--
--
